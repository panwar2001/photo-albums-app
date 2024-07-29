package com.example.photo_alumbs_app

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(images: List<Bitmap>,goBack:()->Unit){
    Scaffold(topBar = {
      TopAppBar(title = { Text(text = "Album images")},
          navigationIcon = {
              IconButton(onClick = goBack) {
                  Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
              }
          })
    }){padding->
        LazyVerticalGrid(
            modifier = Modifier.padding(padding).fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(images) { item ->
                    AsyncImage(model = item,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp).fillMaxWidth().wrapContentHeight().padding(2.dp))
            }
        }
    }
}