package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.refer_patient.commentView
import kotlinx.android.synthetic.main.refer_patient.listViewLayout
import kotlinx.android.synthetic.main.refer_patient.toolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReferPatient : AppCompatActivity(){


    private var mDatabaseRef: DatabaseReference? = null
    private var mDatabasePatientRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refer_patient)
        toolbar.title="Refer Patient"

        mDatabasePatientRef = FirebaseDatabase.getInstance().reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorsList")
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (uniqueKeySnapshot in snapshot.children) {
                    mDatabaseRef!!.child(uniqueKeySnapshot.key. toString())
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val uploadObj = snapshot.getValue()as HashMap<*, *>
                                var upload = Upload()
                                with(uploadObj){
                                    upload!!.Name=uploadObj["name"].toString()
                                    upload!!.DOB=uploadObj["dob"].toString()
                                    upload!!.Mobile= uploadObj["qualification"].toString()
                                }

                                addDocCard(
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

    private fun addDocCard(name: String, mobile: String, dob: String, docId: String) {
        val view = layoutInflater.inflate(R.layout.patient_card, null)
        val patientName = view.findViewById<TextView>(R.id.patientName)
        val patientMobile = view.findViewById<TextView>(R.id.patientMobile)
        val patientDob = view.findViewById<TextView>(R.id.patientDob)
        val dobText = view.findViewById<TextView>(R.id.dob)
        dobText.text = "Qualification"
        patientDob.text = dob
        patientName.text = name
        patientMobile.text = mobile
        view.setOnClickListener { referPatient(docId) }
        listViewLayout.addView(view)
    }

    private fun referPatient(docId: String) {
        val comment: String = commentView.getText().toString()
        if (commentView.getText().toString().isEmpty()) {
            Toast.makeText(
                this@ReferPatient,
                "Comment Field Is Empty ", Toast.LENGTH_SHORT
            ).show()
        } else {
            val today = Calendar.getInstance().time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(today)
            val referPatients = ReferPatientData(
                Global.patientId,
                Global.doctorUID,
                docId,
                comment, date
            )

            mDatabasePatientRef!!.child("referredPatientsList").push().setValue(
                referPatients,
                DatabaseReference.CompletionListener { error, ref ->
                    if (error == null) {
                        Toast.makeText(
                            this@ReferPatient,
                            "Patient Referred ", Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(
                            this@ReferPatient,
                            ReportSelection::class.java
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@ReferPatient,
                            "Data could not be saved! " +
                                    error.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}