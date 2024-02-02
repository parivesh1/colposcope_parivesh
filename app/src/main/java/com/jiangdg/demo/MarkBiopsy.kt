package com.jiangdg.demo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_mark_biopsy_place.imageView
import kotlinx.android.synthetic.main.activity_mark_biopsy_place.save
import kotlinx.android.synthetic.main.activity_mark_biopsy_place.save2
import kotlinx.android.synthetic.main.activity_mark_biopsy_place.toolbar
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MarkBiopsy : AppCompatActivity(){
    private var downloadUrl: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef_Info: DatabaseReference? = null
    private  var mDatabaseRef_ImageData:DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null
    private var compress_bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_biopsy_place)
        toolbar.title="Biopsy Marking"

        mStorageRef = FirebaseStorage.getInstance().reference
        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference
        mDatabaseRef_ImageData = FirebaseDatabase.getInstance().getReference("MarkersList")

        save.setOnClickListener(View.OnClickListener {
            save.setEnabled(false)
            Toast.makeText(this@MarkBiopsy, "Please Wait!", Toast.LENGTH_SHORT).show()
            uploadFile()
        })

        save2.setOnClickListener(View.OnClickListener { imageView.clear() })

    }

    private fun uploadFile() {
        /** UPLOADING FIRST IMAGE TO CLOUD  */
        if (imageView != null) {  // if the first image's path is not empty, then execute the following
            try {
                compress_bitmap =
                    (imageView.getDrawable() as BitmapDrawable).bitmap // store the bitmap of this image in the variable 'compress_bitmap'
            } catch (e: Exception) {  // if somehow bitmpa conversion fails, show the following message in the console
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
                        Toast.makeText(this@MarkBiopsy, "Uploading !", Toast.LENGTH_SHORT)
                            .show()
                        val today = Calendar.getInstance().time
                        val sdf = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        )
                        val date = sdf.format(today)
                        val reference1 = FirebaseDatabase.getInstance().reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("Biopsy")
                            .child(date)
                            .child(System.currentTimeMillis().toString())
                            .setValue(downloadUrl.toString())
                        Toast.makeText(this@MarkBiopsy, "Uploaded !", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@MarkBiopsy, ReportSelection::class.java)
                        startActivity(intent)
                    }
                })
            }.addOnFailureListener { e ->

                // if somehow the upload task is not successful, listen for any failures
                // if the code fails, show the following toast notification
                Toast.makeText(this@MarkBiopsy, "Failed " + e.message, Toast.LENGTH_SHORT)
                    .show()
                save.setEnabled(true) // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
            }
        }
    }
}