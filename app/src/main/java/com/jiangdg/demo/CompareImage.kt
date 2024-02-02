package com.jiangdg.demo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_show_last_saved.displayImage_green
import kotlinx.android.synthetic.main.activity_show_last_saved.displayImage_rgb
import kotlinx.android.synthetic.main.activity_show_last_saved.uploadImages
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class CompareImage : AppCompatActivity(){

    private var compress_bitmap_green: Bitmap? = null
    private var compress_bitmap_rgb:Bitmap? = null
    private var downloadUrl: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var rgb_firebaseImage_string: String? = null
    private var green_firebaseImage_string:String? = null
    private var mUploadTask: StorageTask<*>? = null
    private var mUploadTaskGreen:StorageTask<*>? = null
    private var mDatabaseRef_Info: DatabaseReference? = null
    private var last_greenImageUrl: String? = null
    private var last_rgbImageUrl:String? = null
    private var green_uri: Uri? = null
    private var rgb_uri:Uri? = null
    private val imageSize = 180

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_last_saved)
        mStorageRef = FirebaseStorage.getInstance().reference
        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference


        // get the URLs of the last stored RGB and green-filtered images from the last activity via Intent component
        val intent = intent
        try {
            last_greenImageUrl = Objects.requireNonNull<Bundle?>(intent.extras)
                .getString("LAST_GREEN_IMAGE_PATH") //to display last captured green image in imageView
            last_rgbImageUrl = Objects.requireNonNull<Bundle?>(intent.extras)
                .getString("LAST_RGB_IMAGE_PATH") //to display last captured green image in imageView
            green_firebaseImage_string =
                Objects.requireNonNull(intent.extras)!!.getString("green_firebaseImage_string")
            rgb_firebaseImage_string =
                Objects.requireNonNull(intent.extras)!!.getString("rgb_firebaseImage_string")
            if (last_greenImageUrl == null) {
                Toast.makeText(
                    this@CompareImage,
                    "Can't fetch green image url to display picture. Try again!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {  // show the green image in the imageView
                green_uri = Uri.parse(last_greenImageUrl)
                //if ever in future, the imageview is empty, most probably the problem is not in this activity
                //but the previous activity i.e., Label_Image.java. So, check its "All Images" folder and
                //check if the image url that you are trying to retrieve here, has originally been stored in the
                //"All Images" folder or not?
                displayImage_green.setImageURI(green_uri)
            }
            if (last_rgbImageUrl == null) {
                Toast.makeText(
                    this@CompareImage,
                    "Can't fetch the rgb image url to display picture. Try again!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {  // show the RGB image in the imageView
                rgb_uri = Uri.parse(last_rgbImageUrl)
                displayImage_rgb.setImageURI(rgb_uri)
                var image = MediaStore.Images.Media.getBitmap(this.contentResolver, rgb_uri)
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                image = image
                //                classifyImage(image);
            }
        } catch (e: java.lang.Exception) {
//            Toast.makeText(this@CompareImage, "Temporary Error!", Toast.LENGTH_SHORT).show()
        }
        //we click this button to upload image to cloud
        uploadImages.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@CompareImage, "Please Wait!", Toast.LENGTH_SHORT).show()
            uploadFileToCloud()
        })
    }

    private fun uploadFileToCloud() {
//            to upload saved rgb image to cloud
        try {
            compress_bitmap_green = (displayImage_green.getDrawable() as BitmapDrawable).bitmap
            compress_bitmap_rgb = (displayImage_rgb.getDrawable() as BitmapDrawable).bitmap
            val byteArrayOutputStream_green = ByteArrayOutputStream()
            val byteArrayOutputStream_rgb = ByteArrayOutputStream()
            compress_bitmap_green!!.compress(
                Bitmap.CompressFormat.JPEG,
                50,
                byteArrayOutputStream_green
            )
            compress_bitmap_rgb!!.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream_rgb)
            val thumb_byte_green = byteArrayOutputStream_green.toByteArray()
            val thumb_byte_rgb = byteArrayOutputStream_rgb.toByteArray()
            val fileReference_green_image: StorageReference =
                mStorageRef!!.child(green_firebaseImage_string + "greenImage" + ".null")
            val fileReference_rgb: StorageReference =
                mStorageRef!!.child(rgb_firebaseImage_string + "rgb" + ".null")
            mUploadTask = fileReference_rgb.putBytes(thumb_byte_rgb).addOnSuccessListener {
                runOnUiThread(Thread {
                    fileReference_rgb.downloadUrl.addOnSuccessListener { uri ->
                        downloadUrl = uri //url of real image
                        Toast.makeText(this@CompareImage, "Uploading!", Toast.LENGTH_SHORT)
                            .show()
                        //calling Upload class for storing entries database reference
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        mDatabaseRef_Info!!.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("Images")
                            .child(date).child(System.currentTimeMillis().toString())
                            .setValue(downloadUrl.toString(),
                                DatabaseReference.CompletionListener { error, ref ->
                                    if (error != null) {  // if there is some error while data is being uploaded, show the following toast
                                        Toast.makeText(
                                            this@CompareImage,
                                            "Data could not be saved! " + error.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@CompareImage,
                                            " written to database successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                    }
                })
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@CompareImage,
                    "Failed " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            mUploadTaskGreen =
                fileReference_green_image.putBytes(thumb_byte_green).addOnSuccessListener {
                    runOnUiThread(Thread {
                        fileReference_green_image.downloadUrl.addOnSuccessListener { uri ->
                            downloadUrl = uri //url of real image
                            //                                     Toast.makeText(uploadMedicalData.this, "DownlaodUrl"+uri, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this@CompareImage, "Uploading!", Toast.LENGTH_SHORT)
                                .show()
                            //calling Upload class for storing entries database reference
                            val today = Calendar.getInstance().time
                            val sdf = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            )
                            val date = sdf.format(today)
                            mDatabaseRef_Info!!.child("DoctorsList").child(Global.doctorUID)
                                .child("PatientList").child(Global.patientId).child("Images")
                                .child(date).child(System.currentTimeMillis().toString())
                                .setValue(downloadUrl.toString(),
                                    DatabaseReference.CompletionListener { error, ref ->
                                        if (error != null) {  // if there is some error while data is being uploaded, show the following toast
                                            Toast.makeText(
                                                this@CompareImage,
                                                "Data could not be saved! " + error.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@CompareImage,
                                                " written to database successfully!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                        }
                    })
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@CompareImage,
                        "Failed " + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}