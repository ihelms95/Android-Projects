package com.example.hw05;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// WeatherApp
// Forecast
// Issac Helms
public class Forecast {

    private CurrentWeather[] list;

    public CurrentWeather[] getList() {
        return list;
    }

    @NotNull
    @Override
    public String toString() {
        return "Forecast{" +
                "list=" + Arrays.toString(list) +
                '}';
    }
}
