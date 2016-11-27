package com.trainwithme.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trainwithme.R;
import com.trainwithme.models.UserInfo;
import com.trainwithme.network.ApiManger;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {
    @BindView(R.id.user_login)
    TextView userLogin;
    @BindView(R.id.user_email)
            TextView email;
    @BindView(R.id.user_avatar)
    ImageView userAvatar;
    ApiManger.ApiInterface apiInterface;
    @Override
    int getFragmentLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiManger.getInstance(getContext());
        getDate();
    }
    public void getDate(){
        Call<UserInfo> call = apiInterface.getUserInfo();
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                UserInfo userInfo = response.body();
                email.setText(userInfo.getEmail());
                String[] split = userInfo.getEmail().split("@");
                userLogin.setText(split[0]);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
        Call<ResponseBody> avatarCall = apiInterface.getAvater();
        avatarCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Glide.with(getContext()).load(response.body()).error(R.drawable.profile).bitmapTransform(new CropCircleTransformation(getContext())).into(userAvatar);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
