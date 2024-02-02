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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.new_patient.RegisterPatientButton
import kotlinx.android.synthetic.main.new_patient.patientImageButton
import kotlinx.android.synthetic.main.new_patient.toolbar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Objects

class NewPatient : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var patientEmail: EditText? = null
    var patientName:EditText? = null
    var patientMobile:EditText? = null
    var patientAddress:EditText? = null
    var patientPassword:EditText? = null
    private var patientImage: ImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var docName: TextView? = null
    private  var patientDob:TextView? = null
    private var patientBloodGroup: Spinner? = null
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val STORAGE_PERMISSION_REQUEST_CODE = 1
    private val CAMERA_REQUEST = 1888
    private var photoBitmap: Bitmap? = null
    var myExternalFile_temporary: File? = null
    private var downloadUrl: Uri? = null
    private  var resultUri:Uri? = null
    private var mDatabaseRef_Doc: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mUploadTask: StorageTask<*>? = null
    var thumb_bitmap: Bitmap? = null
    var categories: List<String> = ArrayList()
    var bloodGroupAdapter: ArrayAdapter<String>? = null
    var spin_item: String? = null
    private var mDateSetListener: OnDateSetListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_patient)
        toolbar.title = "Register New Patient"
        mDatabaseRef_Doc = FirebaseDatabase.getInstance().reference
        mStorageRef = FirebaseStorage.getInstance().reference

        //mapping elements to variables
        patientEmail = findViewById(R.id.patientEmail)
        patientName = findViewById(R.id.patientName)
        patientMobile = findViewById(R.id.patientMobile)
        patientAddress = findViewById(R.id.patientAddress)
        mProgressBar = findViewById(R.id.progress_bar)
        docName = findViewById(R.id.docName)
        patientImage = findViewById(R.id.patientImage)
        patientDob = findViewById(R.id.patientDob)
        patientBloodGroup = findViewById(R.id.patientBloodGroup)
        patientPassword = findViewById(R.id.patientPass)

        patientBloodGroup!!.setOnItemSelectedListener(this)

        categories+="A+"
        categories+="A-"
        categories+="B+"
        categories+="B-"
        categories+="AB+"
        categories+="AB-"
        categories+="O+"
        categories+="O-"
        categories+="Rare blood groups: hh, Rh-null,etc"


        // Creating adapter for spinner
        bloodGroupAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories)
        // Drop down layout style - list view with radio button
        bloodGroupAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        patientBloodGroup!!.adapter = bloodGroupAdapter

        //display doctor's name
        displayDocInfo()

        val permissionCheckStorage = ContextCompat.checkSelfPermission(
            this@NewPatient,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )


        RegisterPatientButton.setOnClickListener {


//            final Intent intent = new Intent(createNewPatient.this, createNewPatient.class);
//            startActivity(intent);
            if (patientEmail!!.text.toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "Email field Can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
//            else if (!(patientEmail!!.text.toString()==(emailPattern!!))) {
//                Toast.makeText(this@NewPatient, "Invalid Email Address", Toast.LENGTH_SHORT)
//                    .show()
//            }
            else if (patientName!!.text.toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "Name field Can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (patientName!!.text.toString().length < 3) {
                Toast.makeText(this@NewPatient, "Invalid Name", Toast.LENGTH_SHORT).show()
            } else if (patientPassword!!.text.toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "Password Field can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (patientPassword!!.getText().toString().length < 6) {
                Toast.makeText(
                    this@NewPatient,
                    "Password Must Contain at least 6 letter",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (patientDob!!.getText().toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "DOB field Can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (patientMobile!!.getText().toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "Mobile field Can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (patientMobile!!.getText().toString().length < 10) {
                Toast.makeText(this@NewPatient, "Invalid Mobile Number", Toast.LENGTH_SHORT)
                    .show()
            } else if (patientAddress!!.getText().toString()== "") {
                Toast.makeText(
                    this@NewPatient,
                    "Address field Can't be Empty",
                    Toast.LENGTH_SHORT
                ).show()
//                   Log.d("Patient Mobile Number",patientMobile.getText().toString());
            } else if (patientAddress!!.getText().toString().length < 3) {
                Toast.makeText(this@NewPatient, "Invalid Address", Toast.LENGTH_SHORT).show()
            } else if (resultUri == null) {
                Toast.makeText(this@NewPatient, "Select Patient Image", Toast.LENGTH_SHORT)
                    .show()
            } else {
                RegisterPatientButton.isEnabled =
                    false // Disabling the 'upload' button to avoid multiple presses
                Toast.makeText(this@NewPatient, "Please Wait!", Toast.LENGTH_SHORT).show()
                // Simultaneously, if the user credentials are being uploaded to the cloud, show the following toast notification to the user
                if (mUploadTask != null && mUploadTask!!.isInProgress) {
                    Toast.makeText(this@NewPatient, "Upload in Progress", Toast.LENGTH_SHORT)
                        .show()
                    RegisterPatientButton.isEnabled = false
                } else {
                    Toast.makeText(this@NewPatient, "Uploading Your Data", Toast.LENGTH_SHORT)
                        .show()
                    //if somehow the user credentials are not being uploaded to the cloud, call the following method
                    registerPatient() // this method begins to upload the user credentials on the cloud
                }
            }
        }
        // when 'dobOfPatient_textView' textView element is clicked, it allows the user to select a Day, Month and Year from the calendar
        patientDob!!.setOnClickListener(View.OnClickListener {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                this@NewPatient,
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
            month = month + 1
            val date = "$year-$month-$day"
            // Toast.makeText(newUser.this, "Date: "+date, Toast.LENGTH_SHORT).show();
            patientDob!!.text = date
        }
        patientImageButton.setOnClickListener{
            if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(
                        this@NewPatient,
                        Manifest.permission.CAMERA
                    )
                    == PackageManager.PERMISSION_DENIED
                ){
                    ActivityCompat.requestPermissions(
                        this@NewPatient,
                        arrayOf<String>(
                            Manifest.permission.CAMERA
                        ), 100
                    )
                }

                if (ContextCompat.checkSelfPermission(
                        this@NewPatient,
                        Manifest.permission.CAMERA
                    )
                    == PackageManager.PERMISSION_GRANTED
                ){
                    // show an alert box, asking user whether he/she wants to select image from gallery or camera
                    val alert = AlertDialog.Builder(this@NewPatient)
                    alert.setTitle("Choose the Image Source")
                    alert.setIcon(android.R.drawable.ic_dialog_alert)

                    // if the user clicks on Gallery option, execute the following and open 'onActivityRequest'

                    // if the user clicks on Gallery option, execute the following and open 'onActivityRequest'
                    alert.setPositiveButton("Gallery") { dialog, which ->
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
                                this@NewPatient,
                                "Gallery Error: " + error.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    // if the user clicks on Camera option, execute the following and open 'onActivityRequest'

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
                                this@NewPatient,
                                "Gallery Error: " + error.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    alert.show()
                } else { // if the user denies camera permission, show the following toast
                    Toast.makeText(
                        this@NewPatient,
                        "First, grant CAMERA permission from the Settings!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {  // if internal storage read permission is not allowed, show the following toast to the user
                Toast.makeText(
                    this@NewPatient,
                    "You don't have the permission to read from the user device",
                    Toast.LENGTH_SHORT
                ).show()
                ActivityCompat.requestPermissions(
                    this@NewPatient,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
        spin_item = adapterView.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    private fun displayDocInfo() {
        val ref = FirebaseDatabase.getInstance().reference
        val userName = ref.child("DoctorsList").child(Global.doctorUID).child("name")
        userName.addValueEventListener(object : ValueEventListener {
            // listen for any changes happening in the 'mRefName' database location  //if the data within 'mRefName' database location changes
            override fun onDataChange(snapshot: DataSnapshot) {  //if the data within 'userName' database location changes
                Log.d("name", snapshot.getValue(String::class.java)!!)
                docName!!.text =
                    snapshot.getValue(String::class.java) // then set the value of the 'uername_textView' to be this new changed data
            }

            override fun onCancelled(error: DatabaseError) {  // if the listener fails to listen to changes, then do nothing
            }
        })
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
                // the above code flips the captured image, therefore, the following code flips it again to display it correctly
                val matrix = Matrix() // matrix stores the 2D bitmap
                matrix.setScale(
                    -1f,
                    1f
                ) // -1 flips the image horizontally, 1 keeps the image same along vertical direction
                matrix.postTranslate(
                    photoBitmap!!.getWidth().toFloat(),
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
                patientImage!!.setImageBitmap(photoBitmap) // set the resulting bitmap in the image view (patientImages)

                // The following code stores the URI of the captured image in the variable 'resultURI', that will be used to upload the image to the cloud (when 'upload' button is clicked)
                // to get the URI of this image, first store it in the phone storage, then extract it's path; then convert that path to URI and store that URI in 'resultUri'
                try {
                    patientImage!!.isDrawingCacheEnabled =
                        true // so that I can extract the bitmap of the image displayed in 'patientImage'
                    val bmap =
                        patientImage!!.drawingCache // this command actually extracts the bitmap of the image and stores it in the variable 'bmap'
                    runOnUiThread {
                        if (bmap != null) {
                            val filename = "img.jpg" // name of the stored image
                            val filepath =
                                Global.doctorUID.trim() // make a user-specific folder in the internal storage. The folder name will be the email address of the user
                            myExternalFile_temporary = File(
                                getExternalFilesDir(filepath),
                                filename
                            ) // all the user specific credentials are also stored in the internal storage with the file name 'info.txt'
                            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {  // if the storage is not available in the phone, show the following toast message
                                Toast.makeText(
                                    this@NewPatient,
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
                                    //                                            Toast.makeText(createNewPatient.this, "Captured and Saved!" + myExternalFile_temporary, Toast.LENGTH_LONG).show();  // show this toast notification once the image is successfully stored in the phone storage
                                    resultUri =
                                        Uri.fromFile(File(myExternalFile_temporary.toString())) // storing the image URI in the variable 'resultUri'
                                    patientImage!!.destroyDrawingCache() // just destroy cache after all necessary actions are performed for the sake of clearing space
                                    patientImage!!.isDrawingCacheEnabled = false
                                } catch (e: Exception) {  // if the 'try' statement fails, execute the following
                                    Toast.makeText(
                                        this@NewPatient,
                                        "Storing image in the phone has failed!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
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
            val result = CropImage.getActivityResult(data)
            try {
                resultUri = result.uri
                Picasso.get().load(resultUri)
                    .into(patientImage) // display this cropped image in the imageView element 'mImageView'
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExtensions(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton() // 'mime' stores the extension of the file
        return mime.getExtensionFromMimeType(cR.getType(uri)) // this command returns the 'mime' type of the file
    }

    private fun registerPatient() {
        val thumb_filePathUri = File(
            Objects.requireNonNull(
                resultUri!!.path
            )
        ) // 'thumb_filePathUri' stores the path location of the image
        try {
            thumb_bitmap =
                Compressor(this@NewPatient) // here we try to compress the image and store the resulting bitmap in 'thumb_bitmap'
                    .setMaxWidth(100)
                    .setMaxHeight(100)
                    .setQuality(50)
                    .compressToBitmap(thumb_filePathUri)
        } catch (e: IOException) {  // if somehow the compression fails, show the following message in the console
            // try-catch block prevents the application from crashing, in case the code fails to execute  // 'byteArrayOutputStream' stores the bitmap of the image
            e.printStackTrace()
        }
        val byteArrayOutputStream =
            ByteArrayOutputStream() // here the quality of the image is reduced to 50%. This is done to reduce the overall size of the Image (because database has limited space)
        thumb_bitmap!!.compress(
            Bitmap.CompressFormat.JPEG,
            50,
            byteArrayOutputStream
        ) // here we are converting the bitmap to byteArray
        val thumb_byte = byteArrayOutputStream.toByteArray()
        val fileReference = mStorageRef!!.child(
            System.currentTimeMillis().toString() + "." + getFileExtensions(
                resultUri!!
            )
        ) // the image file will be stored in the firebase Storage, where the name of the file will be System.currentTimeMillis().mime (eg- 16347873498.png)

        // The next command says that if the image file is being uploaded to the firebase storage location, listen to the changes
        mUploadTask = fileReference.putBytes(thumb_byte).addOnSuccessListener {
            // if the image file is uploaded to the firebase storage location successfully, call this method
            runOnUiThread(Thread {
                mProgressBar!!.progress = 0
                fileReference.downloadUrl.addOnSuccessListener { uri ->

                    // copy the URL of the image stored in the storage reference of the firebase cloud
                    downloadUrl =
                        uri // if copying the image URI is successful, store that in the variable 'downloadUrl'
                    Log.d("doc userid ", Global.doctorUID)
                    val upload = UploadPatient(
                        patientEmail!!.text.toString(),
                        patientName!!.text.toString(),
                        patientDob!!.text.toString(),
                        patientMobile!!.text.toString(),
                        patientAddress!!.text.toString(),
                        spin_item.toString(),
                        downloadUrl.toString().trim { it <= ' ' },
                        patientPassword!!.text.toString())

                    mDatabaseRef_Doc!!.child("DoctorsList").child(Global.doctorUID)
                        .child("PatientList").push()
                        .setValue(upload
                        ) { error, ref ->
                            println("Value was set. Error = $error")
                            if (error != null) {  // if there is some error while data is being uploaded, show the following toast
                                Toast.makeText(
                                    this@NewPatient,
                                    "Data could not be saved! " + error.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                RegisterPatientButton.isEnabled =
                                    true // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
                            } else {  // if data is uploaded without any error, show the following and move to next activity called 'AfterLoginActivity'
                                Toast.makeText(
                                    this@NewPatient,
                                    "Data written to database successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Global.patientId = ref.key.toString()
//                                val imgDir = File("/Colposcope/"+Global.patientId+"/Images")
//                                val vidDir = File("/Colposcope/"+Global.patientId+"/Videos")
//                                val greenImgDir = File("/Colposcope/"+Global.patientId+"/Images/Green Image")
//                                if(imgDir.mkdirs()){
//                                    Toast.makeText(this@NewPatient, "Folder Created", Toast.LENGTH_SHORT).show()
//                                }
//                                else{
//                                    Toast.makeText(this@NewPatient, "Folder Not Created", Toast.LENGTH_SHORT).show()
//                                }
                                Log.d("PatientId", Global.patientId)
                                // Finally after uploading the info to the cloud, delete the redundant image from the phone storage
                                val redundantFile =
                                    File(myExternalFile_temporary.toString())
                                if (redundantFile.exists()) {
                                    redundantFile.delete()
                                }
                                Toast.makeText(this@NewPatient, "Doctor "+Global.doctorUID, Toast.LENGTH_SHORT).show()
                                Toast.makeText(this@NewPatient, "PatientId"+Global.patientId, Toast.LENGTH_SHORT).show()
                                // via intent component, move to the next class called 'oldUser'
                                val intent = Intent(
                                    this@NewPatient,
                                    PatientDetails::class.java
                                )
                                startActivity(intent) // start the new activity
                            }
                        }
                }
            })
        }.addOnFailureListener { e ->

            // if somehow the upload task is not successful, listen for any failures
            // if the code fails, show the following toast notification
            Toast.makeText(this@NewPatient, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            RegisterPatientButton.isEnabled =
                true // if somehow data upload fails, enable the 'upload' button; so the user can again try to upload data to the cloud!
        }
            .addOnProgressListener { taskSnapshot ->
                // if the upload task is going on (in progression), listen for changes
                // Progress Listener for loading
                // percentage on the dialog box
                // Progress Listener for changing color of the progressBar
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                mProgressBar!!.progress = progress.toInt()
            }
    }
}