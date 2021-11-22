package com.example.covid19.component.recyclerview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19.model.covid.Country;
import java.util.ArrayList;
import com.example.covid19.R;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            continentName = itemView.findViewById(R.id.continentName);
            activeCaseTextView = itemView.findViewById(R.id.activeCaseTextView);
            countryFlagImageView = itemView.findViewById(R.id.countryFlagImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //arahin ke detail, lempar Country
        }
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }
}
