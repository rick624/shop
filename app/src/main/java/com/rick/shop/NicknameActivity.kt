package com.rick.shop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class NicknameActivity : AppCompatActivity() {
    lateinit var done : Button
    lateinit var nick : EditText

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseDatabase.getInstance("https://shop-69b3d-default-rtdb.asia-southeast1.firebasedatabase.app")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        findViews()

        if (auth.currentUser != null) {
//            FirebaseDatabase.getInstance("https://shop-69b3d-default-rtdb.asia-southeast1.firebasedatabase.app")
                db.getReference("users")
                .child(auth.currentUser!!.uid)
                .child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nick.setText(snapshot.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }

        done.setOnClickListener {
//呼叫Extensions中的setNickname方法把使用者輸入的暱稱存入SharedPreferences中
            setNickname(nick.text.toString())
//Save nickname to Firebase realtime database
//            FirebaseDatabase.getInstance("https://shop-69b3d-default-rtdb.asia-southeast1.firebasedatabase.app")
                db.getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("nickname")
                .setValue(nick.text.toString())
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun findViews(){
        done = findViewById(R.id.done)
        nick = findViewById(R.id.nick)
    }
}