package com.chuan.lim.popularmovie.api;

import com.chuan.lim.popularmovie.models.MovieResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TMDBClient {
    private static TMDBServiceApi tmdbServiceApi;

    /**
     * Singleton method to generate TMDB Service
     * @return TMDBService
     */
    public static TMDBServiceApi getTmdbServiceApi() {
        if(tmdbServiceApi == null) {
            // Reference code: https://square.github.io/retrofit/
            // Setting up HTTP logger for debugging
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
            OkHttpClient okHttpClient = builder.build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(TMDBServiceApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            tmdbServiceApi = retrofit.create(TMDBServiceApi.class);
        }
        return tmdbServiceApi;
    }

    /**
     * Exposing TMDB Movie Listing Service
     * @see: https://developers.themoviedb.org/3/discover/movie-discover
     */
    public interface TMDBServiceApi {
        String BASE_URL = "http://api.themoviedb.org/3/";
        //TODO: enum the image size
        String IMG_BASE_URL = "https://image.tmdb.org/t/p/w185/";
        String IMG_MID_BASE_URL = "https://image.tmdb.org/t/p/w342/";

        //Request for Popular Movies
        @GET("movie/popular")
        Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

        //Request for Top Rated Movies
        @GET("movie/top_rated")
        Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    }
}


