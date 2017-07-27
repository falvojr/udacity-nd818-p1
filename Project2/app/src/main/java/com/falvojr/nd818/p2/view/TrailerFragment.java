package com.falvojr.nd818.p2.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.falvojr.nd818.p2.R;
import com.falvojr.nd818.p2.databinding.FragmentTrailerBinding;

public class TrailerFragment extends Fragment {

    public TrailerFragment() {
        // Required empty public constructor
        super();
    }

    public static TrailerFragment newInstance() {
        return new TrailerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentTrailerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trailer, container, false);
        return binding.getRoot();
    }

}
