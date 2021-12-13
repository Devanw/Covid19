package com.example.covid19.view;

import static android.content.ContentValues.TAG;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.covid19.BaseActivity;
import com.example.covid19.MainActivity;
import com.example.covid19.R;
import com.example.covid19.api.service.RetroServerCorona;
import com.example.covid19.databinding.HomepageBinding;
import com.example.covid19.model.covid.Country;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    public static final String EXTRA_ALL_COVID_INFO = "extraAllCovidInfo";

    private Button navToListCountryBtn;
    private Button myProfileBtn;
    private TextView allCountryTotalCases;
    private TextView allCountryActiveCases;
    private TextView allCountryRecoveries;
    private TextView allCountryTests;

    private String TAG = "HomeActivity";

    private HomepageBinding binding;

    private JSONObject allcovidinfo = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //kalau mau fragment
//        setContentView(R.layout.activity_main);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container_fragment, HomeFragmentManager.newInstance())
//                .commitNow();
        binding = HomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initExtra();
        initView();

        // insert shared preference. get user's name
        SharedPreferences userSp = getSharedPreferences(AuthActivity.USERINFO, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userSp.getString("Userinfo", null);
        Type type = new TypeToken<User>() {}.getType();
        User userFromSp = gson.fromJson(json, type);

        String user;

        try {
            user = userFromSp.getUsername();
        } catch (Exception e) {
            user = "User";
        }


        getSupportActionBar().setTitle("Welcome " + user);
        getSupportActionBar().setElevation(0);


        myProfileBtn = findViewById(R.id.myProfileBtn);
        myProfileBtn.setOnClickListener(v-> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });


        navToListCountryBtn = findViewById(R.id.navBtnList);
        navToListCountryBtn.setOnClickListener(v -> {
            goToListCountry();
        });
    }

    private void goToListCountry() {
        RetroServerCorona.getInstance().getCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                Log.d("getCountries success : ", ""+ response.code() + "\n" + new Gson().toJson(response.body()));
                Intent intent = new Intent(HomeActivity.this, ListCountryActivity.class);
                intent.putExtra(ListCountryActivity.EXTRA_COUNTRIES, new Gson().toJson(response.body()));
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("getCountries fail : ", t.getMessage());
                showMessage(t.getMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToListCountry();
                    }
                });
            }
        });
    }

    private void initView() {
        try {
            if (allcovidinfo != null) {
                binding.allCountryTotalValue.setText(allcovidinfo.has("updated") ? allcovidinfo.getString("updated") : "NaN");
                binding.allRecoveredValue.setText(allcovidinfo.has("recovered") ? allcovidinfo.getString("recovered") : "NaN");
                binding.allActiveValue.setText(allcovidinfo.has("cases") ? allcovidinfo.getString("cases") : "NaN");
                binding.allTestValue.setText(allcovidinfo.has("tests") ? allcovidinfo.getString("cases") : "tests");
            } else {
                showMessage("No Data !");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            showMessage(e.getMessage());
        }
    }

    private void initExtra() {
        try {
            if (getIntent().hasExtra(EXTRA_ALL_COVID_INFO)) {
                allcovidinfo = new JSONObject(getIntent().getStringExtra(EXTRA_ALL_COVID_INFO));
                Log.d(TAG, allcovidinfo.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.toolbar_menu, menu);
//
//        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
//        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
//        sv.setIconifiedByDefault(true);
//        sv.setMaxWidth(Integer.MAX_VALUE);
//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Log.e("TAG", "onQueryTextSubmit: " + s);
//                doSearchItems(s);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.e("TAG", "onQueryTextSubmit: "+ s);
//                if (TextUtils.isEmpty(s)){
//                    resetData();
//                    return true;
//                } else {
//                    doSearchItems(s);
//                }
//                return false;
//            }
//        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item);
        switch (item.getItemId()){
            case R.id.search:
                System.out.println(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
