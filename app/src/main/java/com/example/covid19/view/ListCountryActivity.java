package com.example.covid19.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.BaseActivity;
import com.example.covid19.R;
import com.example.covid19.component.recyclerview.CountryAdapter;
import com.example.covid19.model.covid.Country;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class ListCountryActivity extends BaseActivity {

    private Boolean flagBookmark;

    private RecyclerView recyclerView;
    private CountryAdapter countryAdapter;
    private ArrayList<Country> countries = new ArrayList<>();;

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

    private void resetData(){
        countryAdapter.setCountries(countries);
        countryAdapter.notifyDataSetChanged();
    }

    private void doSearchItems(String query){
        int foundItem = 0;
        ArrayList<Country> bufferArray = new ArrayList<Country>();
        for (Country country:countries) {
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
