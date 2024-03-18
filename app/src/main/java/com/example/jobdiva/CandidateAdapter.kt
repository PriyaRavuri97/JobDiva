package com.example.jobdiva

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class CandidateAdapter(options: FirebaseRecyclerOptions<Candidate>)
    : FirebaseRecyclerAdapter<Candidate, CandidateAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val companyTextView: TextView = itemView.findViewById(R.id.companyTextView)
        val workExperienceTextView: TextView = itemView.findViewById(R.id.workExperienceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.candidate_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Candidate) {
        // Load candidate's photo into ImageView using Glide
        Glide.with(holder.photoImageView.context)
            .load(model.PhotoUrl) // Load image from the provided URL
            .into(holder.photoImageView)

        // Bind other candidate information to TextViews
        holder.nameTextView.text = model.Name
        holder.companyTextView.text = model.Company
        holder.workExperienceTextView.text = model.Work_experience


        holder.cardView.setOnClickListener {
            // Navigate to DetailActivity and pass the candidate's id as extra
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("candidate_id", getRef(position).key)
            holder.itemView.context.startActivity(intent)
        }
    }
}
