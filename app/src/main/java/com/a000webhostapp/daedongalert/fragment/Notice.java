package com.a000webhostapp.daedongalert.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.a000webhostapp.daedongalert.daedongalert.R;

public class Notice extends Fragment {



    public static Notice newInstance() {
        return new Notice();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.notice_fragment, container, false);

        return view;
    }
}
