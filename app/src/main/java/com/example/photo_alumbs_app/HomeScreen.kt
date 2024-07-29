package com.example.photo_alumbs_app

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

data class Album(
    val date:Long=0,
    val thumbnail: Bitmap,
    val id: String,
    val albumName:String,
    )
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(albums:List<Album>,
               scanner: GmsDocumentScanner= rememberDocumentScanner(),
               createNewAlbum: (List<Uri>)->Unit,
               onAlbumClick:(String)->Unit,
               deleteAlbum:(String)->Unit) {
    val activity= LocalContext.current as Activity
    val scannerLauncher = rememberLauncherForActivityResult(
        contract =ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val scanningResult =
                GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            scanningResult?.pages?.let { pages ->
                createNewAlbum(
                    pages.map {it.imageUri}
                )
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scanner.getStartScanIntent(activity)
                    .addOnSuccessListener {
                        scannerLauncher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }.addOnFailureListener{
                        Log.d("TAG", "HomeScreen: ${it.message}")
                    }
            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_camera_24), contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(title = { Text(text = "Album App")},
                navigationIcon = {
                        Icon(imageVector = Icons.Outlined.Home, contentDescription = null, modifier = Modifier.size(30.dp))
                })
        }
    ){padding->
        LazyVerticalGrid(
            modifier = Modifier.padding(padding),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(albums.sortedByDescending { it.date }) { item ->
                ElevatedCard(onClick = {onAlbumClick(item.id)},
                        modifier = Modifier.padding(8.dp),
                ){
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                        IconButton(onClick = {deleteAlbum(item.id)}) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, tint = Color.Red,
                                modifier = Modifier.size(30.dp))
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(5.dp)
                    ){
                        AsyncImage(model = item.thumbnail,
                            contentDescription =null,
                            contentScale = ContentScale.Crop,
                            modifier= Modifier
                                .shadow(elevation = 5.dp)
                                .size(200.dp))
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(text = item.albumName,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }

    }
}
@Preview
@Composable
fun PreviewScreen(){
//   HomeScreen()
}


@Composable
fun rememberDocumentScanner(): GmsDocumentScanner {
    return remember {
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setResultFormats(RESULT_FORMAT_JPEG)
                .setScannerMode(SCANNER_MODE_FULL)
                .build())
    }
}