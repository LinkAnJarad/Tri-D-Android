package com.example.testnavdrawer2.ui.qr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testnavdrawer2.databinding.FragmentQrBinding;

public class QRFragment extends Fragment {

    private FragmentQrBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QRViewModel galleryViewModel =
                new ViewModelProvider(this).get(QRViewModel.class);

        binding = FragmentQrBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}