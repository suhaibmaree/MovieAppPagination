package com.suhaib.pagination.Views;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suhaib.pagination.R;
import com.suhaib.pagination.adapters.PaginationAdapter;
import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.presenters.MovieView;
import com.suhaib.pagination.presenters.Presenter;
import com.suhaib.pagination.utils.HaveNetworksUtils;
import com.suhaib.pagination.utils.PaginationScrollListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieView {

    private static final String TAG = "MainActivity";

    private PaginationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView moviesList;
    private ProgressBar loadingIndicator;

    private static final int pageStart = 1;
    public boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages = 1;
    private int currentPage = pageStart;
    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        loadData();
    }

    private void initUi() {
        moviesList = (RecyclerView) findViewById(R.id.main_recycler);
        loadingIndicator = (ProgressBar) findViewById(R.id.main_progress);
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
            mPresenter = new Presenter(this);
        }
    }

    private void loadData() {
        initPresenter();
        mPresenter.getTopMovies(getAPIKey(), currentPage);
    }

    private String getAPIKey() {
        return getString(R.string.api_key);
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    /***
     *
     * implemented methods
     */

    @Override
    public void onError(Throwable e) {

        String msg = "No Internet Connection";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void displayPageContent(List<Movie> movies, int total) {
        if (HaveNetworksUtils.haveNetwork(this)) {
            if (currentPage == 1) {
                loadingIndicator.setVisibility(View.GONE);
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
        } else {
            adapter.addLoadingFooter();
        }
    }

    @Override
    public void showMessage(String s) {

        Toast.makeText(getApplicationContext(), "can not load data", Toast.LENGTH_SHORT).show();

    }

}