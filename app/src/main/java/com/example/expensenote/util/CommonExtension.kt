package com.example.expensenote.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream

object CommonExtension {

    fun imagePathToBase64(ctx: Context, imagePath: String): String {
        val file = File(imagePath)
        val inputStream = FileInputStream(file)
        val bytes = inputStream.readBytes()
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
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
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        contentUris.forEach { contentUri ->
            val cursor = context.contentResolver.query(contentUri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            val filePath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()
            filePaths.add(filePath)
        }

        return filePaths
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

        directory.listFiles()?.forEach { file ->
            if (file.isFile && (file.extension.equals("jpg", ignoreCase = true) || file.extension.equals("png", ignoreCase = true))) {
                val contentUri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                imageList.add(contentUri)
            }
        }

        return imageList
    }






}