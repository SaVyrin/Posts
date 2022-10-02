package ru.surf.gallery.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.surf.gallery.data.database.Post
import ru.surf.gallery.data.database.PostDao
import ru.surf.gallery.data.database.UserToken
import ru.surf.gallery.data.database.UserTokenDao
import ru.surf.gallery.domain.PostsRepositoryVSUImpl
import ru.surf.gallery.utils.createUpdatedPost
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userTokenDao: UserTokenDao,
    private val postDao: PostDao,
    private val postsRepository: PostsRepositoryVSUImpl
) : ViewModel() {

    private val mutableSearchStatus = MutableLiveData(SearchStatus.NOT_SEARCHING)
    val searchStatus: LiveData<SearchStatus> = mutableSearchStatus

    val postsFromDao = MutableLiveData<List<Post>>()
    private var postsToMatch = listOf<Post>()
    private val mutablePostsToShow = MutableLiveData<List<Post>>()
    val postsToShow: LiveData<List<Post>> = mutablePostsToShow

    val user = userTokenDao.get()
    private var userId = ""

    private var searchText = ""

    fun setUserId(userFromDao: UserToken) {
        userId = userFromDao.token
        viewModelScope.launch {
            postsFromDao.value = postsRepository.getPosts(userId)
        }
    }

    fun setPostsToMatch(newPostsList: List<Post>) {
        postsToMatch = newPostsList
        findMatchingPosts(searchText) // Для актуальности postsToShow при обновлении всего списка
    }

    fun findMatchingPosts(text: String) {
        viewModelScope.launch(Dispatchers.Default) {
            searchText = text

            var newPostsList = emptyList<Post>()
            if (text.isNotEmpty()) {
                newPostsList = getMatchingPosts(text)
                mutablePostsToShow.postValue(newPostsList)
            }
            setSearchStatus(text, newPostsList)
        }
    }

    private fun getMatchingPosts(text: String): List<Post> {
        return postsToMatch.filter {
            text.lowercase() in it.title.lowercase()
        }
    }

    private fun setSearchStatus(text: String, newPostsList: List<Post>) {
        when {
            text.isEmpty() -> mutableSearchStatus.postValue(SearchStatus.NOT_SEARCHING)
            newPostsList.isEmpty() -> mutableSearchStatus.postValue(SearchStatus.NO_RESULTS)
            else -> mutableSearchStatus.postValue(SearchStatus.SHOW_RESULTS)
        }
    }

    fun featuredIconClicked(post: Post) {
        viewModelScope.launch {
            when (post.inFeatured) {
                true -> removeFromFeatured(post)
                false -> addToFeatured(post)
            }
            postsFromDao.value = postsRepository.getPosts(userId)
        }
    }

    private suspend fun addToFeatured(post: Post) {
        postsRepository.addToFeatured(userId, post)
    }

    private suspend fun removeFromFeatured(post: Post) {
        postsRepository.removeFromFeatured(userId, post)
    }
}