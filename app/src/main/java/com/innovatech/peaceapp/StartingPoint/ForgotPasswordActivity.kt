package com.innovatech.peaceapp.StartingPoint

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.GlobalToken
import com.innovatech.peaceapp.GlobalUserEmail
import com.innovatech.peaceapp.Map.MapActivity
import com.innovatech.peaceapp.StartingPoint.Models.RetrofitClient
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.StartingPoint.Beans.UserAuth
import com.innovatech.peaceapp.StartingPoint.Beans.UserAuthenticated
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnRecoverEmail: Button
    private lateinit var btnRecoverPhone: Button
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var code: String
    private val smsManager = SmsManager.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val etEmail = findViewById<EditText>(R.id.et_email)

        btnRecoverEmail = findViewById(R.id.btn_recovery_email)
        btnRecoverPhone = findViewById(R.id.btn_recovery_sms)
        password = "owo"

        btnRecoverEmail.setOnClickListener {
            email = etEmail.text.toString()
            if (email.isNotEmpty()) {
                val service = RetrofitClient.placeHolder
                val user = UserAuth(email, password)

                service.signIn(user).enqueue(object : Callback<UserAuthenticated> {
                    override fun onResponse(p0: Call<UserAuthenticated>, response: Response<UserAuthenticated>) {
                        if(response.isSuccessful){
                            val user = response.body()
                            if(user?.username != null){
                                val token = user.token

                                val sharedPref = getSharedPreferences("GlobalPrefs", MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putInt("userId", user.id)
                                    apply()
                                }

                                GlobalToken.setToken(token)
                                GlobalUserEmail.setEmail(email)

                                intent.putExtra("token", token)

                                code = generateRandomCode()
                                val verificationMessage = "Codigo de seguridad: $code"
                                smsManager.sendTextMessage("8080", null, verificationMessage, null, null)
                                showSendCodeDialog(token)


                            }else {
                                Log.e("Mensaje", user!!.message)
                                // mostrar el dialog de error
                                when(user.message){
                                    "User not found" -> showIncorrectSignInDialog("No existe un usuario " +
                                            "con este correo")
                                    else -> showIncorrectSignInDialog("Error desconocido")
                                }

                            }
                        }
                    }
                    override fun onFailure(p0: Call<UserAuthenticated>, p1: Throwable) {
                        Toast.makeText(this@ForgotPasswordActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                        Log.e("ERROR", p1.message.toString())
                    }
                })
            }
        }


        btnRecoverPhone.setOnClickListener {
            email = etEmail.text.toString()
            if (email.isNotEmpty()) {
                val service = RetrofitClient.placeHolder
                val user = UserAuth(email, password)

                service.signIn(user).enqueue(object : Callback<UserAuthenticated> {
                    override fun onResponse(p0: Call<UserAuthenticated>, response: Response<UserAuthenticated>) {
                        if(response.isSuccessful){
                            val user = response.body()
                            if(user?.username != null){

                                val token = user.token

                                val sharedPref = getSharedPreferences("GlobalPrefs", MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putInt("userId", user.id)
                                    apply()
                                }

                                GlobalToken.setToken(token)
                                GlobalUserEmail.setEmail(email)

                                intent.putExtra("token", token)

                                code = generateRandomCode()
                                val verificationMessage = "Codigo de seguridad: $code"
                                smsManager.sendTextMessage("8080", null, verificationMessage, null, null)
                                showSendCodeDialog(token)


                            }else {
                                Log.e("Mensaje", user!!.message)
                                // mostrar el dialog de error
                                when(user.message){
                                    "User not found" -> showIncorrectSignInDialog("No existe un usuario " +
                                            "con este correo")
                                    else -> showIncorrectSignInDialog("Error desconocido")
                                }

                            }
                        }
                    }
                    override fun onFailure(p0: Call<UserAuthenticated>, p1: Throwable) {
                        Toast.makeText(this@ForgotPasswordActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                        Log.e("ERROR", p1.message.toString())
                    }
                })
            }
        }
    }

    private fun generateRandomCode(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun navigateToChangePasswordActivity(token: String) {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    private fun showSendCodeDialog(token: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_send_recovery_password_code)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val btnVerifyCode = dialog.findViewById<Button>(R.id.btnVerifyCode)
        val etCode = dialog.findViewById<EditText>(R.id.et_code)

        btnVerifyCode.setOnClickListener {
            val enteredCode = etCode.text.toString()
            if (enteredCode == code) {
                dialog.dismiss()
                navigateToChangePasswordActivity(token)
            } else {
                Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun showIncorrectSignInDialog(mensaje: String){
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_incorrect_signup)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)
        val tvMensaje = dialog.findViewById<TextView>(R.id.tvIncorrectSignup)

        tvMensaje.text = mensaje

        btnContinue.setOnClickListener {
            dialog.hide()
        }

        dialog.show()
    }
}