package ir.blackswan.travelapp.ui.Fargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ir.blackswan.travelapp.databinding.FragmentArchiveBinding;

public class ArchiveFragment extends RefreshingFragment {

    private FragmentArchiveBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArchiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textArchive;
        textView.setText("archive");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refresh() {
        //todo: implement this
    }
}