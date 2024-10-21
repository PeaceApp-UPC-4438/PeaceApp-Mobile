package com.innovatech.peaceapp.Profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.GlobalToken
import com.innovatech.peaceapp.GlobalUserEmail
import com.innovatech.peaceapp.Profile.Beans.UserProfile
import com.innovatech.peaceapp.Profile.Models.RetrofitClient
import com.innovatech.peaceapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSave: Button

    private lateinit var token: String
    private lateinit var email: String
    private var user: UserProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.txt_user_name)
        etLastName = findViewById(R.id.txt_user_lastname)
        etEmail = findViewById(R.id.txt_user_email)
        etPhone = findViewById(R.id.txt_user_phone)
        etPassword = findViewById(R.id.txt_user_password)
        btnSave = findViewById(R.id.btn_save_Profile)

        token = GlobalToken.token
        email = GlobalUserEmail.email

        loadUserData()

        btnSave.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        val service = RetrofitClient.getClient(token)

        service.getUserByEmail(email).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        etName.setText(userProfile.name)
                        etLastName.setText(userProfile.lastname)
                        etEmail.setText(userProfile.email)
                        etPhone.setText(userProfile.phonenumber)
                        etPassword.setText(userProfile.password)

                        user = userProfile
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData() {
        val service = RetrofitClient.getClient(token)

        user?.let {
            it.name = etName.text.toString()
            it.lastname = etLastName.text.toString()
            it.email = etEmail.text.toString()
            it.phonenumber = etPhone.text.toString()
            it.password = etPassword.text.toString()

            service.updateUser(it.id, it).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@EditProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } ?: run {
            Toast.makeText(this, "User data is not loaded", Toast.LENGTH_SHORT).show()
        }
    }
}