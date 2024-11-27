package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Education;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {

    private Context context;
    private List<Education> educationList;

    // Constructor
    public EducationAdapter(Context context, List<Education> educationList) {
        this.context = context;
        this.educationList = educationList;
    }

    // Inflates the item layout and creates ViewHolder object
    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_education, parent, false);
        return new EducationViewHolder(view);
    }

    // Binds data to the views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position) {
        Education education = educationList.get(position);

        // Set the author's full name
        String authorName = education.getAuthor().getFirstName() + " " + education.getAuthor().getLastName();
        holder.authorName.setText(authorName);

        // Set the blog title and content
        holder.blogTitle.setText(education.getTitle());
        holder.blogDescription.setText(education.getContent());

        // Set the number of likes and views with titles
        holder.likesCount.setText("Likes: " + education.getLikesCount());
        holder.viewsCount.setText("Views: " + education.getViewsCount());

        String imageUrl = education.getUri();
        Log.d("ImageLoading", "Attempting to load image from URL: " + imageUrl);

        if (!imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_person_24)
                    .into(holder.blogImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Picasso", "Image loaded successfully from: " + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image from URL: " + imageUrl + " - " + e.getMessage());
                        }
                    });
        } else {
            holder.blogImage.setImageResource(R.drawable.baseline_person_24);
        }

        // Format and set the created date
        // Ensure `getCreatedAt()` returns a formatted date string; adjust as needed
        holder.createdAt.setText(education.getCreatedAt().toString());
    }

    // Returns the total number of items in the data list
    @Override
    public int getItemCount() {
        return educationList.size();
    }

    // ViewHolder class to hold references to the views for each item
    public static class EducationViewHolder extends RecyclerView.ViewHolder {

        // Declare views from the layout
        TextView authorName, blogTitle, blogDescription, likesCount, viewsCount, createdAt;
        ImageView blogImage;

        // Constructor to initialize the views
        public EducationViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author_name);
            blogTitle = itemView.findViewById(R.id.blog_title); // Ensure this ID exists in your XML
            blogDescription = itemView.findViewById(R.id.blog_description);
            likesCount = itemView.findViewById(R.id.like_count);
            viewsCount = itemView.findViewById(R.id.view_count);
            blogImage = itemView.findViewById(R.id.blog_image);
            createdAt = itemView.findViewById(R.id.created_at); // Ensure this ID exists in your XML
        }
    }
}
