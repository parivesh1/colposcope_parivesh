package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.key_enter.toolbar
import kotlinx.android.synthetic.main.key_enter.verifyKey

class KeyEnter : AppCompatActivity(){

    private var key: TextView? = null
    private var mDatabaseRef: DatabaseReference? = null
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference
    var keyPresent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.key_enter)
        toolbar.title = "Handy-Colposcopy"

        key = findViewById(R.id.keyView)

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoctorsList")
        val textFileRef = storageRef.child("random_strings.txt")


        key!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (uniqueKeySnapshot in snapshot.children) {
                            mDatabaseRef!!.child(uniqueKeySnapshot.key!!).child("key")
                                .addValueEventListener(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.value != null) {
                                                println(snapshot.value.toString())
                                                println(key!!.text.toString())
                                                if (snapshot.value.toString()
                                                        .trim { it <= ' ' } == key!!.text
                                                        .toString().trim { it <= ' ' }
                                                ) {
                                                    Toast.makeText(
                                                        this@KeyEnter,
                                                        "Key Already Used",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    keyPresent = true
                                                    return
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        verifyKey.setOnClickListener {
            println(keyPresent)
            if (key!!.text.length != 12) {
                Toast.makeText(this@KeyEnter, "Enter Valid key", Toast.LENGTH_SHORT).show()
            } else {
                if (!keyPresent) {
                    textFileRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                        val fileContent = String(bytes!!)
                        if (fileContent.contains(key!!.text.toString().trim { it <= ' ' })) {
                            // String is present in the file
                            println("Found")
                            mDatabaseRef!!.child(Global.doctorUID).child("key")
                                .setValue(key!!.text.toString().trim { it <= ' ' })
                            Toast.makeText(
                                this@KeyEnter,
                                "Key Registered Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this@KeyEnter, HomePage::class.java)
                            startActivity(intent)
                        } else {
                            // String is not present in the file
                            Toast.makeText(this@KeyEnter, "Invalided Key", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    key!!.text = ""
                    keyPresent = false
                }
            }
        }
    }
}