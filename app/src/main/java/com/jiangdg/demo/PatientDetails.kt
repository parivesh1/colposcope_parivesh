package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_page.toolbar
import kotlinx.android.synthetic.main.patient_details.DoctorName_textView
import kotlinx.android.synthetic.main.patient_details.address_textView
import kotlinx.android.synthetic.main.patient_details.clickForPatientHistory
import kotlinx.android.synthetic.main.patient_details.mobile_textView
import kotlinx.android.synthetic.main.patient_details.realTimeStreaming
import kotlinx.android.synthetic.main.patient_details.user_imageView
import kotlinx.android.synthetic.main.patient_details.userage_textView
import kotlinx.android.synthetic.main.patient_details.userbloodgp_textView
import kotlinx.android.synthetic.main.patient_details.userdob_textView
import kotlinx.android.synthetic.main.patient_details.useremail_textView
import kotlinx.android.synthetic.main.patient_details.username_textView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class PatientDetails : AppCompatActivity(){
    private var mDatabaseRef_Info: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_details)
        //setting the title
        toolbar.title = "Patient Details"

        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference
        val patientImage =
            mDatabaseRef_Info!!.child("DoctorsList").child(Global.doctorUID).child("PatientList")
                .child(Global.patientId).child("imageUrl")


        //Sets up patient Image references.
        patientImage.addValueEventListener(object : ValueEventListener {
            // listen for any changes happening in the 'userUrl' database location
            override fun onDataChange(snapshot: DataSnapshot) {  //if the data within 'userUrl' database location changes
                val value = snapshot.value.toString()
                Picasso.get().load(value)
                    .into(user_imageView) // then set the value of the 'user_imageView' to be this new changed data
            }

            override fun onCancelled(error: DatabaseError) {  // if the listener fails to listen to changes, then do nothing
            }
        })

        displayInfo()
        realTimeStreaming.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        clickForPatientHistory.setOnClickListener{
            Intent(this, PatientHistory::class.java).apply {
                startActivity(this)
                finish()
            }
        }



    }

    private fun displayInfo() {
        val ref = FirebaseDatabase.getInstance().reference
        val userName = ref.child("DoctorsList").child(Global.doctorUID).child("name")
        val patientInfo = ref.child("DoctorsList").child(Global.doctorUID).child("PatientList")
            .child(Global.patientId)
        patientInfo.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val uploadObj = snapshot.getValue()as HashMap<*, *>
                var upload = UploadPatient()
                with(uploadObj){
                    upload!!.Name=uploadObj["name"].toString()
                    upload!!.Email=uploadObj["email"].toString()
                    upload!!.DOB=uploadObj["dob"].toString()
                    upload!!.BloodGroup=uploadObj["bloodGroup"].toString()
                    upload!!.Mobile= uploadObj["mobile"].toString()
                    upload!!.Address = uploadObj["address"].toString()

                }
                username_textView.text = upload!!.Name
                useremail_textView.text = upload!!.Email
                val dobString: String = upload!!.DOB.toString()
                userdob_textView.text = upload!!.DOB.toString()
                var date: Date? = null
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                try {
                    date = sdf.parse(dobString)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val dob = Calendar.getInstance()
                val today = Calendar.getInstance()
                dob.time = date
                val age = today[Calendar.YEAR] - dob[Calendar.YEAR]
                userage_textView.text = age.toString()
                userbloodgp_textView.text = upload!!.BloodGroup
                mobile_textView.text = upload!!.Mobile
                address_textView.text = upload!!.Address
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        userName.addValueEventListener(object : ValueEventListener {
            // listen for any changes happening in the 'mRefName' database location  //if the data within 'mRefName' database location changes
            override fun onDataChange(snapshot: DataSnapshot) {  //if the data within 'userName' database location changes
                Log.d("name", snapshot.getValue(String::class.java)!!)
                DoctorName_textView.text =
                    snapshot.value.toString() // then set the value of the 'uername_textView' to be this new changed data
            }

            override fun onCancelled(error: DatabaseError) {  // if the listener fails to listen to changes, then do nothing
            }
        })
    }
}