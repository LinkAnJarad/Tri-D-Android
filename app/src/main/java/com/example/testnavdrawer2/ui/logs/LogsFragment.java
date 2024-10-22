package com.example.testnavdrawer2.ui.logs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testnavdrawer2.databinding.FragmentLogsBinding;

public class LogsFragment extends Fragment {

    private FragmentLogsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogsViewModel slideshowViewModel =
                new ViewModelProvider(this).get(LogsViewModel.class);

        binding = FragmentLogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}