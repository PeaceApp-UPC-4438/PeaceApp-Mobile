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
import com.innovatech.peaceapp.GlobalToken
import com.innovatech.peaceapp.GlobalUserEmail
import com.innovatech.peaceapp.Map.MapActivity
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.StartingPoint.Beans.User
import com.innovatech.peaceapp.StartingPoint.Beans.UserAuth
import com.innovatech.peaceapp.StartingPoint.Beans.UserAuthenticated
import com.innovatech.peaceapp.StartingPoint.Models.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSignIn = findViewById<Button>(R.id.btn_signin)
        val btnSignUp = findViewById<TextView>(R.id.tv_create_account)

        btnSignIn.setOnClickListener {
            val edtEmail = findViewById<TextView>(R.id.et_email)
            val edtPassword = findViewById<TextView>(R.id.et_password)

            signIn(edtEmail.text.toString(), edtPassword.text.toString())
        }

        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    fun signIn(email:String, password:String){
        val service = RetrofitClient.placeHolder

        val user = UserAuth(email, password)
        service.signIn(user).enqueue(object : Callback<UserAuthenticated> {
            override fun onResponse(p0: Call<UserAuthenticated>, response: Response<UserAuthenticated>) {
                if(response.isSuccessful){
                    val user = response.body()
                    if(user?.username != null){
                        val intent = Intent(this@SignInActivity, MapActivity::class.java)
                        val token = user.token

                        GlobalToken.setToken(token)
                        GlobalUserEmail.setEmail(email)

                        intent.putExtra("token", token)
                        startActivity(intent)
                    }else {
                        Toast.makeText(this@SignInActivity, "${user!!.message}", Toast.LENGTH_LONG).show()
                        Log.e("Mensaje", user!!.message)
                    }
                }
            }
            override fun onFailure(p0: Call<UserAuthenticated>, p1: Throwable) {
                Toast.makeText(this@SignInActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                Log.e("ERROR", p1.message.toString())
            }
        })

    }
}