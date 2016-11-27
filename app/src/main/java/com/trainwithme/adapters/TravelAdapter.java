package com.trainwithme.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trainwithme.R;
import com.trainwithme.activities.MainActivity;
import com.trainwithme.fragments.PeopleFragment;
import com.trainwithme.fragments.PeopleFragmentBuilder;
import com.trainwithme.models.Travel;
import com.trainwithme.network.ApiManger;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mruga on 19.11.2016.
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.travelViewHolder> {
    private List<Travel> list;
    private Context context;
    private int layoutResId;
    public TravelAdapter(List<Travel> list, int layoutResId, Context context) {
        this.list = list;
        this.layoutResId = layoutResId;
        this.context = context;}

    class travelViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_day)
        TextView startTime;
        @BindView(R.id.tv_final_station)
        TextView finalStation;
        @BindView(R.id.tv_departure_station)
        TextView departureStation;
        @BindView(R.id.main)
        RelativeLayout relativeLayout;
        travelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    @Override
    public travelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layoutResId, parent, false);
        return new travelViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(travelViewHolder holder, int position) {
        final Travel travel = list.get(position);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(ApiManger.DATE_FORMAT);
        DateTime date = formatter.parseDateTime(travel.getStartTime());
        holder.startTime.setText(date.toString());
        holder.finalStation.setText(travel.getFinalStation());
        holder.departureStation.setText(travel.getDepartureStation());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment peopleFragment = new PeopleFragmentBuilder(travel.getId()).build();
                ((MainActivity)context).replaceFragment(peopleFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
