package com.example.hw05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// WeatherApp
// MainActivity
// Issac Helms
public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesListener, CurrentWeatherFragment.CurrentWeatherListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void toCurrentWeather(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void toForecast(Data.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForecastFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }
}