package com.example.converterjpgtopng.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import io.reactivex.rxjava3.core.Single
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/**
 * Класс Конвертирует файл изображения из jpg в png
 *
 */

class ConverterFromJpgToPng(val context: Context) {
    fun convertRx(uri: Uri?): Single<Uri> {
        uri?.let {
            val tempConvertedFile = File.createTempFile("tmpConvert", ".png", )


            val fileOutputStream = FileOutputStream(tempConvertedFile)
            val bufferOutputStream = BufferedOutputStream(fileOutputStream)
            val imageDecoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
            val bitmap = Bitmap.CompressFormat.PNG

            imageDecoder.compress(bitmap, 100, bufferOutputStream)
            bufferOutputStream.close()
            fileOutputStream.close()


val rxJavaSingleSource = Single.just(tempConvertedFile.toUri())
    .delay(3000L, TimeUnit.MILLISECONDS)
            return rxJavaSingleSource
        }
        return Single.error(Throwable())
    }
}