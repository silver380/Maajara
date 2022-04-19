package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Views.WebImageView;

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.ViewHolder> {
    Tour[] tours;
    Activity activity;
    public TourRecyclerAdapter(Activity activity , Tour[] tours) {
        this.activity = activity;
        this.tours = tours;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.tour_view_holder , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tour tour = tours[position];
       // MyPersianCalender persianDate = new MyPersianCalender(tour.getStart_date());
        //1900-10-10
        String date = tour.getStart_date().replace("-" , "/");

//        holder.image.setImagePath(tour.getPlaces().get(0).getPicturePath());
        holder.price.setText(tour.getPrice() + "");
        holder.startDate.setText(date);
        holder.location.setText(tour.getDestination());
        holder.image.setScale(.5f);
        holder.image.setGradient(true);

        //holder.itemView.setLayoutParams(new ViewGroup.LayoutParams());
        //holder.duration.setText();
    }

    @Override
    public int getItemCount() {
        return tours.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        WebImageView image;
        TextView price , startDate , duration , location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wiv_tvh_image);
            price = itemView.findViewById(R.id.tv_tvh_price);
            startDate = itemView.findViewById(R.id.tv_tvh_start_date);
            duration = itemView.findViewById(R.id.tv_tvh_duration);
            location = itemView.findViewById(R.id.tv_tvh_location);
        }
    }
}
