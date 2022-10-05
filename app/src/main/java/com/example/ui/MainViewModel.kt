package com.example.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixabayapps.model.SearchResponse
import com.example.data.network.services.PixabayApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val apiservice : PixabayApiService by lazy {
        PixabayApiService.invoke()
    }
    val searhResult = MutableLiveData<SearchResponse>()
    val loadingState = MutableLiveData<Boolean>()
    val errorstate = MutableLiveData<Pair<Boolean,Exception?>>()

    fun searchPost(query: String) {
        loadingState.postValue(true)
        errorstate.postValue(Pair(false,null))
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = apiservice.searchphoto(query = query)
                viewModelScope.launch(Dispatchers.Main){
                    searhResult.postValue(data)
                    loadingState.postValue(false)
                    errorstate.postValue(Pair(false,null))
                }
            } catch (e: Exception) {
                loadingState.postValue(false)
                errorstate.postValue(Pair(true,e))
            }
        }

    }
}