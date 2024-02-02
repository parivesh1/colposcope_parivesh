package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.home_page.toolbar

class PatientList : AppCompatActivity(){
    var myLayout: LinearLayout? = null
    private var mDatabaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_list)
        toolbar.title = "Patient List"

        myLayout = findViewById(R.id.listViewLayout)
        mDatabaseRef =
            FirebaseDatabase.getInstance().getReference("DoctorsList").child(Global.doctorUID)
                .child("PatientList")

        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (uniqueKeySnapshot in snapshot.children) {
                    mDatabaseRef!!.child(uniqueKeySnapshot.key!!)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val uploadObj = snapshot.getValue()as HashMap<*, *>
                                var upload = UploadPatient()
                                with(uploadObj){
                                    upload!!.Name=uploadObj["name"].toString()
                                    upload!!.DOB=uploadObj["dob"].toString()
                                    upload!!.Mobile= uploadObj["mobile"].toString()
                                }
                                addPatientCard(
                                    upload!!.Name,
                                    upload!!.Mobile,
                                    upload!!.DOB,
                                    uniqueKeySnapshot.key!!
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
    private fun addPatientCard(name: String, mobile: String, dob: String, patientId: String) {
        val view = layoutInflater.inflate(R.layout.patient_card, null)
        val patientName = view.findViewById<TextView>(R.id.patientName)
        val patientMobile = view.findViewById<TextView>(R.id.patientMobile)
        val patientDob = view.findViewById<TextView>(R.id.patientDob)
        patientDob.text = dob
        patientName.text = name
        patientMobile.text = mobile
        view.setOnClickListener {
            Global.patientId = patientId
            System.out.println(Global.patientId)
            val intent = Intent(this@PatientList, PatientDetails::class.java)
            startActivity(intent)
        }
        myLayout!!.addView(view)
    }
}