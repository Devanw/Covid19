package com.example.covid19.component.recyclerview;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.model.covid.Country;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.example.covid19.R;
import com.example.covid19.view.HomeActivity;
import com.example.covid19.view.ListCountryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private ArrayList<Country> countries;

    public static final String SAVE = "com.example.covid19.listCountry";
    private SharedPreferences sharedPreferences;

    public CountryAdapter(){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        sharedPreferences = context.getSharedPreferences(SAVE, Context.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.countryName.setText(country.getCountry());
        holder.continentName.setText(country.getContinent());
        holder.activeCaseTextView.setText(country.getActive().toString());
        holder.country = country;
        Picasso.get()
                .load(country.getCountryInfo().getFlag())
                .into(holder.countryFlagImageView);

        holder.caseValue.setText(country.getCases().toString());
        holder.todayCaseValue.setText(country.getTodayCases().toString());
        holder.deathValue.setText(country.getDeaths().toString());
        holder.todayDeathValue.setText(country.getTodayDeaths().toString());
        holder.recoveredValue.setText(country.getRecovered().toString());
        holder.todayRecoveredValue.setText(country.getTodayRecovered().toString());
        holder.activeValue.setText(country.getActive().toString());
        holder.criticalValue.setText(country.getCritical().toString());
        holder.testValue.setText(country.getTests().toString());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView countryName;
        TextView continentName;
        TextView activeCaseTextView;
        ImageView countryFlagImageView;
        CardView detailCardView;
        ImageView iconBookmark;
        Country country;

        //bagian detail
        TextView caseValue;
        TextView todayCaseValue;
        TextView deathValue;
        TextView todayDeathValue;
        TextView recoveredValue;
        TextView todayRecoveredValue;
        TextView activeValue;
        TextView criticalValue;
        TextView testValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            continentName = itemView.findViewById(R.id.continentName);
            activeCaseTextView = itemView.findViewById(R.id.activeCaseTextView);
            countryFlagImageView = itemView.findViewById(R.id.countryFlagImageView);
            detailCardView = itemView.findViewById(R.id.cardViewDetail);
            iconBookmark = itemView.findViewById(R.id.bookmarkIconCard);
            itemView.setOnClickListener(v -> {
                if (detailCardView.getVisibility() == View.GONE ) {
                    detailCardView.setVisibility(View.VISIBLE);
                } else {
                    detailCardView.setVisibility(View.GONE);
                }
            });

            caseValue = itemView.findViewById(R.id.caseValue);
            todayCaseValue = itemView.findViewById(R.id.todayCaseValue);
            deathValue = itemView.findViewById(R.id.deathValue);
            todayDeathValue = itemView.findViewById(R.id.todayDeathValue);
            recoveredValue = itemView.findViewById(R.id.recoveredValue);
            todayRecoveredValue = itemView.findViewById(R.id.todayRecoveredValue);
            activeValue = itemView.findViewById(R.id.activeValue);
            criticalValue = itemView.findViewById(R.id.criticalValue);
            testValue = itemView.findViewById(R.id.testValue);

            //read file
            Gson gson = new Gson();
            String json = sharedPreferences.getString("BookmarkList", null);
            Type type = new TypeToken<ArrayList<Country>>() {}.getType();
            ArrayList<Country> obj = gson.fromJson(json, type);
            if (obj != null) { //replace ke check countryname ada di sharedpref bookmark / ga
                for (int i = obj.size() - 1; i >= 0; i--) {
                    //filter according to owner of clicked card
                    if (obj.get(i).getCountry().equals(country.getCountry())) {
                        iconBookmark.setImageResource(android.R.drawable.star_big_on);
                    } else {
                        iconBookmark.setImageResource(android.R.drawable.star_big_off);
                    }
                }
            }

            iconBookmark.setOnClickListener(v -> {
                if (obj != null) { //replace ke check countryname ada di sharedpref bookmark / ga
                    for (int i = obj.size()-1; i >= 0; i--) {
                        //filter according to owner of clicked card
                        if(obj.get(i).getCountry().equals(country.getCountry())){
                            obj.remove(i);
                            iconBookmark.setImageResource(android.R.drawable.star_big_off);
                        } else {
                            obj.add(country);
                            iconBookmark.setImageResource(android.R.drawable.star_big_on);
                        }
                    }
                    //write to sharedpref
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    String writeData = gson.toJson(obj);
                    prefsEditor.putString("BookmarkList", writeData);
                    prefsEditor.commit();
                }
            });
        }

        @Override
        public void onClick(View v) {
            //arahin ke detail, lempar Country
            detailCardView.setVisibility(View.VISIBLE);
        }
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }
}
