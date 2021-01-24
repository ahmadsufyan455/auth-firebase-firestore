package com.fyn.loginregisterapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fyn.loginregisterapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            userLogin()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userLogin() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.edEmail.error = "this field is required"
                binding.edEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edPassword.error = "this field is required"
                binding.edPassword.requestFocus()
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogin.visibility = View.INVISIBLE
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // sign in success, move to home activity
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "You're logged in", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finishAffinity()
                        } else {
                            binding.progressBar.visibility = View.GONE
                            binding.btnLogin.visibility = View.VISIBLE
                            Toast.makeText(this, "your email or password wrong!", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }

        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        }
    }
}