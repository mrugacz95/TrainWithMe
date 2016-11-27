package com.trainwithme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trainwithme.R;
import com.trainwithme.activities.MainActivity;
import com.trainwithme.fragments.ChatFragment;
import com.trainwithme.models.UserInTrain;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by mruga on 19.11.2016.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.peopleViewHolder> {
        private List<UserInTrain> list;
        private Context context;
        private int layoutResId;
        public PeopleAdapter(List<UserInTrain> list, int layoutResId, Context context) {
            this.list = list;
            this.layoutResId = layoutResId;
            this.context = context;}

        class peopleViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.username)
            TextView username;
            @BindView(R.id.user_icon)
            ImageView userIcon;
            @BindView(R.id.finalLocation)
            TextView finalStation;
            @BindView(R.id.startLocation)
            TextView departureStation;
            @BindView(R.id.main)
            RelativeLayout mainLayout;
            peopleViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
        @Override
        public peopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(layoutResId, parent, false);
            return new peopleViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(peopleViewHolder holder, int position) {
            UserInTrain user = list.get(position);
            holder.username.setText(user.getUserName());
            holder.departureStation.setText(user.getStartAddress());
            holder.finalStation.setText(user.getEndAddress());
            if (user.getEncodedAvatar() != null) {
                byte[] imageByteArray = Base64.decode(user.getEncodedAvatar(), Base64.DEFAULT);
                Glide.with(context).load(imageByteArray).bitmapTransform(new CropCircleTransformation(context)).into(holder.userIcon);
            }
            else {
                Glide.with(context).load(R.drawable.profile).bitmapTransform(new CropCircleTransformation(context)).into(holder.userIcon);
            }
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)context).replaceFragment(new ChatFragment());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

}
