package com.a000webhostapp.daedongalert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a000webhostapp.daedongalert.daedongalert.R;

public class Exam extends Fragment {

    public static Exam newInstance() {
        return new Exam();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }
}
