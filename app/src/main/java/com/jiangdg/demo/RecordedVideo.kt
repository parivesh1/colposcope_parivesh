package com.jiangdg.demo

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.recorded_video.idVideoView
import kotlinx.android.synthetic.main.recorded_video.progress_bar
import kotlinx.android.synthetic.main.recorded_video.uploadVideoButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects

class RecordedVideo: AppCompatActivity() {
    var videoUri: String? = null
    private var uploadTask: StorageTask<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recorded_video)

        val intent = intent
        videoUri = Objects.requireNonNull<Bundle?>(intent.extras).getString("videoUri")

        val file = File("$videoUri.mp4")
        if (file.exists()) {
            idVideoView.setVideoPath("$videoUri.mp4")
            val mediaController = MediaController(this)
            mediaController.setAnchorView(idVideoView)
            mediaController.setMediaPlayer(idVideoView)
            idVideoView.setMediaController(mediaController)
            idVideoView.start()
        } else {
            Toast.makeText(this, "file not exists", Toast.LENGTH_SHORT).show()
        }
        uploadVideoButton.setOnClickListener{
            progress_bar.visibility = View.VISIBLE
            uploadVideo()
        }

    }
    private fun uploadVideo() {
        if (videoUri != null) {
            val file: File = File(videoUri + ".mp4")
            if (file.exists()) {
                val uploadVideoUri: Uri
                uploadVideoUri = Uri.fromFile(file)
                val reference =
                    FirebaseStorage.getInstance().reference.child("/video/" + Global.patientId + "/" + videoUri)
                //            reference.child("/videos/" + Global.patientId + "/" + path);
                uploadTask = reference.putFile(uploadVideoUri)
                (uploadTask as UploadTask).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUri = uri.toString()
                        Toast.makeText(this@RecordedVideo, "Uploading!", Toast.LENGTH_SHORT)
                            .show()
                        val today = Calendar.getInstance().time
                        val sdf =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = sdf.format(today)
                        Toast.makeText(
                            this@RecordedVideo,
                            "Video Uploaded!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val reference1 = FirebaseDatabase.getInstance().reference
                        reference1.child("DoctorsList").child(Global.doctorUID)
                            .child("PatientList").child(Global.patientId).child("Videos")
                            .child(date).child(System.currentTimeMillis().toString())
                            .setValue(downloadUri)
                        //                                Intent intent = new Intent(recordedVideoView.this, USBCameraActivity.class);
                        //                                startActivity(intent);
                        progress_bar.visibility = View.GONE
                    }
                }).addOnFailureListener(OnFailureListener { e ->
                    progress_bar.visibility = View.GONE
                    Toast.makeText(
                        this@RecordedVideo,
                        "Uploading! failed$e",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            } else {
                Toast.makeText(this, "file not Exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}