package com.chuan.lim.popularmovie.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * JSON response is as such:
 * {
 *   "page": 1,
 *   "total_results": 10000,
 *   "total_pages": 500,
 *   "results": [
 *     {movie1}, {movie2}
 *    ]
 * }
 *  Using: http://www.jsonschema2pojo.org/ to generate the code.
 *  and delete most of the unused variables
 */
public class MovieResponse {
    // Make it looks like properties.
    @SerializedName("results")
    public List<Movie> movies = null;
}

