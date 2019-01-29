package com.techapp.james.buybuygo.model.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.content.FileProvider
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object FileManager {
    fun createImageFileUri(fileName: String, context: Context): FileData {
        var file: File? = null
        val filePath = context.cacheDir.toString() + "/images/$fileName.jpg"
        Timber.d("file path " + filePath)
        file = File(filePath)
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir()
        }
        Timber.d("file path " + filePath + " filePath " + file.absolutePath)
        //share file to camera
var fileData=FileData(filePath,FileProvider.getUriForFile(context, "buybuygo.fileProvider", file!!))
        return fileData
    }

    fun compressQuality(uri: Uri, context: Context, fileName: String): Single<File> {

        return Single.create(object : SingleOnSubscribe<File> {
            override fun subscribe(emitter: SingleEmitter<File>) {
                val filePath = context.cacheDir.toString() + "/images/$fileName.jpg"
                var file = File(filePath)
                var imageStream = context.contentResolver.openInputStream(uri)
                var compress = 100
                var byteArray: ByteArray
                do {
                    var bmp = BitmapFactory.decodeStream(imageStream)
                    var baos: ByteArrayOutputStream? = ByteArrayOutputStream()
//compress Quality 100->50
                    compress -= 10
                    bmp.compress(Bitmap.CompressFormat.PNG, compress, baos)
                    byteArray = baos!!.toByteArray()
                    baos!!.close()
                    baos = null
                } while (byteArray.size > 2097152)
                emitter.onSuccess(writeFile(file, byteArray))
            }
        })
    }

    private fun writeFile(file: File, bytes: ByteArray): File {
        try {
            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.close()
            return file
        } catch (e: Exception) {
            Timber.d(e.message)
        }
        return file
    }
}