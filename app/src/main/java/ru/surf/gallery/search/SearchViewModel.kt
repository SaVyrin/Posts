package ru.surf.gallery.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.surf.gallery.database.Post
import ru.surf.gallery.database.PostDao

class SearchViewModel(
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
        findMatchingPosts(searchText) // TODO это нужно чтобы сердечка сразу перекрашивалась
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
        val newPostsList = mutableListOf<Post>()
        for (post in postsToMatch) {
            if (text.lowercase() in post.title.lowercase()) {
                // TODO узнать, нужно ли искать без учёта регистра
                newPostsList.add(post)
            }
        }
        return newPostsList
    }

    private fun setSearchStatus(text: String, newPostsList: List<Post>) {
        when {
            text.isEmpty() -> mutableSearchStatus.value = SearchStatus.NOT_SEARCHING
            newPostsList.isEmpty() -> mutableSearchStatus.value = SearchStatus.NO_RESULTS
            else -> mutableSearchStatus.value = SearchStatus.SHOW_RESULTS
        }
    }

    suspend fun featuredIconClicked(post: Post) {
        when (post.inFeatured) {
            true -> removeFromFeatured(post)
            false -> addToFeatured(post)
        }
    }

    private suspend fun addToFeatured(post: Post) {
        postDao.update(createUpdatedPost(true, post))
    }

    private suspend fun removeFromFeatured(post: Post) {
        postDao.update(createUpdatedPost(false, post))
    }

    private fun createUpdatedPost(inFeatured: Boolean, post: Post): Post {
        val currentTime = System.currentTimeMillis()
        return Post(
            post.id,
            post.title,
            post.content,
            post.photoUrl,
            post.publicationDate,
            inFeatured,
            currentTime
        )
    }
}