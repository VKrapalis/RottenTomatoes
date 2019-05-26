package com.example.rottentomatoes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class MainActivityFragment extends Fragment {

    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private SharedViewModel viewModel;

    private MoviesRepository moviesRepository;

    private List<Genre> movieGenres;
    private List<Movie> movies;

    private boolean isFetchingMovies;
    private int currentPage = 1;

    private String sortBy = MoviesRepository.TOP_RATED;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View retView = inflater.inflate(R.layout.activity_main_fragment, container, false);

        moviesRepository = MoviesRepository.getInstance();

        moviesList = (RecyclerView) retView.findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        getGenres();

        setupOnScrollListener();


        return retView;
    }

   private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {

            @Override
            public void onSuccess(List<Genre> genres) {

                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
               showError();
            }


        });

    }

    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                //setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        currentPage = 1;

        switch (item.getItemId()) {
            case R.id.popular:
                sortBy = MoviesRepository.POPULAR;
                getMovies(currentPage);
                Toast.makeText(getActivity(), sortBy, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.top_rated:
                sortBy = MoviesRepository.TOP_RATED;
                getMovies(currentPage);
                return true;
            case R.id.upcoming:
                sortBy = MoviesRepository.UPCOMING;
                getMovies(currentPage);
                return true;
            default:
                return false;
        }
    }

    /*private void setTitle() {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
        }
    }*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            viewModel.setText(Integer.toString(movie.getId()));
        }
    };
};