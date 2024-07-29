package com.example.photo_alumbs_app

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photo_alumbs_app.data.AppInterface
import com.example.photo_alumbs_app.data.AppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
data class UiState(
    val images: List<Bitmap> = listOf(),
    val currentAlbumId:String?=null,
    )

@HiltViewModel
class AppViewModel @Inject constructor(private val appRepo:AppInterface): ViewModel(){
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val albums = appRepo.getAllAlbums()

    fun setCurrentAlbum(id:String){
        _uiState.update {
            it.copy(images = appRepo.getAllImages(id))
        }
    }
    fun createNewAlbum(uris:List<Uri>){
        viewModelScope.launch {
            appRepo.createAlbum(uris)
        }
    }
    fun deleteAlbum(id:String){
        viewModelScope.launch {
            appRepo.delete(id)
        }
    }
}