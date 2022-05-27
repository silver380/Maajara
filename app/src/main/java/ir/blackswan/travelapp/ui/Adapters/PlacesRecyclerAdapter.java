package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

import ir.blackswan.travelapp.Data.Place;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Views.WebImageView;
import ir.blackswan.travelapp.ui.Dialogs.PlaceDialog;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<PlacesRecyclerAdapter.ViewHolder>
implements HasArray<Place>{
    Place[] places;
    HashSet<Place> selectedPlaces;
    Activity activity;
    boolean forSelect;

    public PlacesRecyclerAdapter(Activity activity, Place[] places) {
        this(activity, places, false, null);
    }

    public void setPlaces(Place[] places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public PlacesRecyclerAdapter(Activity activity, Place[] places, boolean forSelect, HashSet<Place> selectedPlaces) {
        this.activity = activity;
        this.places = places;
        this.forSelect = forSelect;
        if (forSelect) {
            this.selectedPlaces = selectedPlaces == null ? new HashSet<>() : selectedPlaces;
        }
    }


    public HashSet<Place> getSelectedPlacesHashset() {
        return selectedPlaces;
    }

    public Place[] getSelectedPlacesArray() {
        return selectedPlaces.toArray(
                new Place[0]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.place_view_holder, parent, false);
        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int size = (int) (parent.getWidth() * 45f / 100);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = size;
                params.width = size;
                view.setLayoutParams(params);
                parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places[position];
        holder.placeImage.setImagePath(place.getPicture());
        holder.placeImage.setGradient(true);
        holder.placeImage.setScale(.5f);
        holder.placeName.setText(place.getName());
        if (forSelect) {
            boolean b = selectedPlaces.contains(place);
            holder.selectView.setVisibility(b ? View.VISIBLE : View.GONE);
            Log.d("SearchSelectedPlaces", "onBindViewHolder: " + selectedPlaces + " " + place);
        }else {
            holder.selectView.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(v -> {
            if (forSelect) {
                if (selectedPlaces.contains(place)) {
                    selectedPlaces.remove(place);
                    holder.selectView.setVisibility(View.GONE);
                } else {
                    selectedPlaces.add(place);
                    holder.selectView.setVisibility(View.VISIBLE);
                }
            } else
                new PlaceDialog(activity, place).show();
        });
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    @Override
    public Place[] getData() {
        return places;
    }

    @Override
    public void setData(Place[] data) {
        places = data;
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