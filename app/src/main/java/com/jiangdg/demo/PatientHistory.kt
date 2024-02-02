package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.home_page.toolbar

class PatientHistory: AppCompatActivity() {
    private var mDatabaseRef: DatabaseReference? = null
    private var myLayout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_history)

        //setting the title
        toolbar.title = "Patient Record"

        myLayout = findViewById(R.id.reports_list)

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorsList")
            .child(Global.doctorUID).child("PatientList").child(Global.patientId)
            .child("report")

        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (uniqueKeySnapshot in snapshot.children) {
                    mDatabaseRef!!.child(uniqueKeySnapshot.key!!)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                println("date = " + uniqueKeySnapshot.key)
                                for (uniqueId in snapshot.children) {
                                    mDatabaseRef!!.child(uniqueKeySnapshot.key!!)
                                        .child(uniqueId.key!!)
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                println("uniqueKey = " + uniqueId.key)
                                                println("PdfUri = " + uniqueId.value)
                                                addPatientCard(
                                                    uniqueKeySnapshot.key!!, uniqueId
                                                        .value.toString(), uniqueId.key!!
                                                )
                                            }

                                            override fun onCancelled(error: DatabaseError) {}
                                        })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    private fun addPatientCard(date: String, pdfUri: String, id: String) {
        val view: View = layoutInflater.inflate(R.layout.patient_history_card, null)
        val dateView = view.findViewById<TextView>(R.id.date)
        val idView = view.findViewById<TextView>(R.id.id)
        dateView.text = date
        idView.text = id
        view.setOnClickListener {
            Global.pdfUri = pdfUri
            val intent = Intent(this@PatientHistory, PdfView::class.java)
            startActivity(intent)
        }
        myLayout!!.addView(view)
    }
}