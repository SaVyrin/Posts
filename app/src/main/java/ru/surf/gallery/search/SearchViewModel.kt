package ru.surf.gallery.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val mutableSearchStatus = MutableLiveData(SearchStatus.NOT_SEARCHING)
    val searchStatus: LiveData<SearchStatus> = mutableSearchStatus

    private val mutableSearchText = MutableLiveData("")
    val searchText: LiveData<String> = mutableSearchText

    fun setSearchText(text: String) {
        mutableSearchText.value = text
    }

    fun setSearchStatus(text: String) {
        when (text) {
            "" -> mutableSearchStatus.value = SearchStatus.NOT_SEARCHING
            else -> mutableSearchStatus.value = SearchStatus.SHOW_RESULTS
        }
    }

}