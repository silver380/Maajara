package ir.blackswan.travelapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.Views.WebImageView;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder> {
     List<Place> places;

     public PlacesRecyclerAdapter(List<Place> places) {
          this.places = places;
     }

     @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          return null;
     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          holder.placeImage.setImagePath(places.get(position).getPicturePath());
     }

     @Override
     public int getItemCount() {
          return places.size();
     }

     class ViewHolder extends RecyclerView.ViewHolder{
          WebImageView placeImage;
          public ViewHolder(@NonNull View itemView) {
               super(itemView);

          }
     }
}