package com.nero.instagram_clone.viewModel

import androidx.lifecycle.ViewModel
import com.nero.instagram_clone.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InstaViewModel @Inject constructor(val myRepository: MyRepository) : ViewModel() {
}