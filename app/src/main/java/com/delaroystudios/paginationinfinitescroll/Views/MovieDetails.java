package com.delaroystudios.paginationinfinitescroll.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.delaroystudios.paginationinfinitescroll.R;
import com.delaroystudios.paginationinfinitescroll.entitys.Movie;

public class MovieDetails extends AppCompatActivity {

    private Movie mMovie;
    private ImageView posterImage;
    public static final String TAG ="MovieDetails";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_movie_details);

        Intent intent = getIntent();
        posterImage = (ImageView) findViewById(R.id.image_header);

        Log.d(TAG,"parcelable move" );

        if(intent.hasExtra("movie")){

            mMovie = intent.getParcelableExtra("movie");
            Log.d(TAG,"parcelable move poster path: " + getString(R.string.poster_path)+mMovie.getPosterPath());
            Glide.with(this)
                    .load(getString(R.string.poster_path) + mMovie.getPosterPath())
                    .into(posterImage);
        }

    }
}
