package com.example.covid19.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;
import com.example.covid19.component.recyclerview.CountryAdapter;
import com.example.covid19.model.covid.Country;
import com.example.covid19.model.covid.CountryInfo;
import com.example.covid19.model.user.User;
import com.example.covid19.plugin.retrofit.AuthRetrofit;
import com.example.covid19.plugin.retrofit.CovidRetrofit;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCountryActivity extends BaseActivity {

    public static final String SAVE = "com.example.covid19.listCountry";
    private SharedPreferences sharedPreferences;
    private Boolean flagBookmark = false;

    private RecyclerView recyclerView;
    private CountryAdapter countryAdapter;
    private ArrayList<Country> countries = new ArrayList<>();;

    private JSONArray countryArray = null;

    public static final String EXTRA_COUNTRIES = "extraCountries";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreate: Listcountry");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        // insert shared preference. get user's name
        String user = "User";
        getSupportActionBar().setTitle("Cases by Country ");
        getSupportActionBar().setElevation(0);


        recyclerView = findViewById(R.id.recyclerview);

        Intent i = getIntent();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Country>>() {}.getType();
        ArrayList<Country> obj2 = gson.fromJson(i.getStringExtra(EXTRA_COUNTRIES), type);
        countries = obj2;
        Log.e("TAG", "onCreate: " + countries.size() );
        countryAdapter = new CountryAdapter();
        countryAdapter.setCountries(countries);
        recyclerView.setAdapter(countryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        sharedPreferences = getSharedPreferences(SAVE, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setIconifiedByDefault(true);
        sv.setMaxWidth(Integer.MAX_VALUE);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("TAG", "onQueryTextSubmit: " + s);
                doSearchItems(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("TAG", "onQueryTextSubmit: "+ s);
                if (TextUtils.isEmpty(s)){
                    resetData();
                    return true;
                } else {
                    doSearchItems(s);
                }
                return false;
            }
        });

        ImageView bm = (ImageView) menu.findItem(R.id.bookmark).getActionView();

        bm.setBackgroundColor(getResources().getColor(R.color.normal_red));
        bm.setImageDrawable(getResources().getDrawable(R.drawable.heart));

        bm.setOnClickListener(v -> {
            switchAdapterData(bm);
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                System.out.println(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetData(){
        countryAdapter.setCountries(countries);
        countryAdapter.notifyDataSetChanged();
    }

    private void switchAdapterData(ImageView bm) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BookmarkList", null);
        Type type = new TypeToken<ArrayList<Country>>() {}.getType();
        ArrayList<Country> obj = gson.fromJson(json, type);
        flagBookmark = !flagBookmark;
        if (flagBookmark) {
            bm.setImageDrawable(getDrawable(R.drawable.heart_alt));
            if (obj != null)
                countryAdapter.setCountries(obj);
            else
                countryAdapter.setCountries(new ArrayList<>());
        } else {
            bm.setImageDrawable(getDrawable(R.drawable.heart));
            countryAdapter.setCountries(countries);
        }
        countryAdapter.notifyDataSetChanged();
    }

    private void doSearchItems(String query){
        int foundItem = 0;
        ArrayList<Country> bufferArray = new ArrayList<Country>();
        ArrayList<Country> sourceArray;
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BookmarkList", null);
        Type type = new TypeToken<ArrayList<Country>>() {}.getType();
        ArrayList<Country> obj = gson.fromJson(json, type);
        if (flagBookmark) {
            sourceArray = obj;
        } else {
            sourceArray = countries;
        }
        for (Country country:sourceArray) {
            if (country.getCountry().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                foundItem++;
                bufferArray.add(country);
            }
        }
        countryAdapter.setCountries(bufferArray);
        countryAdapter.notifyDataSetChanged();
        if (foundItem == 0) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.rvConstraintLayout), "Country is not found", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
