package com.theappnerds.shubham.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.theappnerds.shubham.moviedirectory.Activity.MovieDetailsActivity;
import com.theappnerds.shubham.moviedirectory.Model.Movie;
import com.theappnerds.shubham.moviedirectory.R;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }


    @NonNull
    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.movie_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        String posterLink = movie.getPoster();


        //In new picasso version Picasso.with(context) is replace with Picasso.get() === with no parameter
        Picasso.get()
                .load(posterLink)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.poster);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYear());
        holder.type.setText(movie.getMovieType());


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView title, year, type;


        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            poster = (ImageView) itemView.findViewById(R.id.moviewrow_image_id);
            title = (TextView) itemView.findViewById(R.id.movierow_title_id);
            year = (TextView) itemView.findViewById(R.id.movierow_released_id);
            type = (TextView) itemView.findViewById(R.id.movierow_category_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, MovieDetailsActivity.class);

                    intent.putExtra("movieObj", movie);
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
