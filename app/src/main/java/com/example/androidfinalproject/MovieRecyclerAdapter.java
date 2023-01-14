package com.example.androidfinalproject;

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



    class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {
        OnItemClickListener listener;
        public static interface OnItemClickListener{
            void onItemClick(int pos);
        }

        LayoutInflater inflater;

        public MovieRecyclerAdapter(){
            this.inflater = inflater;
        }

        void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie_row, parent, false);
            MovieViewHolder viewHolder = new MovieViewHolder(view, listener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            Movie movie = MovieListViewModel.getData().getValue().get(position);

            holder.bind(movie);
        }

        @Override
        public int getItemCount() {
            return MovieListViewModel.getData().getValue() == null ? 0 : MovieListViewModel.getData().getValue().size();
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        ImageView movieImage;
        TextView movieReleaseDate;
        ImageView movieButton;

        void bind(Movie movie) {
            movieName.setText(movie.getTitle());

            if (movie.getMovieImageUrl() != null &&
                    !movie.getMovieImageUrl().equals("")) {
                Picasso.get()
                        .load(movie.getMovieImageUrl())
                        .into(movieImage);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            movieReleaseDate.setText(dateFormat.format(movie.getReleaseDate()));
        }

        public MovieViewHolder(@NonNull View view, MovieRecyclerAdapter.OnItemClickListener listener) {
            super(view);
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

