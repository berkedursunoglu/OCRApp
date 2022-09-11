package com.berkedursunoglu.ocrapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class CropImage(var imageBitmap: Bitmap,var x1:Int,var x2:Int,var y1:Int,var y2:Int) {

    private lateinit var mutableBitmap:Bitmap

    fun updateBitmap():Bitmap{
        mutableBitmap()
        detectLine(-10)
        mutableBitmap = cropBitmap()
        return mutableBitmap
    }

    fun cropBitmap(): Bitmap {
        return Bitmap.createBitmap(
            mutableBitmap, x1.toInt(),
            y1.toInt(),
            x2.toInt() - x1.toInt(),
            y2.toInt() - y1.toInt())
    }

    fun mutableBitmap(){
        var bitmap = Bitmap.createBitmap(imageBitmap)
        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
    }

    fun detectLine(margen: Int) {
        var x1_2 = x1 - margen
        var x2_2 = x2 + margen
        var y1_2 = y1 - margen
        var y2_2 = y2 + margen
        val line = lineOptions()
        val canvas = Canvas(mutableBitmap)
        canvas.drawLine(x1_2.toFloat(), y1_2.toFloat(), x2_2.toFloat(), y1_2.toFloat(), line) //up
        canvas.drawLine(x1_2.toFloat(), y1_2.toFloat(), x1_2.toFloat(), y2_2.toFloat(), line) //left
        canvas.drawLine(x1_2.toFloat(), y2_2.toFloat(), x2_2.toFloat(), y2_2.toFloat(), line) //down
        canvas.drawLine(x2_2.toFloat(), y1_2.toFloat(), x2_2.toFloat(), y2_2.toFloat(), line)
    }

    private fun lineOptions() : Paint {
        return Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
            color = Color.RED
        }
    }
}