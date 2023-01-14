package com.example.androidfinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfinalproject.model.Review;
import com.squareup.picasso.Picasso;


class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
        OnItemClickListener listener;
        int buttonsVisible;
        public static interface OnItemClickListener{
            void onItemClick(int pos);
        }

        LayoutInflater inflater;
        public ReviewRecyclerAdapter(int visibility){
            this.inflater = inflater;
            this.buttonsVisible = visibility;
        }

        void setOnItemClickListener(ReviewRecyclerAdapter.OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review_row, parent, false);
            ReviewViewHolder viewHolder = new ReviewViewHolder(view, (OnItemClickListener) listener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
            Review review = ReviewListViewModel.getData().getValue().get(position);

            holder.bind(review);
        }

        @Override
        public int getItemCount() {
            return ReviewListViewModel.getData().getValue() == null ? 0 : ReviewListViewModel.getData().getValue().size();
        }
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewDesc;
        ImageView movieImage;
        ImageView reviewButton;

        void bind(Review review) {

            reviewDesc.setText(review.getReviewDesc());

            if (review.getMovieImageUrl() != null &&
                    !review.getMovieImageUrl().equals("")) {
                Picasso.get()
                        .load(review.getMovieImageUrl())
                        .into(movieImage);
            }

        }

        public ReviewViewHolder(@NonNull View view, ReviewRecyclerAdapter.OnItemClickListener listener) {
            super(view);
            reviewDesc = view.findViewById(R.id.review_vomment_txtview);
            movieImage = view.findViewById(R.id.review_img);
            reviewButton = view.findViewById(R.id.review_button_img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }
    }

