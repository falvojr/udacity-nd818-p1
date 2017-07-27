package com.falvojr.nd818.p2.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;

public class SummaryFragment extends Fragment {

    public SummaryFragment() {
        // Required empty public constructor
        super();
    }

    public static SummaryFragment newInstance() {
        return new SummaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

}
