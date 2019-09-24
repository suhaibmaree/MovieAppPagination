package com.suhaib.pagination.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suhaib.pagination.R;
import com.suhaib.pagination.adapters.PaginationAdapter;
import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.eventbus.FavoriteMovieEvent;
import com.suhaib.pagination.presenters.MovieView;
import com.suhaib.pagination.presenters.MainPresenter;
import com.suhaib.pagination.utils.HaveNetworksUtils;
import com.suhaib.pagination.utils.PaginationScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

import static com.suhaib.pagination.Views.MovieDetails.startDetailsActivity;

public class MainActivity extends AppCompatActivity implements MovieView {

    private static final String TAG = "MainActivity";
    private static String KEY_MSG = TAG + ".KEY_MAG";

    private PaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView moviesList;
    private ProgressBar loadingIndicator;
    private Button relaodButton;

    private static final int pageStart = 1;
    public boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages = 1;
    private int currentPage = pageStart;
    private MainPresenter mPresenter;


    public static void startActivityAndFinish(Activity source) {

        Log.d(TAG, "start Activity And Finish with only source");
        Intent homeIntent = new Intent(source, MainActivity.class);
        source.startActivity(homeIntent);
        source.finish();
    }

    public static void startActivity(Activity source) {

        Log.d(TAG, "start Activity");
        Intent homeIntent = new Intent(source, MainActivity.class);
        source.startActivity(homeIntent);

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFavoriteEvent(FavoriteMovieEvent.DETAIL movieEvent) {

        Movie movie = movieEvent.getMovie();

        Log.d(TAG, "move checked " + movie.isClicked());
        EventBus.getDefault().removeStickyEvent(FavoriteMovieEvent.class);
        adapter.onFavoriteEvent(movie);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        loadData();
        Log.i(TAG, "loadData");
    }

    @Override
    protected void onStart() {
        relaodButton.setVisibility(View.GONE);
        branchInit();

        EventBus.getDefault().register(this);
        super.onStart();
    }// end onStart

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    private void branchInit() {
        // Branch init
        Branch instance = Branch.getInstance();
        if (instance != null) {
            instance.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error == null) {
                        Log.i("BRANCH SDK", referringParams.toString());

                        try {

                            Log.i(TAG, referringParams.getString("key"));
                            int key = Integer.parseInt(referringParams.getString("key"));
                            startDetailsActivity(MainActivity.this, key);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.i("BRANCH SDK", error.getMessage());
                    }
                }
            }, this.getIntent().getData(), this);
        }
    }

    private void initUi() {
        moviesList = (RecyclerView) findViewById(R.id.main_recycler);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_progress);
        relaodButton = (Button) findViewById(R.id.reload_buuton);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        moviesList.setLayoutManager(linearLayoutManager);
        moviesList.setItemAnimator(new DefaultItemAnimator());
        moviesList.setAdapter(adapter);

        moviesList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return totalPages;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {

                return isLoading;
            }
        });
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new MainPresenter(this);
        }
    }

    public void loadData() {

        relaodButton.setVisibility(View.GONE);
        if (currentPage <= 1) {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        initPresenter();
        mPresenter.getTopMovies(getAPIKey(), currentPage);
    }

    private String getAPIKey() {
        return getString(R.string.api_key);
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    /***
     *
     * implemented methods
     */

    @Override
    public void onError(Throwable e) {

        String msg = "No Internet Connection";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        loadingIndicator.setVisibility(View.GONE);
        relaodButton.setVisibility(View.VISIBLE);
        relaodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relaodButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                });
            }
        });


    }

    @Override
    public void displayPageContent(List<Movie> movies, int total) {
        if (HaveNetworksUtils.haveNetwork(this)) {
            if (currentPage == 1) {
                loadingIndicator.setVisibility(View.GONE);
                relaodButton.setVisibility(View.GONE);
                adapter.addAll(movies);

                if (currentPage <= total)
                    adapter.addLoadingFooter();
                else isLastPage = true;
            } else if (currentPage > 1) {
                adapter.removeLoadingFooter();
                isLoading = false;
                adapter.addAll(movies);

                if (currentPage != total) adapter.addLoadingFooter();
                else isLastPage = true;
            }
        }
    }

    @Override
    public void showMessage(String s) {

        Toast.makeText(getApplicationContext(), "can not load data", Toast.LENGTH_SHORT).show();

    }

}// end main activity class