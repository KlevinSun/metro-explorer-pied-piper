package com.example.felixlin.metroexplorer.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.felixlin.metroexplorer.R;
import com.example.felixlin.metroexplorer.activity.adapter.LandmarkRecyclerViewAdapter;
import com.example.felixlin.metroexplorer.activity.model.Landmark;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetroFragment extends Fragment {

    @BindView(R.id.rvLandmarks)
    RecyclerView rvLandmarks;
    private List<Landmark> mLandmarkList;


    public MetroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_metro, container, false);

        ButterKnife.bind(this, view);

        initializeData();

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        rvLandmarks.setHasFixedSize(true);
        rvLandmarks.setLayoutManager(llm);

        LandmarkRecyclerViewAdapter adapter = new LandmarkRecyclerViewAdapter(this.getContext(), mLandmarkList);
        rvLandmarks.setAdapter(adapter);

        return view;
    }


    private void initializeData() {

    }

}
