package com.example.testnavdrawer2.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.testnavdrawer2.R;
import com.example.testnavdrawer2.ui.CardAdapter;
import com.example.testnavdrawer2.ui.CardData;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.SearchView;

public class HistoryFragment extends Fragment {

    private HistoryViewModel mViewModel;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private List<CardData> cardDataList;


    private void filter(String query) {
        if (cardAdapter != null) {
            cardAdapter.getFilter().filter(query);
        }
    }


    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);

        // Initialize RecyclerView using 'view'
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize data and adapter
        cardDataList = new ArrayList<>();
        cardAdapter = new CardAdapter(getContext(), cardDataList);
        recyclerView.setAdapter(cardAdapter);

        Bitmap qrCodeBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.test_qr)).getBitmap();

        // Add a test card (replace with actual dynamic data)
        CardData testCard = new CardData("Sedan", "ABC-1234", "A1", "2023-10-29", qrCodeBitmap, true);
        cardAdapter.addCard(testCard);

        testCard = new CardData("Motor", "ABC-3434", "A2", "2023-20-29", qrCodeBitmap, false);
        cardAdapter.addCard(testCard);
        cardAdapter.addCard(testCard);
        cardAdapter.addCard(testCard);
        cardAdapter.addCard(testCard);
        cardAdapter.addCard(testCard);
        cardAdapter.addCard(testCard);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);



    }

}