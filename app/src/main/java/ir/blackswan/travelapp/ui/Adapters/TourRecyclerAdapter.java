package ir.blackswan.travelapp.ui.Adapters;

import static ir.blackswan.travelapp.ui.TourPageActivity.EXTRA_TOUR;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.blackswan.travelapp.Controller.PathController;
import ir.blackswan.travelapp.Data.Tour;
import ir.blackswan.travelapp.R;
import ir.blackswan.travelapp.Utils.MyPersianCalender;
import ir.blackswan.travelapp.ui.TourPageActivity;
import ir.blackswan.travelapp.Utils.Utils;
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
        view.getLayoutParams().width = Utils.getScreenWidth() * 40 /100;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tour tour = tours[position];
       // MyPersianCalender persianDate = new MyPersianCalender(tour.getStart_date());

        MyPersianCalender startDate = tour.getPersianStartDate();
        MyPersianCalender endDate = tour.getPersianEndDate();
        long daysBetween = Utils.numDaysBetween(startDate.getTimestamp() , endDate.getTimestamp());
        String startDateString = startDate.getShortDate();

        holder.image.setImagePath(PathController.getRandomPath(activity));
        holder.price.setText(tour.getShortPriceString());
        holder.startDate.setText(startDateString);
        holder.location.setText(tour.getDestination());
        holder.image.setScale(.5f);
        holder.image.setGradient(true);
        holder.duration.setText(daysBetween + "");
        holder.itemView.setOnClickListener(v -> {
            activity.startActivity(new Intent(activity , TourPageActivity.class)
            .putExtra(EXTRA_TOUR , tour));
        });

        //holder.duration.setText();
    }

    @Override
    public int getItemCount() {
        if (tours == null)
            return 0;
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
