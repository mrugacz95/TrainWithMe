package com.trainwithme.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.trainwithme.R;
import com.trainwithme.models.AccessTokenModel;
import com.trainwithme.models.RegisterModel;
import com.trainwithme.network.ApiManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private UserLoginTask mAuthTask = null;

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.main_container)
    LinearLayout linearLayout;
    ApiManger.ApiInterface apiInterface;
    CallbackManager callbackManager;
    @BindView(R.id.fb_login)
    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                showProgress(true);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    final String email = object.getString("email");
                                    Call<AccessTokenModel> call = apiInterface.registerExternal(email,loginResult.getAccessToken().getToken(),"Facebook");
                                    call.enqueue(new Callback<AccessTokenModel>() {
                                        @Override
                                        public void onResponse(Call<AccessTokenModel> call, Response<AccessTokenModel> response) {
                                            goToApp(response.body().getAccessToken(),email);
                                        }

                                        @Override
                                        public void onFailure(Call<AccessTokenModel> call, Throwable t) {
                                            showProgress(false);
                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Snackbar.make(linearLayout, "Failed to login", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        apiInterface = ApiManger.getInstance(this);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @OnClick(R.id.debug_login)
    void debug(){
        mEmailView.setText("test@test");
        mPasswordView.setText("test123");
    }
    @OnClick(R.id.email_sign_in_button)
    void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(!isNetworkAvailable())
            cancel = true;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, this);
            mAuthTask.execute((Void) null);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }
    public class UserLoginTask extends AsyncTask<Void, Void, Response<AccessTokenModel>> {

        private final String mEmail;
        private final String mPassword;
        private final Activity activity;

        UserLoginTask(String email, String password, Activity activity) {
            mEmail = email;
            mPassword = password;
            this.activity = activity;
        }

        @Override
        protected Response<AccessTokenModel> doInBackground(Void... params) {
            Call<AccessTokenModel> call= apiInterface.getAccessToken("password",mEmail,mPassword);
            try {
                return call.execute();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<AccessTokenModel> response) {
            mAuthTask = null;
            if (response.errorBody()==null) {
                AccessTokenModel accessToken = response.body();
                goToApp(accessToken.getAccessToken(),mEmail);
            } else {
                showProgress(false);
                try {
                    JSONObject json = new JSONObject(response.errorBody().string());
                    String error = json.getString("error_description");
                    Snackbar.make(linearLayout,error,Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Snackbar.make(linearLayout, "Failed to login", Snackbar.LENGTH_LONG).show();
                }
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void goToApp(String accessToken, String email){

        SharedPreferences sharedPreferences = getSharedPreferences(ApiManger.SHAREDPREFS,MODE_PRIVATE);
        sharedPreferences.edit().putString(ApiManger.USER_TOKEN_PREF,accessToken).apply();
        sharedPreferences.edit().putString(ApiManger.USER_EMAIL,email).apply();
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }
}

