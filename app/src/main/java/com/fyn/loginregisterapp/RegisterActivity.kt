package com.fyn.loginregisterapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fyn.loginregisterapp.data.User
import com.fyn.loginregisterapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.icBack.setOnClickListener { finish() }
    }

    private fun registerUser() {
        val username = binding.edUsername.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        // check validation
        when {
            username.isEmpty() -> {
                binding.edUsername.error = "this field is required"
                binding.edUsername.requestFocus()
            }
            email.isEmpty() -> {
                binding.edEmail.error = "this field is required"
                binding.edEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.edPassword.error = "this field is required"
                binding.edPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.edPassword.error = "password at least 6 character"
                binding.edPassword.requestFocus()
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnRegister.visibility = View.INVISIBLE
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // register success, store data
                            val user = User(username, email)
                            db.collection("users")
                                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d(
                                        "RegisterActivity",
                                        "DocumentSnapshot successfully written!"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w("RegisterActivity", "Error writing document", e)
                                }
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Register successfully!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            Toast.makeText(this, "Register failed!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}