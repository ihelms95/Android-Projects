package com.example.hw05;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// WeatherApp
// ForecastFragment
// Issac Helms
public class ForecastFragment extends Fragment {

    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private Data.City mCity;
    OkHttpClient client;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ForecastRecyclerViewAdapter adapter;
    CurrentWeather[] forecasts;

    public ForecastFragment() {
        // Required empty public constructor
    }

    public static ForecastFragment newInstance(Data.City city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (Data.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Weather Forecast");
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        client = new OkHttpClient();

        TextView textViewForecastCity = view.findViewById(R.id.textViewForecastCity);
        textViewForecastCity.setText(String.format("%s, %s", mCity.getCity(), mCity.getCountry()));

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openweathermap.org")
                .addPathSegments("data/2.5/forecast")
                .addQueryParameter("q", mCity.getCity() )
                .addQueryParameter("appid", "e3f0632a775638dfdea7fd83291f8029")
                .addQueryParameter("units", "imperial")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {
                if(response.isSuccessful()) {
                    Forecast forecast = new Gson().fromJson(response.body().charStream(), Forecast.class);
                    getActivity().runOnUiThread(() -> {
                        recyclerView = view.findViewById(R.id.forecastRecyclerView);
                        recyclerView.setHasFixedSize(false);
                        layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        forecasts = forecast.getList();
                        adapter = new ForecastRecyclerViewAdapter(forecasts);
                        recyclerView.setAdapter(adapter);
                    });
                }
            }
        });

        return view;
    }
}