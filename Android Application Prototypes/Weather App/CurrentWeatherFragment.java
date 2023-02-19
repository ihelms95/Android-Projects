package com.example.hw05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// WeatherApp
// CurrentWeatherFragment
// Issac Helms
public class CurrentWeatherFragment extends Fragment {

    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private Data.City mCity;

    CurrentWeatherListener listener;
    OkHttpClient client;

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(Data.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
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
        getActivity().setTitle("Current Weather");
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        client = new OkHttpClient();

        TextView textViewCurrCityName = view.findViewById(R.id.textViewCurrCityName);
        TextView textViewCurrTemp = view.findViewById(R.id.textViewCurrTemp);
        TextView textViewCurrTempMax = view.findViewById(R.id.textViewCurrTempMax);
        TextView textViewCurrTempMin = view.findViewById(R.id.textViewCurrTempMin);
        TextView textViewCurrDescription = view.findViewById(R.id.textViewCurrDescription);
        TextView textViewCurrHumidity = view.findViewById(R.id.textViewCurrHumidity);
        TextView textViewCurrWindSpeed = view.findViewById(R.id.textViewCurrWindSpeed);
        TextView textViewCurrWindDegree = view.findViewById(R.id.textViewCurrWindDegree);
        TextView textViewCurrCloudiness = view.findViewById(R.id.textViewCurrCloudiness);
        ImageView currentWeatherImageView = view.findViewById(R.id.currentWeatherImageView);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openweathermap.org")
                .addPathSegments("data/2.5/weather")
                .addQueryParameter("q", mCity.getCity() + "," + mCity.getCountry())
                .addQueryParameter("appid", "6738a87bced4645e74ca39c1b52d23d4")
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
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    CurrentWeather weather = new Gson().fromJson(response.body().charStream(), CurrentWeather.class);
                    getActivity().runOnUiThread(() -> {
                        textViewCurrCityName.setText(String.format("%s, %s", mCity.getCity(), mCity.getCountry()));
                        textViewCurrTemp.setText(String.format("%s F", weather.getMain().getTemp()));
                        textViewCurrTempMax.setText(String.format("%s F", weather.getMain().getTemp_max()));
                        textViewCurrTempMin.setText(String.format("%s F", weather.getMain().getTemp_min()));
                        textViewCurrDescription.setText(WordUtils.capitalize(weather.getWeather()[0].getDescription()));
                        textViewCurrHumidity.setText(String.format("%s%%", weather.getMain().getHumidity()));
                        textViewCurrWindSpeed.setText(String.format("%s miles/hr", weather.getWind().getSpeed()));
                        textViewCurrWindDegree.setText(String.format("%s degrees", weather.getWind().getDeg()));
                        textViewCurrCloudiness.setText(String.format("%s%%", weather.getClouds().getAll()));
                        Picasso.get().load("http://openweathermap.org/img/wn/" + weather.getWeather()[0].getIcon() + "@2x.png").into(currentWeatherImageView);
                    });
                }
            }
        });

        view.findViewById(R.id.forecastButton).setOnClickListener(v -> listener.toForecast(mCity));

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CurrentWeatherFragment.CurrentWeatherListener) {
            listener = (CurrentWeatherFragment.CurrentWeatherListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CurrentWeatherListener");
        }
    }

    interface CurrentWeatherListener {
        void toForecast(Data.City city);
    }
}