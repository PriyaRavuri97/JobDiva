package com.example.jobdiva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var candidateId: String
    private lateinit var adapter: CandidateAdapter
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        // Retrieve candidate ID from intent extras
        candidateId = intent.getStringExtra("candidate_id") ?: ""
        Log.e("can1", "can1$candidateId")

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCandidates)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize FirebaseDatabase
        database = FirebaseDatabase.getInstance()

        // Set up FirebaseRecyclerOptions to configure how the adapter interacts with the database
        val options = FirebaseRecyclerOptions.Builder<Candidate>()
            .setQuery(database.reference.child("Candidates"), Candidate::class.java)
            .build()

        // Initialize adapter with options
        adapter = CandidateAdapter(options)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        // Start listening for changes in the database when the activity starts
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        // Stop listening for changes in the database when the activity stops
        adapter.stopListening()
    }

    // Function to handle the "Go Back" button click
    fun goBackToMainActivity(view: View) {
        // Create an intent to navigate back to the MainActivity
        val intent = Intent(this, MainActivity::class.java)
        // Add flags to clear the back stack and start the MainActivity as a new task
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        // Start the MainActivity
        startActivity(intent)
    }
}
