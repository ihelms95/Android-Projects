package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// WeatherApp
// CitiesFragment
// Issac Helms
public class CitiesFragment extends Fragment {

    ListView citiesListView;
    ArrayAdapter<Data.City> adapter;
    CitiesListener listener;

    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Cities");
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        citiesListView = view.findViewById(R.id.citiesListView);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, Data.cities);
        citiesListView.setAdapter(adapter);

        citiesListView.setOnItemClickListener((parent, view1, position, id) -> listener.toCurrentWeather(Data.cities.get(position)));
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CitiesFragment.CitiesListener) {
            listener = (CitiesFragment.CitiesListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CitiesListener");
        }
    }

    interface CitiesListener {
        void toCurrentWeather(Data.City city);
    }
}