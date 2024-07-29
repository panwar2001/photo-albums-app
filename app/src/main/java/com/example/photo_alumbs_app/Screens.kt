package com.example.photo_alumbs_app

sealed class Screens(val route : String) {
    data object Home : Screens("home")
    data object Album : Screens("album")
}