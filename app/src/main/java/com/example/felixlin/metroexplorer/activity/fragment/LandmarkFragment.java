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
public class LandmarkFragment extends Fragment {


    @BindView(R.id.rvLandmarks)
    RecyclerView rvLandmarks;
    private List<Landmark> mLandmarkList;

    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landmark, container, false);
        ButterKnife.bind(this, view);

        initializeData();

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());

        rvLandmarks.setHasFixedSize(true);
        rvLandmarks.setLayoutManager(llm);

        LandmarkRecyclerViewAdapter adapter = new LandmarkRecyclerViewAdapter(getContext(), mLandmarkList);
        rvLandmarks.setAdapter(adapter);

        return view;
    }

    private void initializeData() {

        mLandmarkList = new ArrayList<>();
        mLandmarkList.add(new Landmark("lincoln-memorial-washington-2", "Lincoln Memorial", "The Lincoln Memorial is an American national monument built to honor the 16th President of the United States, Abraham Lincoln. It is located on the western end of the National Mall in Washington, D.C., across from the Washington Monument.", 6.5f, 854, "/jMgApybamWU7NlPiUiGWiw/o.jpg", "/jMgApybamWU7NlPiUiGWiw/o.jpg"));
        mLandmarkList.add(new Landmark("vietnam-veterans-memorial-washington", "Vietnam Veterans Memorial", "The Vietnam Veterans Memorial is a 2-acre (8,000 m²) national memorial in Washington, D.C. It honors U.S. service members of the U.S. armed forces who fought in the Vietnam War, service members who died in service in Vietnam/South East Asia, and those service members who were unaccounted for (missing in action, MIA) during the war.", 6.2f, 745, "/dNXWIvlJYpIqrpnxuiS1JQ/o.jpg", "/dNXWIvlJYpIqrpnxuiS1JQ/o.jpg"));
        mLandmarkList.add(new Landmark("thomas-jefferson-memorial-washington", "Thomas Jefferson Memorial", "The Jefferson Memorial is a presidential memorial in Washington, D.C., dedicated to Thomas Jefferson, one of the most important of the American Founding Fathers as the main drafter and writer", 5.3f, 691, "/X_842o9mAw-jpDHhsVGStg/o.jpg", "/X_842o9mAw-jpDHhsVGStg/o.jpg"));
        mLandmarkList.add(new Landmark("national-mall-washington", "National Mall", "The long, grassy National Mall is home to iconic monuments including the Lincoln Memorial and the Washington Monument. ", 7.2f, 1802, "/7jLO8nuavjWV_B4xpMKH8w/o.jpg", "/7jLO8nuavjWV_B4xpMKH8w/o.jpg"));
        mLandmarkList.add(new Landmark("c-and-o-canal-washington", "C&O Canal", "The Chesapeake and Ohio Canal, abbreviated as the C&O Canal and occasionally called the \"Grand Old Ditch,\" operated from 1831 until 1924 along the Potomac River from Washington, D.C., to Cumberland, Maryland.", 8, 396, "/CC525bIX5faQov5KhV1m7w/o.jpg", "/CC525bIX5faQov5KhV1m7w/o.jpg"));

    }

}
