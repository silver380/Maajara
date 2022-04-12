package ir.blackswan.travelapp.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import ir.blackswan.travelapp.Data.FakeData;
import ir.blackswan.travelapp.TourPageActivity;
import ir.blackswan.travelapp.databinding.FragmentSearchBinding;
import ir.blackswan.travelapp.ui.Adapters.TourRecyclerAdapter;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TourRecyclerAdapter tourRecyclerAdapter = new TourRecyclerAdapter(getActivity() , FakeData.getFakeTours());
        binding.rclSearch.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rclSearch.setAdapter(tourRecyclerAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}