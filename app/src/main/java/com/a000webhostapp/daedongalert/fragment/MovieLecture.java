package com.a000webhostapp.daedongalert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a000webhostapp.daedongalert.daedongalert.R;

public class MovieLecture extends Fragment {

    public static MovieLecture newInstance() {
        return new MovieLecture();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }
}
