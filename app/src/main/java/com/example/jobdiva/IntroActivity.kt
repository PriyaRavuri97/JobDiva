package com.example.jobdiva

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.FirebaseApp

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro) // Set the layout for IntroActivity

        // Firebase initialization
        FirebaseApp.initializeApp(this)

        // Delay execution for 3 seconds (3000 milliseconds) using Handler
        Handler().postDelayed({
            // After delay, start the MainActivity and finish the IntroActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
