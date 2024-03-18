package com.example.jobdiva

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Set click listener for login button
        btnLogin.setOnClickListener {
            // Here you would typically perform login/authentication logic
            // For simplicity, we'll just navigate to MainActivity for now
            navigateToMainActivity()
        }

        // Set click listener for register text
        findViewById<TextView>(R.id.textViewRegister).setOnClickListener {
            // Open registration activity
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun navigateToMainActivity() {
        // Create an Intent to start the MainActivity
        val intent = Intent(this, MainActivity::class.java)

        // Optionally, you can pass any extra data to the MainActivity using Intent.putExtra()
        // For example, if you want to pass the user's email:
        // intent.putExtra("user_email", editTextEmail.text.toString())

        // Start the MainActivity
        startActivity(intent)

        // Finish the LoginActivity to prevent the user from going back
        finish()
    }
}
