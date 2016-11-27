package com.trainwithme.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trainwithme.R;
import com.trainwithme.activities.MainActivity;
import com.trainwithme.adapters.PeopleAdapter;
import com.trainwithme.adapters.TravelAdapter;
import com.trainwithme.models.CurrentTravel;
import com.trainwithme.models.Travel;
import com.trainwithme.network.ApiManger;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {
    @BindView(R.id.rv_planned_travels)
    RecyclerView plannedTravelsRecyclerView;
    ApiManger.ApiInterface apiInterface = ApiManger.getInstance(getContext());
    public MainFragment() {}

    @Override
    int getFragmentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }
    public void getData(){
        Call<List<Travel>> call = apiInterface.getTravels();
        call.enqueue(new Callback<List<Travel>>() {
            @Override
            public void onResponse(Call<List<Travel>> call, Response<List<Travel>> response) {

                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                plannedTravelsRecyclerView.setLayoutManager(llm);
                plannedTravelsRecyclerView.setAdapter(new TravelAdapter(response.body(),R.layout.item_planned_travel,getContext()));
            }

            @Override
            public void onFailure(Call<List<Travel>> call, Throwable t) {

            }
        });
    }
    @OnClick(R.id.bt_find_people)
    void findPeople(){
        Call<CurrentTravel> call = apiInterface.getCurrentTravel();
        call.enqueue(new Callback<CurrentTravel>() {
            @Override
            public void onResponse(Call<CurrentTravel> call, Response<CurrentTravel> response) {
                if(response.code()==400 || response.code()==404){
                    ((MainActivity)getContext()).addFragmentToBackStack(new FormFragment());
                }
                else if(response.code()==200)
                    ((MainActivity)getContext()).addFragmentToBackStack(new PeopleFragment());
            }

            @Override
            public void onFailure(Call<CurrentTravel> call, Throwable t) {

            }
        });
    }
    @OnClick(R.id.bt_plan_travel)
    public void planTravel(){
        ((MainActivity)getContext()).addFragmentToBackStack(new FormFragment());
    }
}
