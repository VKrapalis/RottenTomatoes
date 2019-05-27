package com.example.rottentomatoes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMoviesClickCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_activity_fragment);


        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){ return;}
                Toolbar toolbar = findViewById(R.id.toolbarPhone);
                setSupportActionBar(toolbar);
                MainActivityFragment firstFragment = new MainActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, firstFragment)
                        .commit();



        } else {
            Toolbar toolbar = findViewById(R.id.toolbarTablet);
            setSupportActionBar(toolbar);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainActivityFragment, new MainActivityFragment())
                    .replace(R.id.movieActivityFragment, new MovieActivityFragment())
                    .commit();
        }
    }
    @Override
    public void onAttachFragment(Fragment fragment){
       if (fragment instanceof MainActivityFragment){
            MainActivityFragment mainActivityFragment = (MainActivityFragment) fragment;
            mainActivityFragment.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(Movie movie) {

        if (findViewById(R.id.fragment_container) != null) {
            MovieActivityFragment secondFragment = new MovieActivityFragment();
            String temp = Integer.toString(movie.getId());
            Bundle bundle = new Bundle();
            bundle.putString("message", temp);

            secondFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, secondFragment)
                    .commit();
        } else {
            MovieActivityFragment secondFragment = new MovieActivityFragment();
            String temp = Integer.toString(movie.getId());
            Bundle bundle = new Bundle();
            bundle.putString("message", temp);

            secondFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieActivityFragment, secondFragment)
                    .commit();
        }

    }




}