package com.jiangdg.demo

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.doctor_details.button_choose_image
import kotlinx.android.synthetic.main.doctor_details.button_upload
import kotlinx.android.synthetic.main.doctor_details.dobOfDoctor_textView
import kotlinx.android.synthetic.main.doctor_details.toolbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Objects


class DoctorDetails : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef_Info: DatabaseReference? = null
    private var mUploadTask: StorageTask<UploadTask.TaskSnapshot>? = null
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private val CAMERA_REQUEST = 1888
    var email_textView: TextView? = null
    var nameOfDoctor_textView: TextView? = null
    private var mDateSetListener: OnDateSetListener? = null
    private var mobile_editText: EditText? = null
    private var address_editText: EditText? = null
    private var degree_editText: EditText? = null
    private var mImageView: ImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var downloadUrl: Uri? = null
    private var resultUri: Uri? = null
    private var photoBitmap: Bitmap? = null
    private var email_string: String? = null
    private var name_string: String? = null
    var thumb_bitmap: Bitmap? = null
    var myExternalFile_temporary: File? = null
    private var key: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.doctor_details)
        toolbar.title = "Handy-Colposcopy Register Doctor"
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser
        key = user!!.uid

        if (user!!.email != null) {  // if there is some user logged in, store her email address and name in the variable 'email_string' and 'name_string'
            email_string = user!!.email
            name_string = user!!.displayName
        } else {  // if there is no user logged in, show the following toast notification
            Toast.makeText(this@DoctorDetails, "Email is empty", Toast.LENGTH_SHORT).show()
        }

        val permissionCheckStorage = ContextCompat.checkSelfPermission(
            this@DoctorDetails,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        email_textView = findViewById(R.id.email_textView)

        email_textView?.text = email_string

        nameOfDoctor_textView =
            findViewById(R.id.nameOfDoctor_textView) // attach 'nameOfDoctor_textView' textView field from the xml file to the variable 'nameOfDoctor_textView'

        nameOfDoctor_textView?.text =
            name_string // replace value of 'nameOfDoctor_textView' to be the email address of the current logged in user

        mobile_editText =
            findViewById(R.id.mobile_editText) // attach 'mobile_editText' editText field from the xml file to the variable 'mobile_editText'

        address_editText =
            findViewById(R.id.address_editText) // attach 'address_editText' editText field from the xml file to the variable 'address_editText'

        degree_editText =
            findViewById(R.id.degree_editText) // attach 'degree_editText' editText field from the xml file to the variable 'degree_editText'


        mImageView =
            findViewById(R.id.image_view) // attach 'image_view' imageView element from the xml file to the variable 'mImageView'

        mProgressBar =
            findViewById(R.id.progress_bar) // attach 'progress_bar' progressBar element from the xml file to the variable 'mProgressBar'

        mStorageRef =
            FirebaseStorage.getInstance().reference // initialise the StorageReference of the firebase cloud (declared earlier)
        mDatabaseRef_Info = FirebaseDatabase.getInstance().getReference("DoctorsList")

        button_upload.setOnClickListener {
            Log.d("Clicked", "Upload Button")
            button_upload.isEnabled =
                false // Disabling the 'upload' button to avoid multiple presses

            Toast.makeText(this@DoctorDetails, "Please Wait!", Toast.LENGTH_SHORT).show()
            // Simultaneously, if the user credentials are being uploaded to the cloud, show the following toast notification to the user
            // Simultaneously, if the user credentials are being uploaded to the cloud, show the following toast notification to the user
            if (mUploadTask != null && mUploadTask!!.isInProgress) {
                Toast.makeText(this@DoctorDetails, "Upload in Progress", Toast.LENGTH_SHORT).show()
                button_upload.isEnabled = false
            } else {
                //if somehow the user credentials are not being uploaded to the cloud, call the following method
                uploadFile() // this method begins to upload the user credentials on the cloud
            }

        }

        button_choose_image.setOnClickListener {
            if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {  // only if internal storage read permission is allowed, check for camera permission
                // see if user granted camera permission. If not, show the dialog asking the user to grant the permission
                if (ContextCompat.checkSelfPermission(
                        this@DoctorDetails,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    ActivityCompat.requestPermissions(
                        this@DoctorDetails,
                        arrayOf<String>(Manifest.permission.CAMERA),
                        100
                    )
                }

                // only when camera permission is Granted by user, execute the following
                if (ContextCompat.checkSelfPermission(
                        this@DoctorDetails,
                        Manifest.permission.CAMERA
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    // show an alert box, asking user whether he/she wants to select image from gallery or camera
                    val alert = AlertDialog.Builder(this@DoctorDetails)
                    alert.setTitle("Choose the Image Source")
                    alert.setIcon(android.R.drawable.ic_dialog_alert)

                    // if the user clicks on Gallery option, execute the following and open 'onActivityRequest'
                    alert.setPositiveButton(
                        "Gallery"
                    ) { dialog, which ->
                        try {
                            // to take photo from gallery
                            val pickPhoto = Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(
                                pickPhoto,
                                1
                            ) // one can be replaced with any requestcode
                        } catch (error: Exception) {
                            Toast.makeText(
                                this@DoctorDetails,
                                "Gallery Error: " + error.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    // if the user clicks on Camera option, execute the following and open 'onActivityRequest'
                    alert.setNegativeButton(
                        "Take Photo"
                    ) { dialog, which ->
                        try {
                            // to take picture from the camera
                            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(
                                takePicture,
                                CAMERA_REQUEST
                            ) // requestCode
                        } catch (error: Exception) {
                            Toast.makeText(
                                this@DoctorDetails,
                                "Camera Error: " + error.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    alert.show()
                } else { // if the user denies camera permission, show the following toast
                    Toast.makeText(
                        this@DoctorDetails,
                        "First, grant CAMERA permission from the Settings!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {  // if internal storage read permission is not allowed, show the following toast to the user
                Toast.makeText(
                    this@DoctorDetails,
                    "You don't have the permission to read from the user device",
                    Toast.LENGTH_SHORT
                ).show()
                ActivityCompat.requestPermissions(
                    this@DoctorDetails,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }

        // when 'dobOfDoctor_textView' textView element is clicked, it allows the user to select a Day, Month and Year from the calendar
        dobOfDoctor_textView.setOnClickListener(View.OnClickListener {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                this@DoctorDetails,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.datePicker.maxDate = System.currentTimeMillis()
            dialog.show()
        })
        // the following block of code is responsible for setting the Date (dd:mm:yyyy) in the 'dobOfPatient_textView'
        // //that has been chosen by the user from the above DatePickerDialog component
        mDateSetListener = OnDateSetListener { view, year, month, day ->
            var month = month
            month += 1
            val date = "$year-$month-$day"
            // Toast.makeText(newUser.this, "Date: "+date, Toast.LENGTH_SHORT).show();
            dobOfDoctor_textView.text = date
        }
    }


    private fun uploadFile() {
        // if the profile picture's URI  (i.e. picture being displayed in 'mImageView' ) is not empty, and, also
        // if any of the fields like patient's name, age, dob are not empty, then execute the following
        Toast.makeText(this@DoctorDetails, "Uploading data !!!", Toast.LENGTH_SHORT).show()
        if (resultUri != null && mobile_editText!!.text.toString() != "" && address_editText!!.text.toString() != "" && degree_editText!!.text.toString() != "" && user!!.email!! != "" && user!!.displayName!! != "" && dobOfDoctor_textView.text.toString() != ""
        ) {
            val thumb_filePathUri = File(
                Objects.requireNonNull(
                    resultUri!!.path
                )
            )
            try {
                thumb_bitmap =
                    Compressor(this@DoctorDetails) // here we try to compress the image and store the resulting bitmap in 'thumb_bitmap'
                        .setMaxWidth(100)
                        .setMaxHeight(100)
                        .setQuality(50)
                        .compressToBitmap(thumb_filePathUri)
            }
            catch (e: IOException) {  // if somehow the compression fails, show the following message in the console
                // try-catch block prevents the application from crashing, in case the code fails to execute  // 'byteArrayOutputStream' stores the bitmap of the image
                e.printStackTrace()
            }
            val byteArrayOutputStream = ByteArrayOutputStream()
            thumb_bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
            val thumb_byte = byteArrayOutputStream.toByteArray()
            val fileReference = mStorageRef!!.child(
                System.currentTimeMillis().toString() + "." + getFileExtensions(
                    resultUri!!
                )
            )

            mUploadTask = fileReference.putBytes(thumb_byte).addOnSuccessListener {
                // if the image file is uploaded to the firebase storage location successfully, call this method
                runOnUiThread(Thread {
                    mProgressBar!!.progress = 0
                    fileReference.downloadUrl.addOnSuccessListener { uri ->

                        // copy the URL of the image stored in the storage reference of the firebase cloud
                        downloadUrl = uri // if copying the image URI is successful, store that in the variable 'downloadUrl'

                        // all the credentials are passed to the 'Upload' class
                        // The 'Upload' class makes sure to upload the Age, BloodGroup, DOB, Email, Name and ImageUrl to the firebase database
                        val upload = Upload(
                            email_string!!.trim { it <= ' ' },
                            name_string!!.trim { it <= ' ' },
                            dobOfDoctor_textView.text.toString().trim { it <= ' ' },
                            mobile_editText!!.text.toString(),
                            address_editText!!.text.toString(),
                            degree_editText!!.text.toString(),
                            downloadUrl.toString().trim { it <= ' ' })
                        val personEmail = email_string!!.trim { it <= ' ' }
                        val uid = key!!
                        // Listen to database changes while data is uploading
                        mDatabaseRef_Info!!.child(uid)
                            .setValue(upload
                            ) { error, ref ->
                                println("Value was set. Error = $error")
                                if (error != null) {  // if there is some error while data is being uploaded, show the following toast
                                    Toast.makeText(
                                        this@DoctorDetails,
                                        "Data could not be saved! " + error.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    button_upload.isEnabled =
                                        true // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
                                } else {  // if data is uploaded without any error, show the following and move to next activity called 'AfterLoginActivity'
                                    Toast.makeText(
                                        this@DoctorDetails,
                                        "Data written to database successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Finally after uploading the info to the cloud, delete the redundant image from the phone storage
                                    val redundantFile =
                                        File(myExternalFile_temporary.toString())
                                    if (redundantFile.exists()) {
                                        redundantFile.delete()
                                    }
                                    val intent = Intent(this@DoctorDetails, KeyEnter::class.java)
                                    startActivity(intent)
                                }
                            }
                    }
                })
            }.addOnFailureListener { e ->

                // if somehow the upload task is not successful, listen for any failures
                // if the code fails, show the following toast notification
                Toast.makeText(this@DoctorDetails, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                button_upload.isEnabled = true // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
            }.addOnProgressListener {
                // Progress Listener for changing color of the progressBar
                // if the upload task is going on (in progression), listen for changes
                fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) { // Progress Listener for changing color of the progressBar
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    mProgressBar!!.progress = progress.toInt()
                }
            }


        }
        else { // if the URI of the image is null, probably the user has not selected any file. In that case show the following notification
            Toast.makeText(this, "No File Selected...", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> if (resultCode == RESULT_OK) {
                photoBitmap =
                    data!!.extras!!["data"] as Bitmap? // get the bitmap of the clicked image

                // the following if-else code, makes the aspect ratio of image 1:1
                photoBitmap = if (photoBitmap!!.width >= photoBitmap!!.height) {
                    Bitmap.createBitmap(
                        photoBitmap!!,
                        photoBitmap!!.width / 2 - photoBitmap!!.height / 2,
                        0,
                        photoBitmap!!.height,
                        photoBitmap!!.height
                    )
                } else {
                    Bitmap.createBitmap(
                        photoBitmap!!,
                        0,
                        photoBitmap!!.height / 2 - photoBitmap!!.width / 2,
                        photoBitmap!!.width,
                        photoBitmap!!.width
                    )
                }

                // The following code makes sure that the image is displayed correctly in the imageview
                // the above code flips the captured image, therefore, the following code flips it agin to display it correctly
                val matrix = Matrix() // matrix stores the 2D bitmap
                matrix.setScale(
                    -1f,
                    1f
                ) // -1 flips the image horizontally, 1 keeps the image same along vertical direction
                matrix.postTranslate(
                    photoBitmap!!.getWidth()!!.toFloat(),
                    0f
                ) // this command flips the 'photoBitmap'
                photoBitmap = Bitmap.createBitmap(
                    photoBitmap!!,
                    0,
                    0,
                    photoBitmap!!.getWidth(),
                    photoBitmap!!.getHeight(),
                    matrix,
                    true
                )
                mImageView!!.setImageBitmap(photoBitmap) // set the resulting bitmap in the image view (mImageView)

                // The following code stores the URI of the captured image in the variable 'resultURI', that will be used to upload the image to the cloud (when 'upload' button is clicked)
                // to get the URI of this image, first store it in the phone storage, then extract it's path; then convert that path to URI and store that URI in 'resultUri'
                try {
                    mImageView!!.isDrawingCacheEnabled =
                        true // so that I can extract the bitmap of the image displayed in 'mImageView'
                    val bmap =
                        mImageView!!.drawingCache // this command actually extracts the bitmap of the image and stores it in the variable 'bmap'
                    runOnUiThread {
                        if (bmap != null) {
                            val filename = "img.jpg" // name of the stored image
                            val filepath =
                                email_string!!.trim { it <= ' ' } // make a user-specific folder in the internal storage. The folder name will be the email address of the user
                            myExternalFile_temporary = File(
                                getExternalFilesDir(filepath),
                                filename
                            ) // all the user specific credentials are also stored in the internal storage with the file name 'info.txt'
                            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {  // if the storage is not available in the phone, show the following toast message
                                Toast.makeText(
                                    this@DoctorDetails,
                                    "Your external storage is not available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {  // if the storage is available in the phone, store the image named as 'img.jpg' in the phone storage
                                try {  // try executing the following
                                    val fos =
                                        FileOutputStream(myExternalFile_temporary) // fileOutputStream is responsible for writing/reading files
                                    bmap.compress(
                                        Bitmap.CompressFormat.JPEG,
                                        100,
                                        fos
                                    ) // write the bitmap file
                                    fos.close() // close writing
                                    Toast.makeText(
                                        this@DoctorDetails,
                                        "Captured and Saved!$myExternalFile_temporary",
                                        Toast.LENGTH_LONG
                                    )
                                        .show() // show this toast notification once the image is successfully stored in the phone storage
                                    resultUri =
                                        Uri.fromFile(File(myExternalFile_temporary.toString())) // storing the image URI in the variable 'resultUri'
                                    mImageView!!.destroyDrawingCache() // just destroy cache after all necessary actions are performed for the sake of clearing space
                                    mImageView!!.isDrawingCacheEnabled = false
                                } catch (e: java.lang.Exception) {  // if the 'try' statement fails, execute the following
                                    Toast.makeText(
                                        this@DoctorDetails,
                                        "Storing image in the phone has failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                }
            }

            1 -> if (resultCode == RESULT_OK && data != null && data.data != null) {
                CropImage.activity(data.data) // crop the image to make its aspect ratio as 1:1
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }

        // once cropping of image is done, store the resulting image URI in the variable 'resultUri'
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            try {
                resultUri = result.getUri()
                Picasso.get().load(resultUri)
                    .into(mImageView) // display this cropped image in the imageView element 'mImageView'
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExtensions(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton() // 'mime' stores the extension of the file
        return mime.getExtensionFromMimeType(cR.getType(uri)) // this command returns the 'mime' type of the file
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
            true
        } else false
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == extStorageState) {
            true
        } else false
    }
}