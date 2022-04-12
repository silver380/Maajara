package ir.blackswan.travelapp.ui.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.Utils.Utils;
import ir.blackswan.travelapp.Views.WebImageView;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.date.PersianDateFixedLeapYear;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class TourRecyclerAdapter extends RecyclerView.Adapter<TourRecyclerAdapter.ViewHolder> {
    List<Tour> tours;
    Activity activity;
    public TourRecyclerAdapter(Activity activity , List<Tour> tours) {
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
        Tour tour = tours.get(position);
        MyPersianCalender persianDate = new MyPersianCalender(tour.getStartDate());
        holder.image.setImagePath(tour.getPlaces().get(0).getPicturePath());
        holder.name.setText(tour.getTourName());
        holder.price.setText(tour.getPrice() + "");
        holder.startDate.setText(persianDate.getShortDate());

        //holder.itemView.setLayoutParams(new ViewGroup.LayoutParams());
        //holder.duration.setText();
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        WebImageView image;
        TextView name , price , startDate , duration;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wiv_tvh_image);
            name = itemView.findViewById(R.id.tv_tvh_name);
            price = itemView.findViewById(R.id.tv_tvh_price);
            startDate = itemView.findViewById(R.id.tv_tvh_start_date);
            duration = itemView.findViewById(R.id.tv_tvh_duration);
        }
    }
}
