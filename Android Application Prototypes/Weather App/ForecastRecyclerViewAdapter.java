package com.example.hw05;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.apache.commons.text.WordUtils;

// WeatherApp
// ForecastRecyclerViewAdapter
// Issac Helms
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {
    CurrentWeather[] forecasts;

   public ForecastRecyclerViewAdapter(CurrentWeather[] data){
       this.forecasts = data;

   }



    @NonNull
    @Override
    public ForecastRecyclerViewAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_row, parent, false);


       return new ForecastRecyclerViewAdapter.ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastRecyclerViewAdapter.ForecastViewHolder holder, int position) {
       CurrentWeather currentWeather = forecasts[position];
       holder.textViewForecastDate.setText(String.format("%s", currentWeather.getDt_txt()));
       holder.textViewForecastTemp.setText(String.format("%s F", currentWeather.getMain().getTemp()));
       holder.textViewForecastMaxTemp.setText(String.format("Max: "+ "%s F", currentWeather.getMain().getTemp_max()));
       holder.textViewForecastMinTemp.setText(String.format("Min: " + "%s F", currentWeather.getMain().getTemp_min()));
        holder.textViewForecastHumidity.setText(String.format("Humidity: "+"%s%%", currentWeather.getMain().getHumidity()));
        holder.textViewForecastDesc.setText(WordUtils.capitalize( currentWeather.getWeather()[0].getDescription()));
        Picasso.get().load("http://openweathermap.org/img/wn/" + currentWeather.getWeather()[0].getIcon() + "@2x.png").into(holder.imageViewForecastWeatherIcon);



    }

    @Override
    public int getItemCount() {
        return this.forecasts.length;
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder{
        TextView textViewForecastDate;

        TextView textViewForecastTemp;
        TextView textViewForecastMaxTemp;
        TextView textViewForecastMinTemp;
        TextView textViewForecastHumidity;
        TextView textViewForecastDesc;
        ImageView imageViewForecastWeatherIcon;





        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewForecastWeatherIcon = itemView.findViewById(R.id.imageViewForecastWeatherIcon);
            textViewForecastDate = itemView.findViewById(R.id.textViewForecastDate);

            textViewForecastTemp = itemView.findViewById(R.id.textViewForecastTemp);
            textViewForecastMaxTemp = itemView.findViewById(R.id.textViewForecastMaxTemp);
            textViewForecastMinTemp = itemView.findViewById(R.id.textViewForecastMinTemp);
            textViewForecastHumidity = itemView.findViewById(R.id.textViewForecastHumidity);
            textViewForecastDesc = itemView.findViewById(R.id.textViewForecastDesc);



        }
    }



}
