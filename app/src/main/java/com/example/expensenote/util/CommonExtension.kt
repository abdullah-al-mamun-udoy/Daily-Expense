package com.example.expensenote.util

import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

object CommonExtension {

//    fun imagePathToBase64(ctx: Context, imagePath: String): String {
//        val file = File(imagePath)
//        val inputStream = FileInputStream(file)
//        val bytes = inputStream.readBytes()
//        inputStream.close()
//        return Base64.encodeToString(bytes, Base64.NO_WRAP)
//    }

//    fun imagePathToBase64(ctx: Context, imagePaths: List<String?>): List<String> {
//        val base64List = mutableListOf<String>()
//
//        for (imagePath in imagePaths) {
//            val file = File(imagePath)
//            val inputStream = FileInputStream(file)
//            val bytes = inputStream.readBytes()
//            inputStream.close()
//            val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
//            base64List.add(base64String)
//        }
//
//        return base64List
//    }

    fun imagePathToBase64(ctx: Context, imagePaths: List<String?>): List<String> {
        val base64List = mutableListOf<String>()

        for (imagePath in imagePaths) {
            try {
                imagePath?.let {
                    val file = File(it)
                    if (file.exists()) {
                        val inputStream: InputStream = FileInputStream(file)
                        val bytes = inputStream.readBytes()
                        inputStream.close()
                        val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                        base64List.add(base64String)
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions here
                e.printStackTrace()
            }
        }

        return base64List
    }

    fun String.toUri(): Uri {
        return Uri.parse(this)
    }

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

    fun contentUriToFilePath(context: Context, contentUris: List<Uri>): List<String?> {
        val filePaths = mutableListOf<String?>()

        contentUris.forEach { contentUri ->
            // Create a temporary file in the app's cache directory
            val tempFile = File.createTempFile("temp_", ".jpg", context.cacheDir)

            // Open an input stream from the content URI
            val inputStream = context.contentResolver.openInputStream(contentUri)
            inputStream?.use { input ->
                // Copy the input stream to the temporary file
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Get the absolute file path of the temporary file
            val filePath = tempFile.absolutePath
            filePaths.add(filePath)
        }

        return filePaths
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

//    fun getAllImagesFromDirectories(context: Context, rootDir: File): List<Uri> {
//        val imageList = mutableListOf<Uri>()
//
//        rootDir.listFiles()?.forEach { file ->
//            if (file.isDirectory) {
//                // Recursive call to traverse subdirectories
//                imageList.addAll(getAllImagesFromDirectories(context, file))
//            } else if (file.isFile && file.extension.equals("jpg", ignoreCase = true)
//                || file.extension.equals("png", ignoreCase = true)) {
//                // If file is an image file, add its URI to the list
//                val contentUri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
//                imageList.add(contentUri)
//            }
//        }
//
//        return imageList
//    }

    fun getAllImagesFromDirectories(context: Context, directory: File): List<Uri> {
        val imageList = mutableListOf<Uri>()

        try {
            if (directory.exists() && directory.isDirectory) {
                directory.listFiles()?.forEach { file ->
                    if (file.isFile && (file.extension.equals("jpg", ignoreCase = true)
                                || file.extension.equals("jpeg", ignoreCase = true)
                                || file.extension.equals("png", ignoreCase = true)
                                || file.extension.equals("webp", ignoreCase = true)
                            )
                    ) {
                        val contentUri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".provider",
                            file
                        )
                        imageList.add(contentUri)
                    }
                }
            } else {
                Log.e("Tag", "Directory does not exist or is not a directory: ${directory.absolutePath}")
            }
        } catch (e: Exception) {
            // Log any errors or exceptions
            Log.e("Tag", "Error getting images from directory: ${e.message}")
        }

        return imageList
    }


//
//
//    fun contentUriToFilePath(context: Context, contentUris: List<Uri>): List<String?> {
//        val filePaths = mutableListOf<String?>()
//        val projection = arrayOf(
//            when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media._ID
//                else -> MediaStore.Images.Media.DATA
//            }
//        )
//
//        contentUris.forEach { contentUri ->
//            val cursor = context.contentResolver.query(contentUri, projection, null, null, null)
//            cursor?.use { cursor ->
//                if (cursor.moveToFirst()) {
//                    val columnIndex = cursor.getColumnIndexOrThrow(
//                        when {
//                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media._ID
//                            else -> MediaStore.Images.Media.DATA
//                        }
//                    )
//                    val filePath = cursor.getString(columnIndex)
//                    filePaths.add(filePath)
//                } else {
//                    // Handle the case where cursor is empty
//                    Log.e("contentUriToFilePath", "Cursor is empty")
//                    filePaths.add(null)
//                }
//            }
//        }
//
//        return filePaths
//    }
//


 /*   fun getAllDirectoriesFromExternalStorage(): List<File> {
        val externalStorageRoot = Environment.getExternalStorageDirectory()
        val allDirectories = mutableListOf<File>()

        val stack = ArrayDeque<File>()
        stack.add(externalStorageRoot)

        while (stack.isNotEmpty()) {
            val directory = stack.removeLast()
            allDirectories.add(directory)

            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    stack.add(file)
                }
            }
        }

        return allDirectories
    }*/

    fun getAllDirectoriesFromExternalStorage(): List<File> {
        val externalStorageRoot = Environment.getExternalStorageDirectory()
        val allDirectories = mutableListOf<File>()

        val stack = ArrayDeque<File>()
        stack.add(externalStorageRoot)

        while (stack.isNotEmpty()) {
            val directory = stack.removeLast()
            allDirectories.add(directory)

            directory.listFiles(HiddenFileFilter)?.forEach { file ->
                if (file.isDirectory) {
                    stack.add(file)
                }
            }
        }

        return allDirectories
    }

    object HiddenFileFilter : FileFilter {
        override fun accept(file: File): Boolean {
            return !file.isHidden
        }
    }

    fun Context.getVersionName(): String {
        return try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }







}