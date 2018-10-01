package com.theappnerds.shubham.moviedirectory.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.theappnerds.shubham.moviedirectory.Model.Movie;
import com.theappnerds.shubham.moviedirectory.R;
import com.theappnerds.shubham.moviedirectory.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    ImageView movieImage;
    TextView movieTitle, movieYear, movieType, director, actors, category, rating, writers, plot, boxOffice, runtime;

    RequestQueue queue;
    String movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        queue = Volley.newRequestQueue(this);
        movie = (Movie) getIntent().getSerializableExtra("movieObj");
        movieId = movie.getImdbId();

        getMovieDetails(movieId);

        movieImage = (ImageView) findViewById(R.id.moviedetails_image_id);
        movieTitle = (TextView) findViewById(R.id.moviedetails_title_id);
        movieYear = (TextView) findViewById(R.id.moviedetails_released_id);
        movieType = (TextView) findViewById(R.id.moviedetails_category_id);
        director = (TextView) findViewById(R.id.moviedetails_directedby_id);
        actors = (TextView) findViewById(R.id.moviedetails_actor_id);
        rating = (TextView) findViewById(R.id.moviedetails_rating_id);
        writers = (TextView) findViewById(R.id.moviedetails_writer_id);
        plot = (TextView) findViewById(R.id.moviedetails_plot_id);
        boxOffice = (TextView) findViewById(R.id.moviedetails_boxoffice_id);
        runtime = (TextView) findViewById(R.id.moviedetails_runtime_id);


    }

    private void getMovieDetails(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_DETAILS + id + Constants.API_KEY,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.has("Ratings")) {
                            try {
                                JSONArray ratings = response.getJSONArray("Ratings");
                                String source = null;
                                String value = null;

                                if (ratings.length() > 0) {

                                    //To fetch Rotten Tomoto Ratings
                                    JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);
                                    source = mRatings.getString("Source");
                                    value = mRatings.getString("Value");

                                    rating.setText("Source - " + source + "\nRatings - " + value);

                                } else {
                                    rating.setText("Ratings - N/A");
                                }

                                movieTitle.setText(response.getString("Title"));
                                movieYear.setText("Released - " + response.getString("Released"));
                                movieType.setText("Category - " + response.getString("Type"));
                                director.setText("Director - " + response.getString("Director"));
                                writers.setText("Writer -\n" + response.getString("Writer"));
                                plot.setText("Plot -\n" + response.getString("Plot"));
                                runtime.setText("Runtime - " + response.getString("Runtime"));
                                actors.setText("Actors -\n" + response.getString("Actors"));
                                boxOffice.setText("Box Office - " + response.getString("BoxOffice"));

                                Picasso.get()
                                        .load(response.getString("Poster"))
                                        .placeholder(R.mipmap.ic_launcher_round)
                                        .into(movieImage);
                                Log.d("DetailsTitle", response.getString("Title"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("DetailsActivityError : ", error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);


    }


}
