package com.example.applicationwireless

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.applicationwireless.database.entity.User
import com.example.applicationwireless.model.UserModelFactory
import com.example.applicationwireless.model.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var email: EditText? = null
    private lateinit var password: EditText
    private lateinit var cfpassword: EditText
    lateinit var height: EditText
    lateinit var name: EditText
    private lateinit var weight: EditText
    private lateinit var signUp: Button
    private lateinit var signIn: TextView
    private lateinit var userModelFactory: UserModelFactory
    private val userModel: UserViewModel by viewModels { userModelFactory }
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.Email)
        password = findViewById(R.id.Password)
        cfpassword = findViewById(R.id.Confirmpassword);
        height = findViewById(R.id.Height);
        weight = findViewById(R.id.Weight);
        signUp = findViewById(R.id.signup)
        signIn = findViewById(R.id.signin)
        name = findViewById(R.id.Username)
        userModelFactory = Injection.provideUserModelFactory(this)
        setUpUI()
    }
    private fun setUpUI(){
        signUp.setOnClickListener {
            val email = this.email!!.text.toString()
            val pwd = this.password.text.toString()
            val cfPwd = this.cfpassword.text.toString()
            val weight = this.weight.text.toString()
            val height = this.height.text.toString()
            val name = this.name.text.toString()
            if(email.isEmpty() || pwd.isEmpty()){
                if(email.isEmpty()){
                    this.email?.error = "Enter email"
                    this.email?.requestFocus()
                }
                if(pwd.isEmpty()){
                    this.password.error = "Enter password"
                    this.password.requestFocus()
                }
                else if(pwd.length<6){
                    this.password.error = "Password must include at least 6 characters"
                    this.password.requestFocus()
                }
            }
            else{
                if(pwd != cfPwd){
                    this.cfpassword.error = "Password does not match"
                    this.cfpassword.requestFocus()
                }
                else{
                    if(name.isEmpty()||weight.isEmpty()||height.isEmpty()){
                        Toast.makeText(this, "Please fill all field", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registration(name, pwd, weight.toDouble(), height.toDouble(), email)
                    }
                }
            }
        }
        signIn.setOnClickListener {
            updateUI(Intent(this, LoginActivity::class.java))
        }
    }
    private fun registration(name: String, pwd: String, weight: Double, height: Double, email: String){
        Log.d("REGISTRATION", "Success")
        auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) {task->
                    if(task.isSuccessful){
                        Log.d("Registration", "createUserWithEmail:success")
                        val currentUser = auth.currentUser
                        val user = User(email, name, pwd, weight, height)
                        disposable.add(userModel.insertUser(user)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe{
                                    updateUI(Intent(this, currentUser?.let { MainActivity.setFirebaseUser(it) }))
                                })
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w("Registration", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }

                }
    }
    private fun updateUI(intent: Intent){
        startActivity(intent)
    }
}