package com.suhaib.pagination.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.suhaib.pagination.R;
import com.suhaib.pagination.Views.MainActivity;
import com.suhaib.pagination.entitys.Movie;
import com.suhaib.pagination.eventbus.FavoriteMovieEvent;
import com.suhaib.pagination.utils.HaveNetworksUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.suhaib.pagination.Views.MovieDetails.startDetailsActivity;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int FAILED = 2;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";
    public static final String TAG = "PaginationAdapter";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    private List<Movie> movieResults;
    private Context mContext;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context mView) {
        this.mContext = mView;
        movieResults = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Movie> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View v1 = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new MovieVH(v1);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
            case FAILED:
                ((MainActivity) mContext).setLoading(false);
                View v3 = inflater.inflate(R.layout.item_faild, parent, false);
                viewHolder = new FailedVH(v3);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Movie result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.mMovieTitle.setText(result.getTitle());

                // fab checked
                movieVH.mFavorite.setChecked(result.isClicked());

                movieVH.mYear.setText(
                        result.getReleaseDate().substring(0, 4)
                                + " | " +
                                result.getOriginalLanguage().toUpperCase()
                );
                movieVH.mMovieDesc.setText(result.getOverview());


                Glide
                        .with(mContext)
                        .load(BASE_URL_IMG + result.getPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.mPosterImg);


                //TODO : use eventbus
//                mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//                mEditor = mPreferences.edit();
//                if (mPreferences.getBoolean(result.getTitle(), false)) {
//                    movieVH.mFavorite.setBackgroundResource(R.drawable.ic_favorite_filled);
//                } else {
//                    movieVH.mFavorite.setBackgroundResource(R.drawable.ic_favorite_border);
//                }

                break;

            case LOADING:
//                Do nothing
                break;

            case FAILED:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemCount " + position);
        if (HaveNetworksUtils.haveNetwork(mContext)) {
            Log.d(TAG, "getItemCount have internet" + position);
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        } else {
            ((MainActivity) mContext).setLoading(false);
            return (position == movieResults.size() - 1 && isLoadingAdded) ? FAILED : ITEM;
        }
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        for (Movie result : moveResults) {
            add(result);
        }
    }

    public void remove(Movie r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Movie getItem(int position) {
        return movieResults.get(position);
    }


    public void onFavoriteEvent(Movie event) {
        int id = event.getId();

        int index = movieResults.indexOf(new Movie(id));
        movieResults.get(index).setClicked(event.isClicked());
        notifyDataSetChanged();

    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */


    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        private ToggleButton mFavorite;

        public MovieVH(View itemView) {
            super(itemView);

            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieDesc = itemView.findViewById(R.id.movie_desc);
            mYear = itemView.findViewById(R.id.movie_year);
            mPosterImg = itemView.findViewById(R.id.movie_poster);
            mProgress = itemView.findViewById(R.id.movie_progress);
            mFavorite = itemView.findViewById(R.id.favorite_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    Movie movie = movieResults.get(pos);

                    if (pos != RecyclerView.NO_POSITION) {
                        startDetailsActivity((MainActivity) mContext, movie);
                        Toast.makeText(view.getContext(), movie.getOriginalTitle(),
                                Toast.LENGTH_LONG).show();


                    }
                }
            });


            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();

                    Movie movie = movieResults.get(adapterPosition);
                    movie.setClicked(!movie.isClicked());
                    movieResults.get(adapterPosition).setClicked(movie.isClicked());
                    FavoriteMovieEvent movieEvent = FavoriteMovieEvent
                            .getFavoriteEvent(FavoriteMovieEvent.SOURCE.MAIN, movie);
                    EventBus.getDefault().postSticky(movieEvent);
                }
            });


        } //end movie view holder constructor
    }  //end class


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    protected class FailedVH extends RecyclerView.ViewHolder {

        public FailedVH(View itemView) {
            super(itemView);
        }
    }

}