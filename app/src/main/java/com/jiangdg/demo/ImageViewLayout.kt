package com.jiangdg.demo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.doctor_details.image_view
import kotlinx.android.synthetic.main.image_view.displayImage
import java.io.File
import java.net.URL
import java.util.Objects


class ImageViewLayout : AppCompatActivity(){
    var imageUri: String? = null
    var imagePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_view)
        val intent = intent
        Log.e("aaaa",imageUri.toString())



        imageUri = (Objects.requireNonNull(intent.extras)!!.getString("imgUri"))
        imagePath = Objects.requireNonNull(intent.extras)!!.getString("imagePath")
        if (imageUri!! != "null") {
            Picasso.get().load(imageUri!!).into(displayImage)
        }
        if (imagePath!! != "null") {
            Log.e("zzzzz", imagePath!!.isNotEmpty().toString())

            val file = File(imagePath)
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
            displayImage.setImageBitmap(myBitmap)
        }
    }
}