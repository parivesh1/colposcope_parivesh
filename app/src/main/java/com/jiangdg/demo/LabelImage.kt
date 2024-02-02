package com.jiangdg.demo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_label__image.convertGreenImage
import kotlinx.android.synthetic.main.activity_label__image.fingerPaintView
import kotlinx.android.synthetic.main.activity_label__image.label1
import kotlinx.android.synthetic.main.activity_label__image.label2
import kotlinx.android.synthetic.main.activity_label__image.label3
import kotlinx.android.synthetic.main.activity_label__image.resetDrawing
import kotlinx.android.synthetic.main.activity_label__image.showLastSaved
import kotlinx.android.synthetic.main.activity_label__image.uploadImage
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Core.LUT
import org.opencv.core.CvType.CV_8UC1
import org.opencv.core.Mat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class LabelImage : AppCompatActivity(){
    private var mUploadTask: StorageTask<*>? = null
    private var downloadUrl: Uri? = null
    private var image: Bitmap? = null
    private var mDatabaseRef_Info: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    var paintDraw: Paint? = null
    private var imageUrl: String? = null
    private var uri: Uri? = null
    private val imageSize = 180
    private var greenImage = false
    private  var storeLabelledImage_original:String? = ""
    private  var storeLabelledImage_green:String? = ""
    private  var green_uri:Uri? = null
    private var label1_bool = false
    private  var label2_bool = false
    private  var label3_bool = false
    private var compress_bitmap: Bitmap? = null
    private var rgb_firebaseImage_string: String? = null
    private var green_firebaseImage_string:String? = null
    private var last_greenImagePath = ""
    private var last_rgbImagePath:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label__image)

        mStorageRef = FirebaseStorage.getInstance().reference
        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference


        //for free-hand drawing on imageView
        paintDraw = Paint()
        paintDraw!!.style = Paint.Style.FILL
        paintDraw!!.color = Color.RED
        paintDraw!!.strokeWidth = 10f

        val intent = intent

        try {
            imageUrl = Objects.requireNonNull<Bundle?>(intent.extras)
                .getString("CAPTURED_IMAGE_PATH") //to display captured image in imageView

            if (imageUrl == null) {
                Toast.makeText(
                    this@LabelImage,
                    "Can't fetch image url to display picture. Try again!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val file: File = File(imageUrl)
                uri = Uri.fromFile(file)
                last_rgbImagePath = imageUrl
                image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                try {
                    var image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
                    fingerPaintView.setImageURI(uri)
                } catch (e: Exception) {
                    Toast.makeText(this@LabelImage, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this@LabelImage, "Temporary Error!$e", Toast.LENGTH_SHORT).show()
        }


        // When "Convert Green Image" button is clicked, this function is executed
        // it saves the captured image in the 'Green_image' folder under "All Images" folder in phone
        convertGreenImage.setOnClickListener(View.OnClickListener {
            greenImage = true
//            storeLabelledImage_green = imageUrl.toString()+System.currentTimeMillis()+".jpg"
//            convertToGreen(imageUrl.toString())
            try {
                //converting imageView to bitmap
                //earlier I tried other ways like converting uri to bitmap but those didn't work so I thought of this
                fingerPaintView.setDrawingCacheEnabled(true) // so that I can extract bitmap later (and later convert the extracted bitmap to green image)
                val bmap: Bitmap = fingerPaintView.getDrawingCache()
                runOnUiThread {
                    try {
                        if (bmap != null) {
                            var myFile: File? = File(imageUrl)
                            var fos = FileOutputStream(myFile)
                            bmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            var bmp2 = bmap.copy(bmap.getConfig(), true);

                            var outputPath= imageUrl.toString()+System.currentTimeMillis()+".jpg"
                            Log.e("qwe", outputPath)
                            for (x in 0 until bmp2.width) {
                                for (y in 0 until bmp2.height) {
                                    val color = bmp2.getPixel(x, y)
                                    val red = Color.red(color)
                                    val green = Color.green(color)
                                    val blue = Color.blue(color)
                                    bmp2.setPixel(x, y, Color.rgb(0, green, 0))
                                }
                            }
                            val outputStream = FileOutputStream(outputPath)
                            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.close()


                            fingerPaintView.destroyDrawingCache() //just destroy cache after all necessary actions are performed for the sake of clarity
                            fingerPaintView.setDrawingCacheEnabled(false)
                            storeLabelledImage_green = outputPath
                            green_uri = Uri.parse(outputPath)
                            fingerPaintView.setImageURI(green_uri)






                            //these 4 lines of code converts RGB bitmap to green bitmap
//                            val mat = Mat()
//                            Utils.bitmapToMat(bmap, mat)
//                            val result: Mat = reduceColors(mat, 0, 50, 0)
//                            Utils.matToBitmap(result, bmap)
//                            myFile = File(storeLabelledImage_green)
//                            fos = FileOutputStream(myFile)
//                            bmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                            fos.close()
//                            Toast.makeText(
//                                this@LabelImage,
//                                "Image converted to green Image",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            fingerPaintView.destroyDrawingCache() //just destroy cache after all necessary actions are performed for the sake of clarity
//                            fingerPaintView.setDrawingCacheEnabled(false)
//                            green_uri = Uri.parse(storeLabelledImage_green)
//                            fingerPaintView.setImageURI(green_uri)
//                            last_rgbImagePath =
//                                storeLabelledImage_original //This will be used to display RGB image while pressing the "Show Last Saved" button
                            last_greenImagePath =
                                storeLabelledImage_green as String //This will be used to display green image while pressing the "Show Last Saved" button
//                            rgb_uri =
//                                Uri.parse(last_rgbImagePath) //This stores the RGB uri of "last_rgbImagePath"
//                            green_uri =
//                                Uri.parse(last_greenImagePath) //This stores the RGB uri of "last_greenImagePath"
//                            rgb_firebaseImage_string = sameImageName
//                            green_firebaseImage_string = sameImageName
                            // Finally after storing the images in the Stage folder, delete redundant files
                            //                                    File redundantFile = new File(imageUrl);
                            //                                    if(redundantFile.exists()) {
                            //                                        redundantFile.delete();
                            //                                    }
                        }
                    } catch (nfe: NumberFormatException) {
                    } catch (nfe: FileNotFoundException) {
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Toast.makeText(this@LabelImage, "some issue", Toast.LENGTH_SHORT).show()
            }
        })

        uploadImage.setOnClickListener(View.OnClickListener {
            uploadImage.setEnabled(false)
            Toast.makeText(this@LabelImage, "Please Wait!", Toast.LENGTH_SHORT).show()
            uploadFile()
        })


        //if we want to clear any free-hand drawing from imageView, we click this button
        resetDrawing.setOnClickListener(View.OnClickListener { //                drawClicked = false;
            fingerPaintView.clear()
            fingerPaintView.setImageURI(uri)
        })


        // When "Stage 1" button is clicked, this function is executed
        // it saves the captured image in the 'Stage 1' folder under "All Images" folder in phone
        label1.setOnClickListener(View.OnClickListener {










            if(! label1_bool && !label2_bool && !label3_bool ){
                label1_bool = true
                if (greenImage) {
                    fingerPaintView.setImageURI(uri)
                }

            }
            else if (label2_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (label3_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 3",
                    Toast.LENGTH_SHORT
                ).show()
            }


















//            label1_bool = true
//            if (greenImage) {
//                fingerPaintView.setImageURI(uri)
//            }
//            if (label1_bool && !label2_bool && !label3_bool) {
//
//            } else {
//                if (label2_bool) {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 2",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 3",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        })


        // When "Stage 2" button is clicked, this function is executed
        // it saves the captured image in the 'Stage 2' folder under "All Images" folder in phone
        label2.setOnClickListener(View.OnClickListener {






            if(! label1_bool && !label2_bool && !label3_bool ){
                label2_bool = true
                if (greenImage) {
                    fingerPaintView.setImageURI(uri)
                }

            }
            else if (label1_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 1",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (label3_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 3",
                    Toast.LENGTH_SHORT
                ).show()
            }












//            if (greenImage) {
//                fingerPaintView.setImageURI(uri)
//            }
//            if (!label1_bool && label2_bool && ! label3_bool){ //this condition ensures that each image is only labelled once
//            label2_bool = true
//
//            } else {
//                if (label1_bool) {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 1",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 3",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        })


        // When "Stage 3" button is clicked, this function is executed
        // it saves the captured image in the 'Stage 3' folder under "All Images" folder in phone
        label3.setOnClickListener(View.OnClickListener {








            if(! label1_bool && !label2_bool && !label3_bool ){
                label3_bool = true
                if (greenImage) {
                    fingerPaintView.setImageURI(uri)
                }

            }
            else if (label2_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (label1_bool){
                Toast.makeText(
                    this@LabelImage,
                    "This image has already been labeled as Stage 1",
                    Toast.LENGTH_SHORT
                ).show()
            }













//            if (greenImage) {
//                fingerPaintView.setImageURI(uri)
//            }
//            if (!label1_bool && !label2_bool && label3_bool) { //this condition ensures that each image is only labelled once
//            label3_bool = true
//            } else {
//                if (label1_bool) {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 1",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@LabelImage,
//                        "This image has already been labeled as Stage 2",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        })


        // Clicking on this 'showLastSaved' button shows us the saved RGB and green-filtered images
        showLastSaved.setOnClickListener(View.OnClickListener {
            if (last_greenImagePath!! == "" || last_rgbImagePath!! == "") {
                Toast.makeText(
                    this@LabelImage,
                    "No Image are available for compare!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this@LabelImage, CompareImage::class.java)
                intent.putExtra("LAST_RGB_IMAGE_PATH", last_rgbImagePath)
                intent.putExtra("LAST_GREEN_IMAGE_PATH", storeLabelledImage_green)
                intent.putExtra("green_firebaseImage_string", green_firebaseImage_string)
                intent.putExtra("rgb_firebaseImage_string", rgb_firebaseImage_string)
                startActivity(intent)
            }
        })

    }
    fun reduceColors(img: Mat?, numRed: Int, numGreen: Int, numBlue: Int): Mat {
        val redLUT: Mat = createLUT(numRed)
        val greenLUT: Mat = createLUT(numGreen)
        val blueLUT: Mat = createLUT(numBlue)
        val BGR: List<Mat> = ArrayList<Mat>(3)
        Core.split(img, BGR)
        LUT(BGR[0], blueLUT, BGR[0])
        LUT(BGR[1], greenLUT, BGR[1])
        LUT(BGR[2], redLUT, BGR[2])
        Core.merge(BGR, img)
        return img!!
    }

    fun createLUT(numColors: Int): Mat {
        // When numColors=1 the LUT will only have 1 color which is black.
        if (numColors < 0 || numColors > 256) {
            println("Invalid Number of Colors. It must be between 0 and 256 inclusive.")
            return null!!
        }
//        var lookUpTable: Mat = Mat.zeros(org.opencv.core.Size(1.0,256.0), CV_8UC1)
        val lookupTable: Mat = Mat.zeros(org.opencv.core.Size(1.0,256.0), CV_8UC1)
        var startIdx = 0
        var x = 0.0
        while (x < 256.0) {
            lookupTable.put(x.toInt(), 0, x)

            for (y in startIdx until x.toInt()) {
                if (lookupTable.get(y, 0)[0] == 0.0) {
                    lookupTable.put(y, 0, lookupTable.get(x.toInt(), 0)[y])
                }
            }
            startIdx = x.toInt()
            x += (256.0 / numColors)
        }
        return lookupTable
    }

    fun convertToGreen(imagePath:String){
        var outputPath= imageUrl.toString()+System.currentTimeMillis()+".jpg"
        for (x in 0 until image!!.width) {
            for (y in 0 until image!!.height) {
                val color = image!!.getPixel(x, y)
                val red = Color.red(color)
                val green = Color.green(color)
                val blue = Color.blue(color)
                image!!.setPixel(x, y, Color.rgb(0, green, 0))
            }
        }
        val outputStream = FileOutputStream(outputPath)
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
    }


    private fun uploadFile() {
        /** UPLOADING FIRST IMAGE TO CLOUD  */
        if (image != null) {  // if the first image's path is not empty, then execute the following
            try {
                compress_bitmap =
                    (fingerPaintView.getDrawable() as BitmapDrawable).bitmap // store the bitmap of this image in the variable 'compress_bitmap'
            } catch (e: java.lang.Exception) {  // if somehow bitmpa conversion fails, show the following message in the console
                // try-catch block prevents the application from crashing, in case the code fails to execute
                e.printStackTrace()
            }
            val byteArrayOutputStream =
                ByteArrayOutputStream() // 'byteArrayOutputStream' stores the bitmap of the image
            compress_bitmap!!.compress(
                Bitmap.CompressFormat.JPEG,
                50,
                byteArrayOutputStream
            ) // here the quality of the image is reduced to 50%. This is done to reduce the overall size of the Image (because database has limited space)
            val thumb_byte =
                byteArrayOutputStream.toByteArray() // here we are converting the bitmap to byteArray
            val fileReference = mStorageRef!!.child(
                System.currentTimeMillis().toString() + ".null"
            ) // the image file will be stored in the firebase Storage, where the name of the file will be System.currentTimeMillis().mime (eg- 16347873498.png)

            // The next command says that if the image file is being uploaded to the firebase storage location, listen to the changes
            mUploadTask = fileReference.putBytes(thumb_byte).addOnSuccessListener {
                // if the image file is uploaded to the firebase storage location successfully, call this method
                runOnUiThread(Thread {
                    fileReference.downloadUrl.addOnSuccessListener { uri ->

                        // copy the URL of the image stored in the storage reference of the firebase cloud
                        downloadUrl =
                            uri // if copying the image URI is successful, store that in the variable 'downloadUrl'
                        Toast.makeText(this@LabelImage, "Uploading !", Toast.LENGTH_SHORT)
                            .show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        val reference1 = FirebaseDatabase.getInstance().reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("Images")
                            .child(date)
                            .child(System.currentTimeMillis().toString())
                            .setValue(downloadUrl.toString())
                        Toast.makeText(this@LabelImage, "Uploaded !", Toast.LENGTH_SHORT)
                            .show()
                        Toast.makeText(
                            this@LabelImage,
                            " written to database successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }.addOnFailureListener { e ->
                // if somehow the upload task is not successful, listen for any failures
                // if the code fails, show the following toast notification
                Toast.makeText(this@LabelImage, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                uploadImage.isEnabled = true // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
            }
        }
    }
}