package com.ahuaman.takepicturecompose

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.ahuaman.takepicturecompose.ui.theme.TakePictureComposeTheme
import com.ahuaman.takepicturecompose.utils.createImageFile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TakePictureComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContentApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ContentApp(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(context, "Image captured", Toast.LENGTH_SHORT).show()
            capturedImageUri = uri
        } else {
            capturedImageUri = Uri.EMPTY
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission is granted
            cameraLauncher.launch(uri)
        } else {
            // Permission is denied
        }
    }


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(onClick = {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }) {
            Text(text = "Take Picture")
        }


        //show image
        capturedImageUri?.let{
            Image(
                painter = rememberAsyncImagePainter(model = capturedImageUri),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
private fun PreviewContentApp() {
    TakePictureComposeTheme {
        ContentApp()
    }
}