package com.vaibhav.randomstringgenerator.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vaibhav.randomstringgenerator.data.db.StringModelEntity
import com.vaibhav.randomstringgenerator.data.model.RandomString

import com.vaibhav.randomstringgenerator.data.model.StringModel
import com.vaibhav.randomstringgenerator.data.repository.StringDatabaseRepository
import com.vaibhav.randomstringgenerator.data.repository.StringRepository
import com.vaibhav.randomstringgenerator.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StringRepository,
    private val databaseRepository: StringDatabaseRepository
) : ViewModel() {

    private val _randomStrings = MutableStateFlow<UiState<RandomString>>(UiState.Idle)

    val randomString = _randomStrings.asStateFlow()

    val dbStrings = databaseRepository.getAll().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun delete(stringModelEntity: StringModelEntity){
        viewModelScope.launch {
            databaseRepository.delete(stringModelEntity)
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            databaseRepository.deleteAll()
        }
    }

    fun generateByLength(len: Int) {
        viewModelScope.launch {
            _randomStrings.value = UiState.Loading
            val value = repository.getByLength(len)
            if (value == null) {
                _randomStrings.value = UiState.Error("Unable to generate random string")
            } else {
                databaseRepository.insert(
                    stringModelEntity = StringModelEntity(
                        value = value.randomText?.value,
                        length = value.randomText?.length,
                        time = value.randomText?.created
                    )
                )
                _randomStrings.value = UiState.Success(value)
            }

            Log.d("Value", value.toString())

        }
    }


}
