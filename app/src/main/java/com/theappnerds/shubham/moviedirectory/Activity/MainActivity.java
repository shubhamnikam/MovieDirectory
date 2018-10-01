package com.theappnerds.shubham.moviedirectory.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theappnerds.shubham.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.theappnerds.shubham.moviedirectory.Model.Movie;
import com.theappnerds.shubham.moviedirectory.R;
import com.theappnerds.shubham.moviedirectory.Util.Constants;
import com.theappnerds.shubham.moviedirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //take search from sharedPref
        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        movieList = new ArrayList<>();
        // getMovies(search);
        movieList = getMovies(search);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showInputDialog();
            return true;
        } else if (id == R.id.action_info) {
            Toast.makeText(this, "made by using imdb API", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //Get movies from json and set(assign) setMethods from Movie

    public List<Movie> getMovies(String searchTerm) {


        movieList.clear();

        //To get Main Json object
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.API_KEY,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            //To get JSON array inside the Json object
                            JSONArray movieArray = response.getJSONArray("Search");

                            for (int i = 0; i < movieArray.length(); i++) {
                                //To get each individual object from Json array
                                JSONObject movieObj = movieArray.getJSONObject(i);

                                Movie movie = new Movie();
                                movie.setTitle(movieObj.getString("Title"));
                                movie.setYear("Year : " + movieObj.getString("Year"));
                                movie.setMovieType("Type : " + movieObj.getString("Type"));
                                movie.setPoster(movieObj.getString("Poster"));
                                movie.setImdbId(movieObj.getString("imdbID"));

                                Log.d("data", movie.getTitle());

                                movieList.add(movie);

                            }
                            movieRecyclerViewAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MainActivityError ", error.getMessage());

            }
        });

        queue.add(jsonObjectRequest);
        return movieList;
    }

    private void showInputDialog() {
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_search, null);
        final EditText newSearch = (EditText) view.findViewById(R.id.editText_search_term);
        Button submitButton = (Button) view.findViewById(R.id.button_search);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs p = new Prefs(MainActivity.this);

                String s = newSearch.getText().toString();
                p.setSearch(s);

                movieList.clear();
                movieRecyclerViewAdapter.notifyDataSetChanged();
                getMovies(s);

                dialog.dismiss();
            }
        });

    }
}
