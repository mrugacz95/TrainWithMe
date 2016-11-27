package com.trainwithme.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.trainwithme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mruga on 19.11.2016.
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;

    View view;

    public BaseFragment(){ }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getFragmentLayoutId(), container, false);
        context = view.getContext();
        ButterKnife.bind(this, view);
        return view;
    }
    abstract int getFragmentLayoutId();

    void showSnackBar(String message){
        Snackbar.make(view, message,Snackbar.LENGTH_LONG).show();
    }
}

