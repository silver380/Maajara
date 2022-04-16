package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.WebImageView;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder> {
     List<Place> places;
     Activity activity;
     public PlacesRecyclerAdapter(Activity activity, List<Place> places) {
          this.activity = activity;
          this.places = places;
     }

     @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View view = activity.getLayoutInflater().inflate(R.layout.place_view_holder , parent , false);
          return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Place place = places.get(position);
          holder.placeImage.setImagePath(places.get(position).getPicturePath());
          holder.placeName.setText(place.getName());
     }

     @Override
     public int getItemCount() {
          return places.size();
     }

     class ViewHolder extends RecyclerView.ViewHolder{
          WebImageView placeImage;
          TextView placeName;
          public ViewHolder(@NonNull View itemView) {
               super(itemView);
               placeImage = itemView.findViewById(R.id.place_vh_iv);
               placeName = itemView.findViewById(R.id.place_vh_tv);
          }
     }
}