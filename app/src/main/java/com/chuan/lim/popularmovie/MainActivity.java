package com.chuan.lim.popularmovie;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chuan.lim.popularmovie.adapter.MovieAdapter;
import com.chuan.lim.popularmovie.models.Movie;
import com.chuan.lim.popularmovie.api.TMDBClient;
import com.chuan.lim.popularmovie.models.MovieResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Reference: https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
 * https://inthecheesefactory.com/blog/retrofit-2.0/en
 *
 *
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {
    private RecyclerView mMovieListingView;
    private MovieAdapter mMovieAdapter;
    private TextView mFailToLoadTextView;
    private TextView mPageTitle;
    private final String TAG = this.getClass().getCanonicalName();
    private By currentSelectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // when app load, we will start 'Popular Movies'
        callTMDBApi(By.POPULAR_MOVIES);
    }

    /**
     * Initialise view
     */
    private void initView() {
        mFailToLoadTextView = findViewById(R.id.tv_unable_to_load);
        mPageTitle = findViewById(R.id.tv_page_title);
        mMovieListingView = findViewById(R.id.rv_movie_list);
        // Perform better
        mMovieListingView.setHasFixedSize(true);

        // use a Gridlayout manager
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mMovieListingView.setLayoutManager(mLayoutManager);

        // Set Adapter to movie list
        mMovieAdapter = new MovieAdapter(this);
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_popular_movies:
                callTMDBApi(By.POPULAR_MOVIES);
                break;
            case R.id.item_top_rated:
                callTMDBApi(By.TOP_RATED);
                break;
            default:
                //noop
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calling TMDB API by popular_movies or top_rated.
     * @param by
     */
    private void callTMDBApi(By by) {
        // Avoid calling the api if the currentOption is the same
        if (currentSelectedOption == by) {
            Log.d(TAG, "Same option as before not doing it");
            return;
        }
        switch (by) {
            case POPULAR_MOVIES:
                Log.d(TAG, "Calling Popular Movies API");
                TMDBClient.getTmdbServiceApi()
                        .getPopularMovies(getString(R.string.api_key))
                        .enqueue(movieListingCallback);
                currentSelectedOption = by;
                // set the page title
                mPageTitle.setText(R.string.item_popular_movies);
                break;
            case TOP_RATED:
                Log.d(TAG, "Calling Top rated Movies API");
                TMDBClient.getTmdbServiceApi()
                        .getTopRatedMovies(getString(R.string.api_key))
                        .enqueue(movieListingCallback);
                currentSelectedOption = by;
                // set the page title
                mPageTitle.setText(R.string.item_top_rated);
                break;
            default:
                //Shouldn't happen, so no_op
                break;
        }
    }

    /**
     * Callback to TMDB service which allows us to show UI when Retrofit async call
     * come back.
     */
    Callback<MovieResponse> movieListingCallback = new Callback<MovieResponse>() {
        @Override
        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            if (response.code() == 200 && response.body() != null) {
                // Make error message textview disappears just in case
                mFailToLoadTextView.setVisibility(View.INVISIBLE);

                List<Movie> movies = response.body().movies;
                Log.d("movieListingCallback", "Return back Number of movies:  " + movies.size());
                mMovieAdapter.setMovieList(movies);
                mMovieListingView.setAdapter(mMovieAdapter);
                Toast.makeText(MainActivity.this,
                        getString(R.string.msg_movie_listing_success),
                        Toast.LENGTH_SHORT).show();
            } else {
                showErrorOnToast(R.string.error_msg_movie_listing + response.toString());
            }
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {
            showErrorOnToast(R.string.error_msg_movie_listing + call.request().toString());
            t.printStackTrace();
        }
    };

    /**
     * Show error on a Toast
     */
    private void showErrorOnToast(String msg) {
        mFailToLoadTextView.setVisibility(View.VISIBLE);
        mFailToLoadTextView.setText(R.string.error_msg_unable_to_load);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Movie movie) {
        if (movie == null) {
            Log.e(TAG, "Movie clicked is null. Ignoring click");
            return;
        }

        Log.d(TAG, "Launching movie details: " + movie.getTitle());

        //Explicit intent
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent); //Launching movie details
    }

    /**
     * Enum to store Menu Item options
     */
    public enum By {
        POPULAR_MOVIES("Popular Movies"), TOP_RATED("Top Rated Movies");
        private final String value;

        By(String value) {this.value = value;}

        @Override
        public String toString() { return value; }
    }
}