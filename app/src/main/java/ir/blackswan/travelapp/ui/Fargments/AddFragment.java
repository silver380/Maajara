package ir.blackswan.travelapp.ui.Fargments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ir.blackswan.travelapp.databinding.FragmentAddBinding;
import ir.blackswan.travelapp.ui.AddTourActivity;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAdd;
        textView.setText("comingSoon");

        startActivity(new Intent(getActivity() , AddTourActivity.class));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}