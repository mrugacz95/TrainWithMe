package com.trainwithme.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.trainwithme.models.AccessTokenModel;
import com.trainwithme.models.CurrentTravel;
import com.trainwithme.models.PeopleInTrain;
import com.trainwithme.models.RegisterForTrainModel;
import com.trainwithme.models.RegisterModel;
import com.trainwithme.models.Travel;
import com.trainwithme.models.UserInTrain;
import com.trainwithme.models.UserInfo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mruga on 19.11.2016.
 */

public class ApiManger {


    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String BASE_URL = "http://backendtrains.azurewebsites.net/";
    public static final String USER_TOKEN_PREF = "TOKEN_PREF";
    public static final String USER_EMAIL = "USER_EMAIL";
    private static SharedPreferences sharedPreferences;
    private static ApiInterface client;
    public static final String SHAREDPREFS="SHARED_PREFS";

    public static ApiInterface getInstance(Context context) {
        if(client!=null)
            return client;
        sharedPreferences = context.getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>(){
                    @Override
                    public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(json.toString(DATE_FORMAT));
                    }
                })
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createHttpClient())
                .build();
        client =  retrofit.create(ApiInterface.class);
        return client;

    }
    private static OkHttpClient createHttpClient() {
        Interceptor authInterceptor = new Interceptor() {
                       @Override
            public Response intercept(Chain chain) throws IOException {
                String token = sharedPreferences.getString(USER_TOKEN_PREF, null);
                if (token == null || token.isEmpty()) {
                    return chain.proceed(chain.request());
                }
                //add token header
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer" + " " + token).build();

                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }
    public interface ApiInterface{
        @FormUrlEncoded
        @POST("/Token")
        Call<AccessTokenModel> getAccessToken(@Field("grant_type") String grantType,
                                              @Field("username") String userName,
                                              @Field("password") String passowrd);
        @POST("/Account/Register")
        Call<ResponseBody> register(@Body RegisterModel registerModel);
        @POST("api/Travel/Register")
        Call<ResponseBody> registerForTrain(@Body RegisterForTrainModel registerModel);

        @POST("api/Travel/GetTrain")
        Call<ResponseBody> getTrain(@Body RegisterForTrainModel trainModel);

        @GET("api/Travel/GetPeople")
        Call<PeopleInTrain> getPeople();

        @GET("api/Travel/GetPeople/{travelId}")
        Call<PeopleInTrain> getPeople(@Path("travelId") String travelId);

        @GET("api/Travel/Current")
        Call<CurrentTravel> getCurrentTravel();

        @GET("api/Travel/MyTravels")
        Call<List<Travel>> getTravels();

        @POST("api/Account/RegisterExternalToken")
        @FormUrlEncoded
        Call<AccessTokenModel> registerExternal(@Field("Email") String email,
                                            @Field("Token") String token,
                                            @Field("Provider") String provider);

        @GET("api/Account/UserInfo")
        Call<UserInfo> getUserInfo();

        @POST("api/Users/Avatar")
        Call<ResponseBody> getAvater();
    }


}
