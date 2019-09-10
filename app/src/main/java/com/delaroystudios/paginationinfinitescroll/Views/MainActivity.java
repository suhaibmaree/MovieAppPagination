package com.delaroystudios.paginationinfinitescroll.Views;

import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.delaroystudios.paginationinfinitescroll.adapters.PaginationAdapter;
import com.delaroystudios.paginationinfinitescroll.entitys.Movie;
import com.delaroystudios.paginationinfinitescroll.entitys.MoviesResponse;
import com.delaroystudios.paginationinfinitescroll.presenters.Presenter;
import com.delaroystudios.paginationinfinitescroll.utils.HaveNetworks;
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
//    private MovieService movieService;

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
                        mPresenter = new Presenter(MainActivity.this, currentPage);
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

        mPresenter = new Presenter(this, currentPage);

    }


    public void loadPage(List<Movie> results, int total) {

        if(new HaveNetworks(this).haveNetwork()) {

            if (currentPage == 1) {
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= total)
                    adapter.addLoadingFooter();
                else isLastPage = true;
            } else if (currentPage > 1) {
                adapter.removeLoadingFooter();
                isLoading = false;
                adapter.addAll(results);

                if (currentPage != total) adapter.addLoadingFooter();
                else isLastPage = true;
            }
        }else {
            adapter.addLoadingFooter();
        }
    }

    public String getAPIKey() {
        return getString(R.string.api_key);
    }

    public void showMessage(String msg) {

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


    }


}