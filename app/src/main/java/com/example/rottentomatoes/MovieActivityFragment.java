package com.example.rottentomatoes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MovieActivityFragment extends Fragment {
    private EditText editText;
    private String movieid;
    private TextView textView;

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    private MoviesRepository moviesRepository;
    private int movieId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View retView = inflater.inflate(R.layout.activity_movie_fragment, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null){


            movieid = bundle.getString("message", "bla");
            //editText.setText(movieid);}
            movieId = Integer.parseInt(movieid);
            Log.d("123",movieid);

        }


        moviesRepository = MoviesRepository.getInstance();


        initUI(retView);

        getMovie();

        //Final FragmentActivity fragmentBelongActivity = getActivity();
        return retView;
    }


    private void initUI(View view) {
        movieBackdrop = view.findViewById(R.id.movieDetailsBackdrop);
        movieTitle = view.findViewById(R.id.movieDetailsTitle);
        movieGenres = view.findViewById(R.id.movieDetailsGenres);
        movieOverview = view.findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = view.findViewById(R.id.summaryLabel);
        movieReleaseDate = view.findViewById(R.id.movieDetailsReleaseDate);
        movieRating = view.findViewById(R.id.movieDetailsRating);
        movieTrailers = view.findViewById(R.id.movieTrailers);
        movieReviews = view.findViewById(R.id.movieReviews);
    }

    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieTitle.setText(movie.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isRemoving()) {
                    Glide.with(MovieActivityFragment.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }
            }

            @Override
            public void onError() {
              //  finish();
            }
        });
    }

    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/


    private void showError() {
        //Toast.makeText(MovieActivityFragment.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){


        movieid = bundle.getString("message", "bla");
        //editText.setText(movieid);}
        movieId = Integer.parseInt(movieid);
            Log.d("123",movieid);

    }
}
}