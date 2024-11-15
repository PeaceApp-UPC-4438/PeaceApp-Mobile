
package com.innovatech.peaceapp.StartingPoint

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var btnRecoverEmail: Button
    private lateinit var btnRecoverPhone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnRecoverEmail = findViewById(R.id.btn_recover_email)
        btnRecoverPhone = findViewById(R.id.btn_recover_phone)

        btnRecoverEmail.setOnClickListener {
            // Lógica para recuperar la cuenta por correo
            Toast.makeText(this, "Recuperar cuenta por correo", Toast.LENGTH_SHORT).show()
        }

        btnRecoverPhone.setOnClickListener {
            // Lógica para recuperar la cuenta por número telefónico
            Toast.makeText(this, "Recuperar cuenta por número telefónico", Toast.LENGTH_SHORT).show()
        }
    }
}