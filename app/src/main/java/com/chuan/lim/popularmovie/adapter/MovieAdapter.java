package com.chuan.lim.popularmovie.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chuan.lim.popularmovie.R;
import com.chuan.lim.popularmovie.api.TMDBClient;
import com.chuan.lim.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {
    private List<Movie> movieList;
    private final OnItemClickListener onItemClickListener;
    private final String TAG = this.getClass().getName();

    // Create a listener interface
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public MovieAdapter(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Setting the movie list
     * @param movies
     */
    public void setMovieList(List<Movie> movies) {
        Log.d(TAG, "MovieAdapter with movies count: " + movies.size());
        if (movieList != null ) {
            Log.d(TAG, "Re-writing the movie list");
            movieList.clear();
            movieList.addAll(movies);
        } else {
            Log.d(TAG, "Assigning a new movie list");
            movieList = movies;
        }
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating a new view holder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_listing_layout, parent, false);

        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        Movie m = movieList.get(position);
        if (null != m) {
            holder.bind(m);

        }
    }


    /**
     * Movie Item
     */
    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final String TAG = MovieItemViewHolder.class.getSimpleName();
        private final ImageView poster;

        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * Binding the movie value to view.
         * @param movie
         */
        public void bind(Movie movie) {
            String imgUrl = getPosterUrl(movie.getPosterPath());
            Log.d(TAG, "Loading Img URL: " + imgUrl);
            Picasso.get().load(Uri.parse(imgUrl)).into(poster);
        }

        /**
         * Return the absolute url for the poster image url
         * @param relativeImgUrl
         * @return Poster URL (String)
         */
        private String getPosterUrl(String relativeImgUrl) {
            return TMDBClient.TMDBServiceApi.IMG_BASE_URL.concat(relativeImgUrl);
        }

        @Override
        public void onClick(View v) {
            Movie m = movieList.get(getAdapterPosition());
            onItemClickListener.onItemClick(m);
        }
    }
}
