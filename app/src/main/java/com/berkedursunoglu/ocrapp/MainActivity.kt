package com.berkedursunoglu.ocrapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.decodeBitmap
import androidx.databinding.DataBindingUtil
import com.berkedursunoglu.ocrapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    var uriImage: Uri? = null
    private lateinit var dataBinding:ActivityMainBinding
    lateinit var bitmap:Bitmap
    var uriString = "";

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        dataBinding.mainActivity = this
        dataBinding.uploadButton.setOnClickListener {
            permissionReadExternal(it)
        }
        dataBinding.copyText.setOnClickListener {
            var intent = Intent(this,TextActivity::class.java)
            intent.putExtra("Uri",uriString)
            startActivity(intent)
            Log.d("Uri", uriString)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun permissionReadExternal(view: View) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for galery!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", object :
                    View.OnClickListener{
                    override fun onClick(p0: View?) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
                    }
                }).show()
            }else{
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,102)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 101){
            if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,102)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 102 && resultCode == Activity.RESULT_OK && data != null){
            uriImage = data.data
            uriString = uriImage.toString()
            try{
                if (Build.VERSION.SDK_INT >= 28){
                    var imageDecoder:ImageDecoder.Source = ImageDecoder.createSource(this@MainActivity.contentResolver,uriImage!!)
                    bitmap = ImageDecoder.decodeBitmap(imageDecoder)
                    dataBinding.imageView.setImageBitmap(bitmap)
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(this@MainActivity.contentResolver,uriImage)
                    dataBinding.imageView.setImageBitmap(bitmap)
                }

                dataBinding.copyText.visibility = View.VISIBLE
            }catch (e:Exception){
                Toast.makeText(this@MainActivity,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}