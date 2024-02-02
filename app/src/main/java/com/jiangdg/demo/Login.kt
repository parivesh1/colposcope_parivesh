package com.jiangdg.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login.sign_in_button_doc

class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseRef_Info: DatabaseReference? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var role: String? = null
    private val RC_SIGN_IN = 1
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        mProgressBar = findViewById(R.id.progressBar)
        mAuth = FirebaseAuth.getInstance()
        mDatabaseRef_Info = FirebaseDatabase.getInstance().reference.child("EmailsList")

        //This API allows the user to signIn using google authentication service

        //This API allows the user to signIn using google authentication service
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) //                .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        sign_in_button_doc.setOnClickListener {
            setValue("doctor")
            signIn()
        }
    }

    fun setValue(s: String) {
        role = s
        Log.d(role, "Role selected")
    }

    // changes made here: this f(x) did not exist
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN) // this function calls 'onActivityResult'
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            Toast.makeText(this@Login,"Function entered"+ "onActivityResult", Toast.LENGTH_SHORT).show()
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
//            Toast.makeText(this@Login,
//                "Function entered"+ "handleSignInResult",Toast.LENGTH_SHORT).show()
            //Changes Made above

            // Google Sign In was successful, authenticate with Firebase
            val acc = completedTask.getResult(ApiException::class.java)
//            Toast.makeText(this@Login,"Sign In successful"+"Getting Token", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAut(acc)
//            Toast.makeText(this@Login,role+"FirebaseGoogleAut done", Toast.LENGTH_SHORT).show()
            mProgressBar?.visibility = View.VISIBLE
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Toast.makeText(
                this@Login,
                "Try again later!" + e.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
            //            FirebaseGoogleAut(null);
            mProgressBar?.visibility = View.GONE
        }
    }

    private fun FirebaseGoogleAut(acct: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(authCredential).addOnCompleteListener(
            this
        ) { task ->

            if (task.isSuccessful) {
                mProgressBar?.visibility = View.VISIBLE
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(
                    this@Login,
                    "Successful",
                    Toast.LENGTH_SHORT
                ).show()
                //                    FirebaseUser user = mAuth.getCurrentUser();
//                Global.user = mAuth.currentUser
                Log.d("Function about to enter", "updateUI")
                updateUI(mAuth!!.currentUser)
            } else {
                mProgressBar?.visibility = View.GONE
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this@Login,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI(null)
            }
        }
    }

    private fun updateUI(fUser: FirebaseUser?) {
//        btnSignOut.setVisibility(View.VISIBLE);
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        println("fuser" + fUser.toString())
        if (account != null) {
            val personName = account.displayName
            val personEmail = account.email
            Global.doctorUID = fUser!!.uid
//            Log.d("UID", Global.doctorUID)
            if (personName != null) {
                println("Name$personName")
            }
            if (personEmail != null) {
                println("Email$personEmail")
            }
            // if the user has already signed in then do the following:
            // Firstly we check if the user "email" exists in the database under any of the parent keys. If it does, 'onDataChange' is executed, else 'onCancelled' is executed
            mDatabaseRef_Info!!.orderByChild("email").equalTo(personEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) { //Database exists for this old user
                            //if database already has entry of user, move to oldUser activity to show further options
                            for (childSnapshot in dataSnapshot.children) {
                                // String uid = childSnapshot.getKey();
                                Log.d("childSnapshot", childSnapshot.toString())
                                Log.d("childSnapshot values", childSnapshot.value.toString())
                                val list: EmailsList? =
                                    childSnapshot.getValue(EmailsList::class.java)
                                Log.d("EmailsList object", list.toString())

                                mProgressBar!!.visibility = View.GONE
                                if (list != null) {
                                    if (list.getRole().equals("doctor")) {
                                        var mDatabaseRef: DatabaseReference
                                        mDatabaseRef =
                                            FirebaseDatabase.getInstance()
                                                .getReference("DoctorsList")
                                                .child(Global.doctorUID).child("key")
                                        mDatabaseRef.addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                println(snapshot.value)
                                                if (snapshot.value == null) {
                                                    val intent: Intent = Intent(
                                                        this@Login,
                                                        KeyEnter::class.java
                                                    )
                                                    startActivity(intent)
                                                } else {
                                                    Global.loginTime =
                                                        System.currentTimeMillis().toString()
                                                    var mDatabaseRef: DatabaseReference
                                                    mDatabaseRef =
                                                        FirebaseDatabase.getInstance()
                                                            .getReference("DoctorsList")
                                                            .child(Global.doctorUID)
                                                            .child("loginTime")
                                                    mDatabaseRef.setValue(Global.loginTime)
                                                    val intent: Intent = Intent(
                                                        this@Login,
                                                        HomePage::class.java
                                                    )
                                                    startActivity(intent)
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                val intent: Intent = Intent(
                                                    this@Login,
                                                    KeyEnter::class.java
                                                )
                                                startActivity(intent)
                                            }
                                        })
                                        //                                final Intent intent = new Intent(MainActivity.this,homeScreen.class);
                                        //                                startActivity(intent);
                                    } else {
                                        //                                    val intent: Intent = Intent(
                                        //                                        this@Login,
                                        //                                        oldUserPatient::class.java
                                        //                                    )
                                        //                                    startActivity(intent)
                                    }
                                }
                            }
                        } else { //if Database doesn't exist for this user,
                            //means that database has no entry of the user yet, make entry (name and email) and move to 'newUser' activity to update profile
                            mProgressBar!!.visibility = View.GONE
                            //String key = mDatabaseRef_Info.push().getKey();
                            val key: String = Global.doctorUID
                            Log.d("key added", key)
                            mDatabaseRef_Info!!.child(key).child("Name")
                                .setValue(personName) // set the child value for 'Name'
                            mDatabaseRef_Info!!.child(key).child("email")
                                .setValue(personEmail) // set the child value for 'email'
                            mDatabaseRef_Info!!.child(key).child("role").setValue(role)
                            if (role == "doctor") {
                                // if the user is registering as a doctor
                                val intent: Intent = Intent(
                                    this@Login,
                                    DoctorDetails::class.java
                                ) // via intent component, move to the next class called 'newUser'
                                startActivity(intent)
                            } else {
                                // if the user id registering as a patient
//                                val intent: Intent = Intent(
//                                    this@Login,
//                                    newUserPatient::class.java
//                                ) // via intent component, move to the next class called 'newUserPatient'
//                                startActivity(intent)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("Error", "Some Database issue")
                    }
                })
        }
    }
}