package ru.surf.gallery.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDao
import ru.surf.gallery.utils.createUpdatedPost
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val postDao: PostDao
) : ViewModel() {

    private val mutableSearchStatus = MutableLiveData(SearchStatus.NOT_SEARCHING)
    val searchStatus: LiveData<SearchStatus> = mutableSearchStatus

    val postsFromDao = postDao.getAll()
    private var postsToMatch = listOf<Post>()
    private val mutablePostsToShow = MutableLiveData<List<Post>>()
    val postsToShow: LiveData<List<Post>> = mutablePostsToShow

    private var searchText = ""

    fun setPostsToMatch(newPostsList: List<Post>) {
        postsToMatch = newPostsList
        findMatchingPosts(searchText) // Для актуальности postsToShow при обновлении всего списка
    }

    fun findMatchingPosts(text: String) {
        searchText = text

        var newPostsList = emptyList<Post>()
        if (text.isNotEmpty()) {
            newPostsList = getMatchingPosts(text)
            mutablePostsToShow.value = newPostsList
        }
        setSearchStatus(text, newPostsList)
    }

    private fun getMatchingPosts(text: String): List<Post> {
        return postsToMatch.filter {
            text.lowercase() in it.title.lowercase()
        }
    }

    private fun setSearchStatus(text: String, newPostsList: List<Post>) {
        when {
            text.isEmpty() -> mutableSearchStatus.value = SearchStatus.NOT_SEARCHING
            newPostsList.isEmpty() -> mutableSearchStatus.value = SearchStatus.NO_RESULTS
            else -> mutableSearchStatus.value = SearchStatus.SHOW_RESULTS
        }
    }

    fun featuredIconClicked(post: Post) {
        viewModelScope.launch {
            when (post.inFeatured) {
                true -> removeFromFeatured(post)
                false -> addToFeatured(post)
            }
        }
    }

    private suspend fun addToFeatured(post: Post) {
        postDao.update(post.createUpdatedPost(true))
    }

    private suspend fun removeFromFeatured(post: Post) {
        postDao.update(post.createUpdatedPost(false))
    }
}