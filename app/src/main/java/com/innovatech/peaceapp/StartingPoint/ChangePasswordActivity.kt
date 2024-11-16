package com.innovatech.peaceapp.StartingPoint

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.R

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var edtPassword_1: EditText
    private lateinit var ivEye_1: ImageView

    private lateinit var edtPassword_2: EditText
    private lateinit var ivEye_2: ImageView

    private lateinit var btnRecovery: Button

    private var passwordFieldSelected_1 = false
    private var passwordFieldSelected_2 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initComponents()
        highlightInputOnFocus()

        ivEye_1.setOnClickListener{
            changePasswordVisibility_1()
        }
        ivEye_2.setOnClickListener{
            changePasswordVisibility_2()
        }

        btnRecovery.setOnClickListener {

        }

    }

    private fun initComponents() {
        edtPassword_1 = findViewById(R.id.et_password_1)
        ivEye_1 = findViewById(R.id.iv_eye_1)

        edtPassword_2 = findViewById(R.id.et_password_2)
        ivEye_2 = findViewById(R.id.iv_eye_2)

        btnRecovery = findViewById(R.id.btn_recovery)
    }

    private fun highlightInputOnFocus() {
        edtPassword_1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordFieldSelected_1 = true
                edtPassword_1.setBackgroundResource(R.drawable.auth_input_focused)
                edtPassword_1.setHintTextColor(resources.getColor(R.color.input_focused_stroke))
                ivEye_1.drawable.setTint(resources.getColor(R.color.input_focused_stroke))
            } else {
                passwordFieldSelected_1 = false
                edtPassword_1.setBackgroundResource(R.drawable.auth_input)
                edtPassword_1.setHintTextColor(resources.getColor(R.color.input_stroke))
                ivEye_1.drawable.setTint(resources.getColor(R.color.input_stroke))
            }
        }

        edtPassword_2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordFieldSelected_2 = true
                edtPassword_2.setBackgroundResource(R.drawable.auth_input_focused)
                edtPassword_2.setHintTextColor(resources.getColor(R.color.input_focused_stroke))
                ivEye_2.drawable.setTint(resources.getColor(R.color.input_focused_stroke))
            } else {
                passwordFieldSelected_2 = false
                edtPassword_2.setBackgroundResource(R.drawable.auth_input)
                edtPassword_2.setHintTextColor(resources.getColor(R.color.input_stroke))
                ivEye_2.drawable.setTint(resources.getColor(R.color.input_stroke))
            }
        }

    }

    private fun changePasswordVisibility_1(){
        if(edtPassword_1.inputType == 129){
            edtPassword_1.inputType = 1
            // change the icon
            ivEye_1.setImageResource(R.drawable.ic_closed_eye)
        }else{
            edtPassword_1.inputType = 129
            ivEye_1.setImageResource(R.drawable.ic_open_eye)
        }
        if(passwordFieldSelected_1){
            ivEye_1.drawable.setTint(resources.getColor(R.color.input_focused_stroke))
        }else{
            ivEye_1.drawable.setTint(resources.getColor(R.color.input_stroke))
        }

    }

    private fun changePasswordVisibility_2(){
        if(edtPassword_2.inputType == 129){
            edtPassword_2.inputType = 1
            // change the icon
            ivEye_2.setImageResource(R.drawable.ic_closed_eye)
        }else{
            edtPassword_2.inputType = 129
            ivEye_2.setImageResource(R.drawable.ic_open_eye)
        }
        if(passwordFieldSelected_2){
            ivEye_2.drawable.setTint(resources.getColor(R.color.input_focused_stroke))
        }else{
            ivEye_2.drawable.setTint(resources.getColor(R.color.input_stroke))
        }
    }

}