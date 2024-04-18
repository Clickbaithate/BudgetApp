package com.example.finalandroidgit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList transID, transAmount, transName, transMonth, transDay, transYear, transRemaining;

    // copies data passed in to arrays from this class
    CustomAdapter (Activity activity, Context context,
                   ArrayList transID,
                   ArrayList transAmount,
                   ArrayList transName,
                   ArrayList transMonth,
                   ArrayList transDay,
                   ArrayList transYear,
                   ArrayList transRemaining
                   ) {
        this.activity = activity;
        this.context = context;
        this.transID = transID;
        this.transAmount = transAmount;
        this.transName = transName;
        this.transMonth = transMonth;
        this.transDay = transDay;
        this.transYear = transYear;
        this.transRemaining = transRemaining;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    // sets transaction data for each item in recycler view and adds a onclick that takes user to another screen to update that transaction
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.TV_transID.setText(String.valueOf(transID.get(holder.getAdapterPosition())));
        holder.TV_transAmount.setText(String.valueOf("$" + transAmount.get(holder.getAdapterPosition())));
        holder.TV_transName.setText(String.valueOf(transName.get(holder.getAdapterPosition())));
        holder.TV_transDate.setText(String.valueOf(transMonth.get(holder.getAdapterPosition()) + "/" + transDay.get(holder.getAdapterPosition()) +"/" + transYear.get(holder.getAdapterPosition())));
        holder.TV_transRemaining.setText(String.valueOf("$" + transRemaining.get(holder.getAdapterPosition())));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(transID.get(holder.getAdapterPosition())));
                intent.putExtra("amount", String.valueOf(transAmount.get(holder.getAdapterPosition())));
                intent.putExtra("name", String.valueOf(transName.get(holder.getAdapterPosition())));
                intent.putExtra("month", String.valueOf(transMonth.get(holder.getAdapterPosition())));
                intent.putExtra("day", String.valueOf(transDay.get(holder.getAdapterPosition())));
                intent.putExtra("year", String.valueOf(transYear.get(holder.getAdapterPosition())));
                intent.putExtra("date", String.valueOf(transMonth.get(holder.getAdapterPosition())+"/"+transDay.get(holder.getAdapterPosition())+"/"+transYear.get(holder.getAdapterPosition())));
                intent.putExtra("remaining", String.valueOf(transRemaining.get(holder.getAdapterPosition())));
                activity.startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return transID.size();
    }

    // stores references to all the TextViews that our data will hold in order to display the data
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TV_transID, TV_transAmount, TV_transName, TV_transDate, TV_transRemaining;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TV_transID = itemView.findViewById(R.id.TV_transactionID);
            TV_transAmount = itemView.findViewById(R.id.TV_transactionAmount);
            TV_transName = itemView.findViewById(R.id.TV_transactionName);
            TV_transDate = itemView.findViewById(R.id.TV_transactionDate);
            TV_transRemaining = itemView.findViewById(R.id.TV_transactionRemaining);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
