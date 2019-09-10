package com.delaroystudios.paginationinfinitescroll.Views;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.delaroystudios.paginationinfinitescroll.R;
import com.delaroystudios.paginationinfinitescroll.adapter.PaginationAdapter;
import com.delaroystudios.paginationinfinitescroll.entity.Movie;
import com.delaroystudios.paginationinfinitescroll.entity.MoviesResponse;
import com.delaroystudios.paginationinfinitescroll.presenter.Presenter;
import com.delaroystudios.paginationinfinitescroll.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private Presenter mPresenter;
//    private Service movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter = new Presenter(MainActivity.this,currentPage);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
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

        //init service and load data
//        movieService = Client.getClient().create(Service.class);

        mPresenter = new Presenter(this,currentPage);

    }


//    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
//
//        callTopRatedMoviesApi().enqueue(new Callback<MoviesResponse>() {
//            @Override
//            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
//                // Got data. Send it to adapter
//                if(currentPage == 1) {
//                    List<Movie> results = fetchResults(response);
//                    progressBar.setVisibility(View.GONE);
//                    adapter.addAll(results);
//
//                    if (currentPage <= response.body().getTotalPages()) adapter.addLoadingFooter();
//                    else isLastPage = true;
//                }
//                else if (currentPage > 1){
//                    adapter.removeLoadingFooter();
//                    isLoading = false;
//
//                    List<Movie> results = fetchResults(response);
//                    adapter.addAll(results);
//
//                    if (currentPage != response.body().getTotalPages()) adapter.addLoadingFooter();
//                    else isLastPage = true;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                t.printStackTrace();
//
//            }
//        });
//
//    }

    public void loadPage(List<Movie> results, int total){

        if(currentPage == 1) {
            progressBar.setVisibility(View.GONE);
            adapter.addAll(results);

            if (currentPage <= total) adapter.addLoadingFooter();
            else isLastPage = true;
        }
        else if (currentPage > 1){
            adapter.removeLoadingFooter();
            isLoading = false;
            adapter.addAll(results);

            if (currentPage != total) adapter.addLoadingFooter();
            else isLastPage = true;
        }
    }



    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse topRatedMovies = response.body();
        Log.d(TAG,"Total pages: " + response.body().getTotalPages() );
        return topRatedMovies.getResults();
    }




//    private Call<MoviesResponse> callTopRatedMoviesApi() {
//        return movieService.getTopRatedMovies(
//                getString(R.string.api_key),
//                currentPage
//        );
//    }


    public String getAPIKey(){
       return getString(R.string.api_key);
    }

    public void showMessage(String msg) {

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


    }


}