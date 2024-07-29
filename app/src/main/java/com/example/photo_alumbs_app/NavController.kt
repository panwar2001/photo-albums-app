package com.example.photo_alumbs_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable

/**
 * Composable that has navigation host and graph for navigating among different composable screens.
 */
@Composable
fun NavigationController(navController: NavHostController= rememberNavController(),
                         viewModel:AppViewModel= hiltViewModel<AppViewModel>()) {
        NavHost(navController=navController, startDestination = Screens.Home.route){

             composable(route= Screens.Home.route){
                 val albums by viewModel.albums.collectAsState(initial = listOf())
                 HomeScreen(albums = albums,
                    createNewAlbum = viewModel::createNewAlbum,
                     onAlbumClick={
                             viewModel.setCurrentAlbum(it)
                             navController.navigate(Screens.Album.route)
                     },
                     deleteAlbum = viewModel::deleteAlbum)
            }
            composable(route= Screens.Album.route){
                val uiState by viewModel.uiState.collectAsState()
                AlbumScreen(images = uiState.images) {
                    navController.navigateUp()
                }
            }
        }
}

