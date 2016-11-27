package com.trainwithme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trainwithme.R;
import com.trainwithme.fragments.AboutFragment;
import com.trainwithme.fragments.ChatFragment;
import com.trainwithme.fragments.MainFragment;
import com.trainwithme.fragments.ProfileFragment;
import com.trainwithme.fragments.TravelHistoryFragment;
import com.trainwithme.network.ApiManger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager mFragmentManager;
    @BindView(R.id.fragment_container)
    FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(ApiManger.SHAREDPREFS,MODE_PRIVATE);
        String email = sharedPreferences.getString(ApiManger.USER_EMAIL,"");
        TextView userName =(TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        String[] nick = email.split("@");
        userName.setText(nick[0]);
        TextView emailTv =(TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        emailTv.setText(email);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fm.getBackStackEntryCount()>0) {

            if(!(getCurrentFragment() instanceof MainFragment)) {
                replaceFragment(new MainFragment());
            }
            else
                popFragment();
        } else {
            Snackbar.make(container, R.string.sure_exit,Snackbar.LENGTH_LONG)
                    .setAction(R.string.exit,new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    })
                    .show();
        }
    }
    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager
                .findFragmentByTag(fragmentTag);
        return currentFragment;
    }
    public void addFragmentToBackStack(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }
    public void popFragment(){
        mFragmentManager.popBackStack();
    }
    public void replaceFragment(Fragment fragment){
        for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
            mFragmentManager.popBackStack();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main) {
            replaceFragment(new MainFragment());
        } else if (id == R.id.plan_travel) {
            replaceFragment(new MainFragment());
        } else if (id == R.id.chat) {
            replaceFragment(new ChatFragment());

        } else if (id == R.id.travel_history) {
            replaceFragment(new TravelHistoryFragment());
        } else if (id == R.id.profile) {
            replaceFragment(new ProfileFragment());
        } else if (id == R.id.about) {
            replaceFragment(new AboutFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toggle(View v){
        ((CheckedTextView)v).toggle();
    }
}
