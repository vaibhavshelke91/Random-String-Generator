package com.vaibhav.randomstringgenerator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vaibhav.randomstringgenerator.data.model.RandomString

import com.vaibhav.randomstringgenerator.data.model.StringModel
import com.vaibhav.randomstringgenerator.data.repository.StringRepository
import com.vaibhav.randomstringgenerator.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StringRepository
)  : ViewModel(){

    private val _randomStrings = MutableStateFlow<UiState<RandomString>>(UiState.Idle)

    val randomString = _randomStrings.asStateFlow()

    fun generateByLength(len:Int){
        viewModelScope.launch {
            _randomStrings.value=UiState.Loading
            val value= repository.getByLength(len)
            if (value == null) {
                _randomStrings.value=UiState.Error("Unable to generate random string")
            }else{
                _randomStrings.value=UiState.Success(value)
            }

            Log.d("Value",value.toString())

        }
    }




}
