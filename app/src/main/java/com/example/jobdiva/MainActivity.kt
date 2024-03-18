package com.example.jobdiva

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var btnSignOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize FirebaseDatabase
        database = FirebaseDatabase.getInstance()

        // Initialize sign-out button
        btnSignOut = findViewById(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            signOut()
        }

        // Fetch and display posts for connected candidates
        fetchAndDisplayPosts()

        // Set onClickListener for the "View Candidates" button
        val viewCandidatesButton: Button = findViewById(R.id.btnNavigateCandidateActivity)
        viewCandidatesButton.setOnClickListener {
            // Navigate to Candidate Activity
            startActivity(Intent(this, CandidateActivity::class.java))
        }
    }

    private fun fetchAndDisplayPosts() {
        Log.d("MainActivity", "Fetching posts for connected candidates...")

        // Reference to the "Connections" node to fetch connected candidates
        val connectionsRef = database.reference.child("Connections")

        // Query to fetch posts for connected candidates
        connectionsRef.orderByChild("connected").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("MainActivity", "onDataChange: Fetched connected candidates")

                    val connectedCandidateIds = mutableListOf<String>()

                    // Iterate through connected candidates and collect their IDs
                    for (data in snapshot.children) {
                        val candidateId = data.key
                        if (candidateId != null) {
                            connectedCandidateIds.add(candidateId)
                        }
                    }

                    Log.d("MainActivity", "Connected Candidate IDs: $connectedCandidateIds")

                    // Check if there are any connected candidates
                    if (connectedCandidateIds.isNotEmpty()) {
                        // Query posts for connected candidates
                        val query = database.reference.child("Posts")
                            .orderByChild("CandidateId")
                            .startAt(connectedCandidateIds[0])
                            .endAt(connectedCandidateIds[connectedCandidateIds.size - 1] + "\uf8ff")

                        Log.d("MainActivity", "Querying posts for connected candidates")

                        // FirebaseRecyclerOptions to configure how the adapter interacts with the database
                        val options = FirebaseRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post::class.java)
                            .build()

                        // Initialize adapter with options
                        adapter = PostAdapter(options, connectedCandidateIds)

                        // Set adapter to RecyclerView
                        recyclerView.adapter = adapter

                        // Start listening for changes in the database
                        adapter.startListening()

                        Log.d("MainActivity", "Posts fetched and displayed for connected candidates")
                    } else {
                        // Handle case when there are no connected candidates
                        Log.d("MainActivity", "No connected candidates found")
                        // You can display a message or handle the situation accordingly
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("MainActivity", "Error loading connected candidates: $error")
                }
            })
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart() called")
        // Start listening for changes in the adapter when the activity starts
        if (::adapter.isInitialized && adapter != null) {
            adapter.startListening()
            Log.d("MainActivity", "Adapter started listening")
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop() called")
        // Stop listening for changes in the adapter when the activity stops
        if (::adapter.isInitialized) {
            adapter.stopListening()
            Log.d("MainActivity", "Adapter stopped listening")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun signOut() {
        // Sign out the current user using Firebase Auth
        FirebaseAuth.getInstance().signOut()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish MainActivity to prevent user from returning back
    }
}
