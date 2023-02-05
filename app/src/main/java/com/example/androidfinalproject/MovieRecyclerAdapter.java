package com.example.androidfinalproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfinalproject.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;


class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        OnItemClickListener listener;

        List<Movie> data;

        public void setData(List<Movie> data){
            this.data = data;
            notifyDataSetChanged();
            Log.d("TAG", data.get(0).getTitle());
        }


    public static interface OnItemClickListener{
            void onItemClick(int pos);
        }

        LayoutInflater inflater;

        public MovieRecyclerAdapter(LayoutInflater inflater, List<Movie> data){
            this.inflater = inflater;
            this.data = data;
        }

        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie_row, parent, false);
            MovieViewHolder viewHolder = new MovieViewHolder(view, listener, data);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            Movie movie = data.get(position);

            holder.bind(movie);
        }

        @Override
        public int getItemCount() {
            if(data == null)return 0;
            return data.size();
        }

    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        ImageView movieImage;
        TextView movieReleaseDate;
        ImageView movieButton;

        List<Movie> data;

        void bind(Movie movie) {
            movieName.setText(movie.getTitle());

            if (movie.getMovieImageUrl() != null &&
                    !movie.getMovieImageUrl().equals("")) {
                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500"+movie.getMovieImageUrl())
                        .into(movieImage);
            } else {
                movieImage.setImageResource(R.drawable.empty_movie);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            movieReleaseDate.setText(movie.getReleaseDate());
        }

        public MovieViewHolder(@NonNull View view, MovieRecyclerAdapter.OnItemClickListener listener, List<Movie> data) {
            super(view);
            this.data = data;
            movieName = view.findViewById(R.id.movie_name_txtview);
            movieImage = view.findViewById(R.id.movie_img);
            movieReleaseDate = view.findViewById(R.id.movie_releasedate_txtview);
            movieButton = view.findViewById(R.id.movie_button_img);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });

            movieButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });


        }
    }

