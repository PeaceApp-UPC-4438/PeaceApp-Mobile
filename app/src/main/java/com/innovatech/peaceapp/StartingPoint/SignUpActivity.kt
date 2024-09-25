package com.innovatech.peaceapp.StartingPoint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.StartingPoint.Beans.User
import com.innovatech.peaceapp.StartingPoint.Beans.UserSchema
import com.innovatech.peaceapp.StartingPoint.Models.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSignUp = findViewById<Button>(R.id.btn_signup)
        val btnSignIn = findViewById<TextView>(R.id.tv_log_in)

        btnSignUp.setOnClickListener {
            val edtEmail = findViewById<TextView>(R.id.et_email)
            val edtPassword = findViewById<TextView>(R.id.et_password)

            saveUserCredentials(edtEmail.text.toString(), edtPassword.text.toString())

            //val intent = Intent(this, ACTIVIDAD-DEL-MAP::class.java)
            //startActivity(intent)
        }

        btnSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveUserCredentials(email:String, password:String){
        val service = RetrofitClient.placeHolder

        var roles = listOf("ROLE_USER")

        val user = UserSchema(email, password, roles)

        service.signUp(user).enqueue(object : Callback<User> {
            override fun onResponse(p0: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
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
}