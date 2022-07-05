package com.example.shohojogi.historyRecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shohojogi.R;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView catName,nameHistory,jobHistory,paymentHistory,time;
    public ImageView ivHistory;
    public CardView hisCard;
    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        hisCard = (CardView) itemView.findViewById(R.id.historyCard);

        ivHistory = (ImageView) itemView.findViewById(R.id.ivHis);

        time = (TextView) itemView.findViewById(R.id.time);

        catName = (TextView) itemView.findViewById(R.id.catName);
        nameHistory = (TextView) itemView.findViewById(R.id.nameHistory);
        jobHistory = (TextView) itemView.findViewById(R.id.jobHistory);
        paymentHistory = (TextView) itemView.findViewById(R.id.paymentHistory);
    }

    @Override
    public void onClick(View v) {

    }
}
