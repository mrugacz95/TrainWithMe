package com.trainwithme.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.trainwithme.R;
import com.trainwithme.activities.MainActivity;
import com.trainwithme.models.RegisterForTrainModel;
import com.trainwithme.network.ApiManger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends BaseFragment implements  TimePicker.OnTimeChangedListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.time_picker)
    TimePicker timePicker;
    @BindView(R.id.tv_departure_station)
    EditText departureStation;
    @BindView(R.id.tv_final_station)
    EditText finalStation;
    @BindView(R.id.string_date)
    TextView stringDate;
    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
    ApiManger.ApiInterface apiInterface;
    RegisterForTrainModel registerModel = new RegisterForTrainModel();
    public FormFragment() {  }

    @Override
    int getFragmentLayoutId() {
        return R.layout.fragment_form;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         timePicker.setOnTimeChangedListener(this);
        timePicker.setIs24HourView(true);
        registerModel.setWhen(new DateTime());
        stringDate.setText(new DateTime().toString(formatter));


    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        DateTime dateTime = registerModel.getWhen();
        dateTime = dateTime.hourOfDay().setCopy(hourOfDay);
        dateTime = dateTime.minuteOfDay().setCopy(minute);
        registerModel.setWhen(dateTime);
    }
    @OnClick(R.id.bt_check)
    public void check(){
        if(departureStation.getText().toString().isEmpty() || finalStation.getText().toString().isEmpty()) {
            showSnackBar("Please, fill in the form!");
            return;
        }
        apiInterface = ApiManger.getInstance(getContext());
        registerModel.setFrom(departureStation.getText().toString());
        registerModel.setTo(finalStation.getText().toString());
        Call<ResponseBody> call = apiInterface.registerForTrain(registerModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.code()==400){
                    ((MainActivity)getContext()).replaceFragment(new PeopleFragment());
                }
                else {
                    showSnackBar(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBar(t.getMessage());
            }
        });


    }
    @OnClick(R.id.set_date)
    public void setDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                registerModel.getWhen().getYear(),
                registerModel.getWhen().getMonthOfYear()-1,
                registerModel.getWhen().getDayOfMonth()
        );
        dpd.show(((MainActivity)getContext()).getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DateTime date = new DateTime();
        date = date.year().setCopy(year);
        date = date.monthOfYear().setCopy(monthOfYear+1);
        date = date.dayOfMonth().setCopy(dayOfMonth);
        stringDate.setText(date.toString(formatter));
    }
}
