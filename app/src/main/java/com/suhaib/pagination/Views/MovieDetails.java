package com.suhaib.pagination.Views;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.suhaib.pagination.R;
import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.model.MovieModel;
import com.suhaib.pagination.presenters.DetailsView;
import com.suhaib.pagination.presenters.MovieDetailsPresenter;

import java.util.Calendar;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;


public class MovieDetails extends AppCompatActivity implements DetailsView {

    private int mMovieId;
    private Movie mMovie;
    private MovieDetailsPresenter detailsPresenter;

    public static final String TAG = "MovieDetails";
    private static String KEY_MSG = "movieId";
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



        public static void startDetailsActivity(Activity source, int id) {

        Log.d(TAG, "start Activity And Finish with source and movie id");
        Intent homeIntent = new Intent(source, MovieDetails.class);
        homeIntent.putExtra(KEY_MSG, id);
        source.startActivity(homeIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Branch logging for debugging
        Branch.enableDebugMode();
        // Branch object initialization
        Branch.getAutoInstance(this);

        setupUI();
        BaindUI();


    }// end onCreate


    public void setupUI() {

        Log.d(TAG, "parcelable move");

        mIntent = getIntent();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        detailsPresenter = new MovieDetailsPresenter(this);
    } //end setup UI

    public void BaindUI() {

        if (mIntent.hasExtra(KEY_MSG)) {

            mMovieId = mIntent.getIntExtra(KEY_MSG,0);
            detailsPresenter.getMovie(getString(R.string.api_key),mMovieId);


        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
    } // end binding UI

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } //end onCreate option menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_button:
                shareButton();
                break;
        }
        return super.onOptionsItemSelected(item);
    } //end on option item selected


    public void shareButton(){

        Log.i(TAG, "Share Button: " +mMovie.getId().toString() );

        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle(mMovie.getTitle())
                .setContentDescription(mMovie.getOverview())
                .setContentImageUrl(getString(R.string.poster_path) + mMovie.getPosterPath())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(new ContentMetadata().addCustomMetadata("key", mMovie.getId().toString() ));

        LinkProperties lp = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 123 launch")
                .setStage("new user")
                .addControlParameter("$desktop_url", "http://example.com/home")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        buo.generateShortUrl(this, lp, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i(TAG, "got my Branch link to share: " + url);
                }
            }
        });

        ShareSheetStyle ss = new ShareSheetStyle(MovieDetails.this, "Check this out!", "This movie is awesome: ")
                .setCopyUrlStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        buo.showShareSheet(this, lp,  ss,  new Branch.BranchLinkShareListener() {
            @Override
            public void onShareLinkDialogLaunched() {
            }
            @Override
            public void onShareLinkDialogDismissed() {
            }
            @Override
            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
            }
            @Override
            public void onChannelSelected(String channelName) {
            }
        });


    }


    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void showMessage() {

        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayDetailsContent(Movie movie) {

        this.mMovie = movie;

        String movieName = movie.getOriginalTitle();
        String synopsis = movie.getOverview();
        double rating = movie.getVoteAverage();
        String dateOfRelease = movie.getReleaseDate();

        Log.i(TAG, "parcelable move poster path: " + getString(R.string.poster_path) + movie.getPosterPath());
        Glide.with(this)
                .load(getString(R.string.poster_path) + movie.getPosterPath())
                .into(mPosterImage);

        mNameOfMovie.setText(movieName);
        mPlotSynopsis.setText(synopsis);
        mReleaseData.setText(dateOfRelease);
        mRatingBar.setRating((float) rating / 2);

    }
}//end details class
