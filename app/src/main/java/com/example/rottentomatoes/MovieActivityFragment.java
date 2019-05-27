package com.example.rottentomatoes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MovieActivityFragment extends Fragment {
    private EditText editText;
    private String movieid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View retView = inflater.inflate(R.layout.activity_movie_fragment, container, false);
        editText = retView.findViewById(R.id.edit_text);
        setHasOptionsMenu(true);

        //Final FragmentActivity fragmentBelongActivity = getActivity();
        return retView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){

        movieid = bundle.getString("message", "bla");
        editText.setText(movieid);}
    }
}