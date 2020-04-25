package com.example.applicationwireless

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signIn: Button
    lateinit var signUp: TextView
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        signIn = findViewById(R.id.signin)
        signUp = findViewById(R.id.signup)
        setUI()
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(Intent(this, MainActivity.setFirebaseUser(currentUser)))
        }
    }
    fun setUI(){
        signIn.setOnClickListener {
            val email = this.email.text.toString()
            val pwd = this.password.text.toString()
            if(email.isEmpty() || pwd.isEmpty()){
                if(email.isEmpty()){
                    this.email?.error = "Enter email"
                    this.email?.requestFocus()
                }
                if(pwd.isEmpty()){
                    this.password.error = "Enter password"
                    this.password.requestFocus()
                }
            }
            else{
                signIn(email, pwd)
            }
        }
        signUp.setOnClickListener {
            updateUI(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun updateUI(intent: Intent){
        startActivity(intent)
    }
    fun signIn(email: String, pwd: String){
        auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) {task->
            if(task.isSuccessful){
                Log.d("SIGN IN", "signInWithEmail:success")
                val user = auth.currentUser
                updateUI(Intent(this, user?.let { MainActivity.setFirebaseUser(it) }))
            }
            else{
                Log.w("SIGN IN", "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Wrong email or password",
                        Toast.LENGTH_SHORT).show()

            }
        }
    }
}