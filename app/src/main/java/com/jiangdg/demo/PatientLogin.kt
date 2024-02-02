package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.patient_login.emailView
import kotlinx.android.synthetic.main.patient_login.getReport
import kotlinx.android.synthetic.main.patient_login.reports_list

class PatientLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        getReport.setOnClickListener(View.OnClickListener {
            println(emailView.getText())
            val mDatabaseReference = FirebaseDatabase.getInstance()
                .getReference("DoctorsList")
            mDatabaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (uniqueKeySnapshot in snapshot.children) {
                        mDatabaseReference.child(uniqueKeySnapshot.key!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    println("Doctors id =" + uniqueKeySnapshot.key)
                                    mDatabaseReference.child(uniqueKeySnapshot.key!!)
                                        .child("PatientList")
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (patientId in snapshot.children) {
                                                    println("Patient Doctor Id:" + uniqueKeySnapshot.key)
                                                    println("Patient Id:" + patientId.key)
                                                    mDatabaseReference.child(uniqueKeySnapshot.key!!)
                                                        .child("PatientList")
                                                        .child(patientId.key!!).child("email")
                                                        .addValueEventListener(object :
                                                            ValueEventListener {
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                println("patient Email:" + snapshot.value)
                                                                if (snapshot.value.toString() == emailView.getText()
                                                                        .toString()
                                                                        .trim { it <= ' ' }
                                                                ) {
                                                                    println("values are equal")
                                                                } else {
                                                                    println("values are not equal")
                                                                }
                                                                if (snapshot.value.toString() == emailView.getText()
                                                                        .toString()
                                                                        .trim { it <= ' ' }
                                                                ) {
                                                                    mDatabaseReference.child(
                                                                        uniqueKeySnapshot.key!!
                                                                    )
                                                                        .child("PatientList").child(
                                                                            patientId.key!!
                                                                        )
                                                                        .child("report")
                                                                        .addValueEventListener(
                                                                            object :
                                                                                ValueEventListener {
                                                                                override fun onDataChange(
                                                                                    snapshot: DataSnapshot
                                                                                ) {
                                                                                    for (uniqueDate in snapshot.children) {
                                                                                        mDatabaseReference.child(
                                                                                            uniqueKeySnapshot.key!!
                                                                                        )
                                                                                            .child("PatientList")
                                                                                            .child(
                                                                                                patientId.key!!
                                                                                            )
                                                                                            .child("report")
                                                                                            .child(
                                                                                                uniqueDate.key!!
                                                                                            )
                                                                                            .addValueEventListener(
                                                                                                object :
                                                                                                    ValueEventListener {
                                                                                                    override fun onDataChange(
                                                                                                        snapshot: DataSnapshot
                                                                                                    ) {
                                                                                                        for (uniqueReportId in snapshot.children) {
                                                                                                            mDatabaseReference.child(
                                                                                                                uniqueKeySnapshot.key!!
                                                                                                            )
                                                                                                                .child(
                                                                                                                    "PatientList"
                                                                                                                )
                                                                                                                .child(
                                                                                                                    patientId.key!!
                                                                                                                )
                                                                                                                .child(
                                                                                                                    "report"
                                                                                                                )
                                                                                                                .child(
                                                                                                                    uniqueDate.key!!
                                                                                                                )
                                                                                                                .child(
                                                                                                                    uniqueReportId.key!!
                                                                                                                )
                                                                                                                .addValueEventListener(
                                                                                                                    object :
                                                                                                                        ValueEventListener {
                                                                                                                        override fun onDataChange(
                                                                                                                            snapshot: DataSnapshot
                                                                                                                        ) {
                                                                                                                            addPatientCard(
                                                                                                                                uniqueDate.key!!,
                                                                                                                                uniqueReportId
                                                                                                                                    .value.toString(),
                                                                                                                                uniqueReportId.key!!
                                                                                                                            )
                                                                                                                        }

                                                                                                                        override fun onCancelled(
                                                                                                                            error: DatabaseError
                                                                                                                        ) {
                                                                                                                            println(
                                                                                                                                "Error in pdf report$error"
                                                                                                                            )
                                                                                                                        }
                                                                                                                    })
                                                                                                        }
                                                                                                    }

                                                                                                    override fun onCancelled(
                                                                                                        error: DatabaseError
                                                                                                    ) {
                                                                                                        println(
                                                                                                            "Error in report Id$error"
                                                                                                        )
                                                                                                    }
                                                                                                })
                                                                                    }
                                                                                }

                                                                                override fun onCancelled(
                                                                                    error: DatabaseError
                                                                                ) {
                                                                                    println("Error in pdf report date$error")
                                                                                }
                                                                            })
                                                                }
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                println("Error in report$error")
                                                            }
                                                        })
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                println("Error in patient Id$error")
                                            }
                                        })
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    println("Error in Doc$error")
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error in ref$error")
                }
            })
        })
    }

    private fun addPatientCard(date: String, pdfUri: String, id: String) {
        val view = layoutInflater.inflate(R.layout.patient_history_card, null)
        val dateView = view.findViewById<TextView>(R.id.date)
        val idView = view.findViewById<TextView>(R.id.id)
        dateView.text = date
        idView.text = id
        view.setOnClickListener {
            Global.pdfUri = pdfUri
            //                System.out.println(Global.patientId);
            val intent = Intent(this@PatientLogin, PdfView::class.java)
            startActivity(intent)
            //                Intent browserIntent = new Intent(Intent.ACTION_VIEW, pdfUri);
//                startActivity(browserIntent);
        }
        reports_list.addView(view)
    }
}