package com.example.jobdiva

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        // Set click listener for register button
        btnRegister.setOnClickListener {
            // Handle registration logic
            registerUser()
        }

        // Set click listener for login text
        findViewById<TextView>(R.id.textViewLogin).setOnClickListener {
            // Navigate back to login activity
            finish()
        }
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()

        // Validate input fields
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }

        if (password != confirmPassword) {
            showToast("Passwords do not match")
            return
        }

        // Register user with Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration success
                    showToast("Registered successfully!")
                    // Navigate to the next activity, or perform any other desired action
                } else {
                    // Registration failed
                    val errorMessage = task.exception?.message ?: "Registration failed"
                    showToast(errorMessage)
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
