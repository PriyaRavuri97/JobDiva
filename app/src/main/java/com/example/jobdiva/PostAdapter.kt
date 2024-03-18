package com.example.jobdiva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class PostAdapter(options: FirebaseRecyclerOptions<Post>, private val connectedCandidateIds: List<String>) :
    FirebaseRecyclerAdapter<Post, PostAdapter.MyViewHolder>(options) {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.TitleTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_post, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Post) {
        val candidateId = model.CandidateId

        // Check if the post belongs to any connected candidate
        if (candidateId in connectedCandidateIds) {
            holder.titleTextView.text = model.Title
            Glide.with(holder.imageView.context)
                .load(model.imageUrl) // Load image from URL using Glide
                .into(holder.imageView)
            holder.itemView.visibility = View.VISIBLE
        } else {
            // If not connected, set an empty placeholder
            holder.titleTextView.text = ""
            holder.imageView.setImageDrawable(null) // Clear image
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams.height = 0
        }
    }

}