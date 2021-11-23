package com.example.covid19.component.recyclerview;


import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import com.example.covid19.R;
import com.example.covid19.view.HomeActivity;
import com.example.covid19.view.ListCountryActivity;
import com.squareup.picasso.Picasso;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private ArrayList<Country> countries;

    public CountryAdapter(){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
        holder.activeCaseTextView.setText(country.getActive());
        Picasso.get()
                .load(country.getCountryInfo().getFlag())
                .into(holder.countryFlagImageView);
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
            iconBookmark.setOnClickListener(v -> {
                if (true) { //replace ke check countryname ada di sharedpref bookmark / ga
                    iconBookmark.setImageResource(android.R.drawable.star_big_off);
                } else {
                    iconBookmark.setImageResource(android.R.drawable.star_big_on);
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
