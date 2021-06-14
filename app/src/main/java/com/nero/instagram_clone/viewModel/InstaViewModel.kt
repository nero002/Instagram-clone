package com.nero.instagram_clone.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nero.instagram_clone.models.module.search.Data
import com.nero.instagram_clone.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstaViewModel @Inject constructor(val myRepository: MyRepository) : ViewModel() {

    private var searchDataList = MutableLiveData<List<Data>>(listOf())


    fun search(): MutableLiveData<List<Data>> {
        viewModelScope.launch {

            val result = myRepository.getSearchResults()
            searchDataList.value = result.
        }
        return searchDataList
    }

    val search = myRepository.getSearchResults().cachedIn(viewModelScope)

}