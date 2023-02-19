package com.example.hw05;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

// WeatherApp
// CurrentWeather
// Issac Helms
public class CurrentWeather {

    private Weather[] weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private String dt_txt;

    public Weather[] getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    static class Weather {
        private String description, icon;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        @NotNull
        @Override
        public String toString() {
            return "Weather{" +
                    "description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }

    static class Main {
        private String temp, temp_min, temp_max, humidity;

        public String getTemp() {
            return temp;
        }

        public String getTemp_min() {
            return temp_min;
        }

        public String getTemp_max() {
            return temp_max;
        }

        public String getHumidity() {
            return humidity;
        }

        @NotNull
        @Override
        public String toString() {
            return "Main{" +
                    "temp='" + temp + '\'' +
                    ", temp_min='" + temp_min + '\'' +
                    ", temp_max='" + temp_max + '\'' +
                    ", humidity='" + humidity + '\'' +
                    '}';
        }
    }

    static class Wind {
        private String speed, deg;

        public String getSpeed() {
            return speed;
        }

        public String getDeg() {
            return deg;
        }

        @NotNull
        @Override
        public String toString() {
            return "Wind{" +
                    "speed='" + speed + '\'' +
                    ", deg='" + deg + '\'' +
                    '}';
        }
    }

    static class Clouds {
        private String all;

        public String getAll() {
            return all;
        }

        @NotNull
        @Override
        public String toString() {
            return "Clouds{" +
                    "all='" + all + '\'' +
                    '}';
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "CurrentWeather{" +
                "weather=" + Arrays.toString(weather) +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                '}';
    }
}
