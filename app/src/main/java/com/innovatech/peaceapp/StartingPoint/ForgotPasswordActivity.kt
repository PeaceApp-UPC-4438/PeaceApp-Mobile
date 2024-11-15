package com.innovatech.peaceapp.StartingPoint

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnRecoverPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.et_email)
        btnRecoverPassword = findViewById(R.id.btn_recover_password)

        btnRecoverPassword.setOnClickListener {
            val email = etEmail.text.toString()
            if (email.isNotEmpty()) {
                // Lógica para recuperar la contraseña
                recoverPassword(email)
            } else {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recoverPassword(email: String) {
        // Implementa la lógica para recuperar la contraseña aquí
        // Por ejemplo, puedes hacer una llamada a una API para enviar un correo de recuperación
        Toast.makeText(this, "Se ha enviado un correo de recuperación a $email", Toast.LENGTH_SHORT).show()
    }
}