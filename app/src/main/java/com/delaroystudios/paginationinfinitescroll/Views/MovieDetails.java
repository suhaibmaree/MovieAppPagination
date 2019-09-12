package com.delaroystudios.paginationinfinitescroll.Views;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.delaroystudios.paginationinfinitescroll.R;
import com.delaroystudios.paginationinfinitescroll.entitys.Movie;

public class MovieDetails extends AppCompatActivity {

    private Movie mMovie;
    public static final String TAG ="MovieDetails";
    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    private Intent mIntent;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    private ImageView mPosterImage;
    private TextView mNameOfMovie;
    private TextView mPlotSynopsis;
    private TextView mUserRating;
    private TextView mReleaseData;
    private RatingBar mRatingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_movie_details);

        setupUI();
        BaindUI();


    }


    public void setupUI(){

        Log.d(TAG,"parcelable move" );
        mIntent = getIntent();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        mCollapsingToolbarLayout.setTitle("Movie Details");

        mPosterImage = (ImageView) findViewById(R.id.image_header);
        mNameOfMovie = (TextView) findViewById(R.id.title);
        mPlotSynopsis = (TextView) findViewById(R.id.plotsynopsis);
        mReleaseData = (TextView) findViewById(R.id.releasedate);
        mRatingBar = (RatingBar) findViewById(R.id.userrating);
    }

    public void BaindUI(){

        if(mIntent.hasExtra("movie")){

            mMovie = mIntent.getParcelableExtra("movie");

            String movieName = mMovie.getOriginalTitle();
            String synopsis = mMovie.getOverview();
            double rating = mMovie.getVoteAverage();
            String dateOfRelease = mMovie.getReleaseDate();

            Log.d(TAG,"parcelable move poster path: " + getString(R.string.poster_path)+mMovie.getPosterPath());
            Glide.with(this)
                    .load(getString(R.string.poster_path) + mMovie.getPosterPath())
                    .into(mPosterImage);

            mNameOfMovie.setText(movieName);
            mPlotSynopsis.setText(synopsis);
            mReleaseData.setText(dateOfRelease);
            mRatingBar.setRating((float) rating/2);


        }// end binding UI
        else {
            Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
        }
    }

}
