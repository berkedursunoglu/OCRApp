package com.berkedursunoglu.ocrapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.berkedursunoglu.ocrapp.databinding.ActivityTextBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class TextActivity : AppCompatActivity() {

    lateinit var dataBinding:ActivityTextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_text)
        var bitmap : Bitmap? =null
        if (intent.hasExtra("image")){
            val byteArray = intent.getByteArrayExtra("image")
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            dataBinding.cropImageView.setImageBitmap(bitmap)
            getStringFromPic(bitmap)
        }
    }

    private fun getStringFromPic(bitmap: Bitmap) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        try{
            val inputImage = InputImage.fromBitmap(bitmap,0)
            recognizer.process(inputImage).addOnSuccessListener {
                dataBinding.progressBar.visibility = View.GONE
                dataBinding.textView.text = it.text
                dataBinding.textView.visibility = View.VISIBLE
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }catch (e:Exception){
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}