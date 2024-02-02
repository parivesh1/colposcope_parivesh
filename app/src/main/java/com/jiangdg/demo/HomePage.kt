package com.jiangdg.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.home_page.newPatientButton
import kotlinx.android.synthetic.main.home_page.oldPatientButton
import kotlinx.android.synthetic.main.home_page.toolbar


class HomePage : AppCompatActivity(){
    val reference = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        //setting the title
        toolbar.title = "Register New Patient"

        newPatientButton.setOnClickListener {
            Intent(this, NewPatient::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        oldPatientButton.setOnClickListener {
            Intent(this, PatientList::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        reference.child("DoctorsList").child(Global.doctorUID)
            .child("loginTime").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("DDM", "Data Changed")
                    if (snapshot.value != Global.loginTime){
                        val intent = Intent(this@HomePage, Login::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("DDM", "Error While Messaging")
                }
            })
    }
}