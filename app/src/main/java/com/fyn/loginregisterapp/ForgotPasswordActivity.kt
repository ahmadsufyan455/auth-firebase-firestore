package com.fyn.loginregisterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fyn.loginregisterapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnReset.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.edEmail.text.toString()

        if (email.isEmpty()) {
            binding.edEmail.error = "Email is required"
            binding.edEmail.requestFocus()
        } else {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    Toast.makeText(this, "Check your email address", Toast.LENGTH_LONG).show()
                }
        }
    }
}