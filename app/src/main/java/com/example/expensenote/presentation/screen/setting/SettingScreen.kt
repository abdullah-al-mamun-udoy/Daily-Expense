package com.example.expensenote.presentation.screen.setting

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.expensenote.R
import com.example.expensenote.constant.Constant
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.util.CommonExtension
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.launch
import java.util.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navhost: NavHostController, viewmodel: SettingViewmodel = hiltViewModel()) {

    var name by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewmodel.setSelectedImageUri(uri.toString())
        })


    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var imageLoad by remember { mutableStateOf(false) }

    val isModalVisible by viewmodel.isModalVisible.collectAsStateWithLifecycle()

    var uploadAll by remember { mutableStateOf(false) }

    var showImagePickingCard by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current






    Box(modifier = Modifier.fillMaxSize()) {
        Column() {

            Row(
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "back_button", modifier = Modifier.size(24.dp)
                )
                Text(text = "Back", modifier = Modifier.padding(start = 4.dp), fontSize = 20.sp)
            }
            Row(
                modifier = Modifier.padding(start = 10.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dp),
                        contentDescription = "dp",
                        modifier = Modifier
                            .size(72.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Align the box to the bottom end (right bottom corner)
                            .padding(6.dp) // Add padding to create some space between the corner and the image
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "dp",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewmodel.showModalSheet()
                                }
                        )
                        if (isModalVisible) {
                            ModalBottomSheet(
                                sheetState = modalSheetState,
                                onDismissRequest = { viewmodel.hideModalSheet() }) {

                                Column(modifier = Modifier.padding(vertical = 0.dp)) {
                                    Row(
                                        modifier = Modifier.padding(start = 20.dp, top = 0.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dp),
                                            contentDescription = "dp",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "See profile picture",
                                            modifier = Modifier.padding(start = 4.dp),
                                            fontSize = 20.sp
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(
                                                start = 20.dp,
                                                top = 16.dp,
                                                bottom = 24.dp
                                            )
                                            .clickable {
//                                                imageLoad = true
//                                                viewmodel.hideModalSheet()
//                                                singlePhotoLauncher.launch(PickVisualMediaRequest())
                                                if (ContextCompat.checkSelfPermission(
                                                        context,
                                                        Manifest.permission.READ_EXTERNAL_STORAGE

                                                    )
                                                    != PackageManager.PERMISSION_GRANTED
                                                ) {

                                                    ActivityCompat.requestPermissions(
                                                        context as Activity,
                                                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                        Constant.REQUEST_READ_EXTERNAL_STORAGE
                                                    )
                                                } else {
                                                    // Permission has already been granted, proceed with accessing media content
                                                    // Launch the gallery intent here
                                                    imageLoad = true
                                                    viewmodel.hideModalSheet()
                                                    singlePhotoLauncher.launch(
                                                        PickVisualMediaRequest()
                                                    )
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_gallary),
                                            contentDescription = "dp",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Text(
                                            text = "Upload profile picture",
                                            modifier = Modifier.padding(start = 4.dp),
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }

                        }
                        if (imageLoad) {
                            showImagePickingCard = false
                            getAllGalleryImages(context)
                            Log.d("TAG", "gallary images number : ${getAllGalleryImages(context)}")
                        }
                    }
                }
                TextField(
                    value = name, // You can bind this to a variable if needed
                    onValueChange = { name = it },
                    placeholder = { Text("Write Your Name", fontSize = 20.sp) },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = appColor
                    )
                )
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email", modifier = Modifier.size(24.dp)
                )
                Text(text = "Email us", modifier = Modifier.padding(start = 4.dp), fontSize = 20.sp)
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "send", modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Crash Analytics and Report",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 12.dp)
                    .clickable {
                        coroutineScope.launch {
                            viewmodel.deleteAllExpenseItem()
                        }

                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = "clear", modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Clear Storage",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 20.sp
                )
            }

            Row {


                val imageUriList = getAllGalleryImages(context)
                val selectedImageUri: Uri? = if (imageUriList.isNotEmpty()) {
                    // Pick a random index
                    // Get the URI at the random index
                    imageUriList[0]
                } else {
                    null
                }
                if (selectedImageUri != null) {
                    val path = contentUriToFilePath(context, selectedImageUri)
                    if (path != null) {
                        val base64 = CommonExtension.imagePathToBase64(context, path)
                        Log.d("Tag", "base64 $base64")
                    }
                }


// Display the image using Image composable
                Image(
                    modifier = Modifier
                        .width(400.dp)
                        .height(250.dp)
                        .align(Alignment.CenterVertically),
                    painter = rememberImagePainter(
                        data = selectedImageUri,
                        builder = {
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = stringResource(id = R.string.app_name)
                )

                Log.d("Tag", "selectedImageUri $selectedImageUri")


            }

        }


    }
}


