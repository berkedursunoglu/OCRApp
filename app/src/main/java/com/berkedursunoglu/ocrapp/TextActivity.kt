package com.berkedursunoglu.ocrapp

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.berkedursunoglu.ocrapp.databinding.ActivityTextBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextActivity : AppCompatActivity() {

    lateinit var dataBinding:ActivityTextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_text)

        var uriString = intent.getStringExtra("Uri")
        var uri:Uri = Uri.parse(uriString)
        getStringFromPic(uri)
    }

    private fun getStringFromPic(uri: Uri) {
        var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        try{
            var inputImage = InputImage.fromFilePath(this,uri)
            var result = recognizer.process(inputImage).addOnSuccessListener {
                dataBinding.textView.text = it.text
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }catch (e:Exception){
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
}