package com.example.testnavdrawer2.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testnavdrawer2.R;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private List<LogEntry> logList;

    public LogAdapter(List<LogEntry> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogEntry log = logList.get(position);
        holder.textDate.setText(log.getDate());
        String entry_text = log.getTimeIn() + log.getTimeOut();
        holder.textLogEntry.setText(entry_text);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public void addLog(LogEntry log) {
        logList.add(0, log); // Add to the beginning of the list
        notifyItemInserted(0);
    }

    public void removeLog(int position) {
        logList.remove(position);
        notifyItemRemoved(position);
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textLogEntry;

        LogViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textLogEntry = itemView.findViewById(R.id.textLogEntry);
        }
    }
}