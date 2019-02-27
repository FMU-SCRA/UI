package com.example.pmarl.peedeehealthadvisor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MySearchResultRecyclerViewAdapter extends RecyclerView
        .Adapter<MySearchResultRecyclerViewAdapter
        .DataObjectHolder> {
    //    private static String LOG_TAG = "MyMedicationRecyclerViewAdapter";
    private ArrayList<SearchServiceDataObject> mDataset;
    private static MyClickListener myClickListener;
    static String phoneNumber;
    private Context context;


    public MySearchResultRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView locationNameView;
        TextView searchDistanceView;
        TextView searchHoursView;
        TextView searchServicesView;
        TextView searchAddressView;
        TextView searchWebsiteView;
        TextView searchPhoneView;
//        TextView medTaken;
        ImageButton callButton;
        ImageButton mapsButton;
//        Switch background;


        public DataObjectHolder(View itemView) {
            super(itemView);
            locationNameView = (TextView) itemView.findViewById(R.id.locationNameCardText);
            searchDistanceView = (TextView) itemView.findViewById(R.id.searchDistanceCardText);
            searchHoursView = (TextView) itemView.findViewById(R.id.searchHoursCardText);
            searchServicesView = (TextView) itemView.findViewById(R.id.searchServicesCardText);
            searchAddressView = (TextView) itemView.findViewById(R.id.searchAddressCardText);
            searchWebsiteView = (TextView) itemView.findViewById(R.id.searchWebsiteNumCardText);
            searchPhoneView= (TextView) itemView.findViewById(R.id.searchPhoneCardText);
            callButton = (ImageButton) itemView.findViewById(R.id.phoneCallButtonSearch);
            mapsButton = (ImageButton) itemView.findViewById(R.id.mapButton);
//            medTaken = (TextView) itemView.findViewById(R.id.medTakingCardText);
//            background = (Switch) itemView.findViewById(R.id.switchTaking);
//            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
        }

    } // end of class


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MySearchResultRecyclerViewAdapter(ArrayList<SearchServiceDataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_search_services_card_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;

    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.locationNameView.setText(mDataset.get(position).getName());
        holder.searchDistanceView.setText(String.valueOf(mDataset.get(position).getDistance()));
        holder.searchHoursView.setText(mDataset.get(position).getSchedule());
        holder.searchServicesView.setText(mDataset.get(position).getServices());
        holder.searchAddressView.setText(mDataset.get(position).getAddress());
        holder.searchWebsiteView.setText(mDataset.get(position).getUrl());
        holder.searchPhoneView.setText(mDataset.get(position).getPhone());
        holder.callButton.setOnClickListener(mDataset.get(position).createCall());
        Log.d("TESTINGCARD: ", mDataset.get(position).getServices());

//        holder.takenButton.setOnClickListener(mDataset.get(position).updateTaken());
//        holder.background.setOnCheckedChangeListener(mDataset.get(position).createSwitch());


    }

    public void addItem(SearchServiceDataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }
}