package com.example.felixlin.metroexplorer.activity.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felixlin.metroexplorer.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.felixlin.metroexplorer.activity.model.Landmark;

public class LandmarkDetailActivity extends AppCompatActivity {

    Landmark mLandmark;
    @BindView(R.id.ivLandmarkBackdrop)
    ImageView ivLandmarkBackdrop;
    @BindView(R.id.tvOverview)
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Landmark saved as favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLandmark = (Landmark) extras.getSerializable("LANDMARK");
            this.setTitle(mLandmark.getTitle());
            tvOverview.setText(mLandmark.getOverview());

            Picasso.with(this)
                    .load(mLandmark.getBackdropPath())
                    .into(ivLandmarkBackdrop);

        }
    }
}
