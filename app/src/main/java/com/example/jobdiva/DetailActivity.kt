package com.example.jobdiva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var companyTextView: TextView
    private lateinit var educationTextView: TextView
    private lateinit var jobroleTextView: TextView
    private lateinit var workExperienceTextView: TextView
    private lateinit var largeImageView: ImageView
    private lateinit var connectButton: Button
    private lateinit var candidateId: String
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView)
        companyTextView = findViewById(R.id.companyTextView)
        educationTextView = findViewById(R.id.educationTextView)
        jobroleTextView = findViewById(R.id.jobroleTextView)
        workExperienceTextView = findViewById(R.id.workExperienceTextView)
        largeImageView = findViewById(R.id.largeImageView)
        connectButton = findViewById(R.id.connectButton)

        // Get the candidate ID from the intent extras
        candidateId = intent.getStringExtra("candidate_id") ?: ""

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance()

        // Load candidate details from Firebase database
        loadCandidateDetails()

        // Load connection status from Firebase and update UI
        loadConnectionStatus()

        // Set onClickListener for the "Connect" button
        connectButton.setOnClickListener {
            // Toggle the text of the button between "Connect" and "Connected"
            if (connectButton.text == "Connect") {
                connectButton.text = "Connected"
                updateConnectionStatus(true)
            } else {
                connectButton.text = "Connect"
                updateConnectionStatus(false)
            }
        }

        // Set onClickListener for the "Go to Main Page" button
        val goToMainPageButton: Button = findViewById(R.id.goToMainActivityButton)
        goToMainPageButton.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun loadCandidateDetails() {
        val candidateRef = database.reference.child("Candidates").child(candidateId)

        // Listen for changes in the candidate data
        candidateRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val candidate = snapshot.getValue(Candidate::class.java)
                    candidate?.let {
                        // Set candidate details to views
                        nameTextView.text = candidate.Name
                        companyTextView.text = candidate.Company
                        educationTextView.text = candidate.Education
                        jobroleTextView.text = candidate.Jobrole
                        workExperienceTextView.text = candidate.Work_experience

                        // Load candidate photo using Glide
                        Glide.with(this@DetailActivity)
                            .load(candidate.PhotoUrl)
                            .into(largeImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("DetailActivity", "Error loading candidate details: $error")
            }
        })
    }

    private fun loadConnectionStatus() {
        val connectionsRef = database.reference.child("Connections").child(candidateId)

        // Retrieve connection status from Firebase
        connectionsRef.child("connected")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connected = snapshot.getValue(Boolean::class.java) ?: false
                    // Update UI based on connection status
                    if (connected) {
                        connectButton.text = "Connected"
                    } else {
                        connectButton.text = "Connect"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("DetailActivity", "Error loading connection status: $error")
                }
            })
    }

    private fun updateConnectionStatus(connected: Boolean) {
        val connectionsRef = database.reference.child("Connections").child(candidateId)

        // Update connection status in Firebase database
        connectionsRef.child("connected").setValue(connected)
            .addOnSuccessListener {
                Log.e("DetailActivity", "updated connection status: $it")
                // Broadcast connection status change
                val intent = Intent("connection_status_changed")
                intent.putExtra("candidate_id", candidateId)
                intent.putExtra("connected", connected)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            .addOnFailureListener {
                // Handle failure
                Log.e("DetailActivity", "Failed to update connection status: $it")
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("candidate_id", candidateId)
        startActivity(intent)
    }
}
