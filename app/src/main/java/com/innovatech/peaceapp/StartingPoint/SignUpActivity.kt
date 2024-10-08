package com.innovatech.peaceapp.StartingPoint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.innovatech.peaceapp.Profile.Beans.UserProfile
import com.innovatech.peaceapp.Profile.Beans.UserProfileSchema
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.StartingPoint.Beans.User
import com.innovatech.peaceapp.StartingPoint.Beans.UserSchema
import com.innovatech.peaceapp.StartingPoint.Models.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var etName:EditText
    private lateinit var etLastName:EditText
    private lateinit var etPhone:EditText

    private lateinit var btnSignUp:Button
    private lateinit var btnSignIn:TextView
    private lateinit var edtEmail:TextView
    private lateinit var edtPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()


        btnSignUp.setOnClickListener {

            saveUserCredentials()
            saveUser()
        }

        btnSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initComponents() {
        etName = findViewById(R.id.et_name)
        etLastName = findViewById(R.id.et_surname)
        etPhone = findViewById(R.id.et_phone)
        btnSignUp = findViewById(R.id.btn_signup)
        btnSignIn = findViewById(R.id.btn_signin)
        edtEmail = findViewById(R.id.et_email)
        edtPassword = findViewById(R.id.et_password)
    }

    private fun saveUserCredentials(){
        val service = RetrofitClient.placeHolder

        var roles = listOf("ROLE_USER")

        val user = UserSchema(
            edtEmail.text.toString(),
            edtPassword.text.toString(),
            roles)

        service.signUp(user).enqueue(object : Callback<User> {
            override fun onResponse(p0: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user?.username != null) {
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(p0: Call<User>, p1: Throwable) {
                Toast.makeText(this@SignUpActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                Log.e("Error", p1.message.toString())
            }
        })
    }

    private fun saveUser(){
        val serviceUserProfile = com.innovatech.peaceapp.Profile.Models.RetrofitClient.placeHolder

        val userProfile = UserProfileSchema(
            etName.text.toString(),
            etLastName.text.toString(),
            etPhone.text.toString(),
            edtEmail.text.toString(),
            edtPassword.text.toString()
            )

        serviceUserProfile.createUser(userProfile).enqueue(object : Callback<UserProfile> {
            override fun onResponse(p0: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile?.name != null) {
                        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(p0: Call<UserProfile>, p1: Throwable) {
                Toast.makeText(this@SignUpActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                Log.e("Error", p1.message.toString())
            }
        })
    }
}