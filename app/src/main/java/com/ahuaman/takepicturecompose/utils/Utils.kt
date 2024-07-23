package com.ahuaman.takepicturecompose.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Create temp file on external cache directory

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_$timeStamp"
    // Use internal storage
    val storageDir = externalCacheDir
    val image = File(storageDir, "$imageFileName.jpg")
    return image
}

