package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.WebImageView;
import ir.blackswan.travelapp.ui.Dialogs.PlaceDialog;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder> {
    Place[] places;
    HashSet<Place> selectedPlaces = new HashSet<>();
    Activity activity;
    boolean forSelect;

    public PlacesRecyclerAdapter(Activity activity, Place[] places) {
        this(activity, places, false);
    }

    public PlacesRecyclerAdapter(Activity activity, Place[] places, boolean forSelect) {
        this.activity = activity;
        this.places = places;
        this.forSelect = forSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.place_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places[position];
        holder.placeImage.setImagePath(FakeData.getRandomImagePath());
        holder.placeImage.setGradient(true);
        holder.placeName.setText(place.getName());
        holder.selectView.setVisibility(selectedPlaces.contains(place) ?
                View.VISIBLE : View.GONE);


        holder.itemView.setOnClickListener(v -> {
            if (forSelect) {
                selectedPlaces.add(place);
                holder.selectView.setVisibility(View.VISIBLE);
            } else
                new PlaceDialog(activity, place).show();
        });
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        WebImageView placeImage;
        TextView placeName;
        View selectView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_vh_iv);
            placeName = itemView.findViewById(R.id.place_vh_tv);
            selectView = itemView.findViewById(R.id.place_vh_select);
        }
    }
}