package com.trainwithme.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.trainwithme.R;
import com.trainwithme.adapters.PeopleAdapter;
import com.trainwithme.models.PeopleInTrain;
import com.trainwithme.models.UserInTrain;
import com.trainwithme.network.ApiManger;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@FragmentWithArgs
public class PeopleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    ApiManger.ApiInterface apiInterface;
    @Arg
    String travelId;
    @BindView(R.id.rv_people)
    RecyclerView peopleRecView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_results)
    TextView noResults;
    @Override
    int getFragmentLayoutId() {
        return R.layout.fragment_people;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiManger.getInstance(context);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if(travelId==null)
            getCurrentTravelers();
        else
            getPeople();
    }

    private void getPeople() {
        Call<PeopleInTrain> call = apiInterface.getPeople(travelId);
        call.enqueue(new Callback<PeopleInTrain>() {
            @Override
            public void onResponse(Call<PeopleInTrain> call, Response<PeopleInTrain> response) {
                onPeopleResponse(response);
            }

            @Override
            public void onFailure(Call<PeopleInTrain> call, Throwable t) {
                onPeopleFailure(t);
            }
        });
    }
    public void onPeopleFailure(Throwable t){
        noResults.setVisibility(View.VISIBLE);
        showSnackBar(t.getMessage());
        swipeRefreshLayout.setRefreshing(false);
    }
    public void onPeopleResponse(Response<PeopleInTrain> response){
        if(response.errorBody()==null){
            if(response.body().getUserInTrain().isEmpty())
                noResults.setVisibility(View.VISIBLE);
            else {
                noResults.setVisibility(View.GONE);
                PeopleAdapter peopleAdapter = new PeopleAdapter(response.body().getUserInTrain(), R.layout.item_people, getContext());
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                peopleRecView.setLayoutManager(llm);
                peopleRecView.setAdapter(peopleAdapter);
                peopleAdapter.notifyDataSetChanged();
            }
        }
        else
            noResults.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void getCurrentTravelers() {
        Call<PeopleInTrain> call = apiInterface.getPeople();
        call.enqueue(new Callback<PeopleInTrain>() {
            @Override
            public void onResponse(Call<PeopleInTrain> call, Response<PeopleInTrain> response) {
                onPeopleResponse(response);
            }

            @Override
            public void onFailure(Call<PeopleInTrain> call, Throwable t) {
                onPeopleFailure(t);
            }
        });
    }
}