fun uploadImageToDrive(imageUri: Uri) {
    // Code for uploading image to Google Drive
}

// Function to upload all images from gallery to Google Drive
@Composable
fun uploadAllImagesToDrive() {
    val context = LocalContext.current
    val galleryImages = getAllGalleryImages(context)
    for (imageUri in galleryImages) {
        uploadImageToDrive(imageUri)
    }
}

// Helper function to get all images from gallery
//fun getAllGalleryImages(): List<Uri> {
//    val imageList = mutableListOf<Uri>()
//    val projection = arrayOf(MediaStore.Images.Media._ID)
//    val selection = "${MediaStore.Images.Media.MIME_TYPE} = ?"
//    val selectionArgs = arrayOf("image/jpeg", "image/png") // Add more mime types if needed
//    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
//    val query = context.contentResolver.query(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        projection,
//        selection,
//        selectionArgs,
//        sortOrder
//    )
//    query?.use { cursor ->
//        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//        while (cursor.moveToNext()) {
//            val id = cursor.getLong(idColumn)
//            val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//            imageList.add(contentUri)
//        }
//    }
//    return imageList
//}


//fun getAllGalleryImages(context: Context): List<Uri> {
//    val imageList = mutableListOf<Uri>()
//    val projection = arrayOf(MediaStore.Images.Media._ID)
//    val selection = "${MediaStore.Images.Media.MIME_TYPE} = ?"
//    val selectionArgs = arrayOf("image/jpeg", "image/png") // Add more mime types if needed
//    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
//    val query = context.contentResolver.query(
//        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//        projection,
//        selection,
//        selectionArgs,
//        sortOrder
//    )
//    query?.use { cursor ->
//        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//        while (cursor.moveToNext()) {
//            val id = cursor.getLong(idColumn)
//            val contentUri =
//                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//            imageList.add(contentUri)
//        }
//    }
//    return imageList
//}
//


fun getAllGalleryImages(context: Context): List<Uri> {
    val imageList = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val selectionArgs = buildString {
        append("(")
        append("${MediaStore.Images.Media.MIME_TYPE} = ?")
        append(" OR ")
        append("${MediaStore.Images.Media.MIME_TYPE} = ?")
        // Add more MIME types if needed
        append(")")
    }
    val selectionArgsArray = arrayOf("image/jpeg", "image/png") // Add more mime types if needed
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
    val query = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selectionArgs,
        selectionArgsArray,
        sortOrder
    )
    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageList.add(contentUri)
        }
    }
    return imageList
}


@Composable
fun ViewImage(imageUriList: List<String>) {
    // Generate a random index to select an image from the list
    val random = Random()
    val randomIndex = if (imageUriList.isNotEmpty()) random.nextInt(imageUriList.size) else -1
    val randomImageUri = if (randomIndex != -1) imageUriList[randomIndex] else null

    // Create a Painter for the selected image URI
    val painter: Painter = rememberCoilPainter(request = randomImageUri)

    // Show the image using Image composable
    Image(
        painter = painter,
        contentDescription = "Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
    )
}

fun contentUriToFilePath(context: Context, contentUri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(contentUri, projection, null, null, null)
    val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    val filePath = columnIndex?.let { cursor.getString(it) }
    cursor?.close()
    return filePath
}