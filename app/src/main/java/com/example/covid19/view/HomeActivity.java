package com.example.covid19.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import com.example.covid19.R;
import com.example.covid19.model.covid.Country;
import com.example.covid19.plugin.sessionmanager.SessionManagerUtil;

public class HomeActivity extends BaseActivity {

    private Button navToListCountryBtn;
    private Button logoutBtn;
    private TextView allCountryTotalCases;
    private TextView allCountryActiveCases;
    private TextView allCountryRecoveries;
    private TextView allCountryTests;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //kalau mau fragment
//        setContentView(R.layout.activity_main);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container_fragment, HomeFragmentManager.newInstance())
//                .commitNow();

        setContentView(R.layout.homepage);

        // insert shared preference. get user's name
        String user = "User";
        getSupportActionBar().setTitle("Welcome " + user);
        getSupportActionBar().setElevation(0);




        navToListCountryBtn = findViewById(R.id.navBtnList);
        navToListCountryBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListCountryActivity.class);
            startActivity(intent);
        });
        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(v -> {
            SessionManagerUtil.getInstance().endUserSession(v.getContext());
            Intent intent = new Intent(v.getContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
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
