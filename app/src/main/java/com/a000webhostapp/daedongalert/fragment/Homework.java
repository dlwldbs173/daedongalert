package com.a000webhostapp.daedongalert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a000webhostapp.daedongalert.daedongalert.R;

public class Homework extends Fragment {

    public static Homework newInstance() {
        return new Homework();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homework_fragment, container, false);
    }
}
