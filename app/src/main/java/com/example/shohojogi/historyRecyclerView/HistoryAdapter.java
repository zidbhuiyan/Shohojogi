package com.example.shohojogi.historyRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shohojogi.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders>{
    private List<HistoryObject> itemList;
    private Context context;

    public HistoryAdapter(List<HistoryObject> itemList,Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        HistoryViewHolders rcv = new HistoryViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolders holder, int position) {
        holder.time.setText(itemList.get(position).getTime());

        if(itemList.get(position).getClientOrWorker().equals("Client")){
            holder.catName.setText("Worker Name:");
        }
        else if(itemList.get(position).getClientOrWorker().equals("Worker")){
            holder.catName.setText("Client Name:");
        }

        holder.nameHistory.setText(itemList.get(position).getNameHistory());
        holder.jobHistory.setText(itemList.get(position).getJobHistory());
        holder.paymentHistory.setText(itemList.get(position).getPaymentHistory());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
