package com.chuan.lim.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuan.lim.popularmovie.api.TMDBClient;
import com.chuan.lim.popularmovie.models.Movie;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView mPosterImageView;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_activity);

        //Initialise the view
        initView();

        // Get the intent extra
        Intent mainScreenIntent = getIntent();
        if (mainScreenIntent == null) {
            Log.e(TAG, "MainIntent is null");
        } else {
            Movie movie = (Movie)mainScreenIntent.getSerializableExtra("movie");
            Log.d(TAG, "Loading this movie: " + movie);
            // Load image
            Picasso.get().load(Uri.parse(TMDBClient.TMDBServiceApi.IMG_MID_BASE_URL.concat(movie.getPosterPath()))).into(mPosterImageView);
            mOriginalTitle.setText(movie.getOriginalTitle());
            mOverview.setText(movie.getOverview());
            mUserRating.setText(movie.getVoteAverage().toString());
            mReleaseDate.setText(movie.getReleaseDate());
        }
    }

    /**
     * Initialise the view elements
     */
    private void initView() {
        mPosterImageView = findViewById(R.id.iv_poster_big);
        mOriginalTitle = findViewById(R.id.tv_original_title);
        mOverview = findViewById(R.id.tv_overview);
        mUserRating = findViewById(R.id.tv_user_rating);
        mReleaseDate = findViewById(R.id.tv_release_date);
    }
}