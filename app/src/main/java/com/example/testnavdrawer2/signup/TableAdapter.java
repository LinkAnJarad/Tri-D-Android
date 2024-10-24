package com.example.testnavdrawer2.signup;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavdrawer2.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<TableRowModel> tableRows;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public TableAdapter(List<TableRowModel> tableRows, OnDeleteClickListener deleteClickListener) {
        this.tableRows = tableRows;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        TableRowModel currentRow = tableRows.get(position);
        holder.column1.setText(currentRow.getColumn1());
        holder.column2.setText(currentRow.getColumn2());
        holder.column3.setText(currentRow.getColumn3());
        holder.column4.setText(currentRow.getColumn4());

        holder.deleteButton.setOnClickListener(v -> deleteClickListener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return tableRows.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {

        public TextView column1, column2, column3, column4;
        public ImageButton deleteButton;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            column1 = itemView.findViewById(R.id.column1);
            column2 = itemView.findViewById(R.id.column2);
            column3 = itemView.findViewById(R.id.column3);
            column4 = itemView.findViewById(R.id.column4);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void deleteRow(int position) {
        tableRows.remove(position);
        notifyItemRemoved(position);
    }

    public void addRow(TableRowModel row) {
        tableRows.add(row);
        notifyItemInserted(tableRows.size() - 1);
    }
}

