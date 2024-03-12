package com.rick.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var signin : Button
    lateinit var signup : Button
    lateinit var email : EditText
    lateinit var password : EditText

    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        findViews()

        signup.setOnClickListener {
//            FirebaseAuth.getInstance()
            val sEmail = email.text.toString()
            val sPassword = password.text.toString()
            auth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("Sign up")
                            .setMessage("Account created")
                            .setPositiveButton("ok") { dialog, which ->
                                setResult(RESULT_OK)
                                finish()
                            } .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Sign up")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("ok", null)
                            .show()
                    }
                }
        }

        signin.setOnClickListener {
            val sEmail = email.text.toString()
            val sPassword = password.text.toString()
            auth.signInWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("Sign in")
                            .setMessage("Account signined")
                            .setPositiveButton("ok") { dialog, which ->
                                setResult(RESULT_OK)
                                finish()
                            } .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Sign in")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("ok", null)
                            .show()
                    }
                }
        }
    }
    private fun findViews(){
        signin = findViewById(R.id.signin)
        signup = findViewById(R.id.signup)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
    }
}