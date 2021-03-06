package com.fmu.pmarl.peedeehealthadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyMedicationRecyclerViewAdapter extends RecyclerView
        .Adapter<MyMedicationRecyclerViewAdapter
        .DataObjectHolder> {
    private ArrayList<MedicationsDataObject> mDataset;
    private static MyClickListener myClickListener;
    static String phoneNumber;
    private Context context;
    static final String LOG_TAG = "MEDICATION";


    public MyMedicationRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView medNameView;
        TextView medDoseView;
        TextView medDeliveryView;
        TextView medRxNumView;
        TextView medPharmNameView;
        TextView medPharmNumView;
        TextView medFreqView;
        TextView medTaking;
        ImageButton callButton;
        ImageButton deleteButton;
        ImageButton changeButton;
        CardView background;
        TextView taking;
        int position;



        public DataObjectHolder(View itemView) {
            super(itemView);
            medNameView = (TextView) itemView.findViewById(R.id.medNameCardText);
            medDoseView = (TextView) itemView.findViewById(R.id.medDoseCardText);
            medDeliveryView = (TextView) itemView.findViewById(R.id.medDeliveryCardText);
            medRxNumView = (TextView) itemView.findViewById(R.id.medRXNumCardText);
            medPharmNameView = (TextView) itemView.findViewById(R.id.medPharmNameCardText);
            medPharmNumView = (TextView) itemView.findViewById(R.id.medPharmNumCardText);
            medFreqView= (TextView) itemView.findViewById(R.id.medFreqCardText);
            medTaking = (TextView) itemView.findViewById(R.id.medTakingText);
            callButton = (ImageButton) itemView.findViewById(R.id.phoneCallButton);
            deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
            changeButton = (ImageButton) itemView.findViewById(R.id.changeTakingButton);



            deleteButton.setOnClickListener(this);
            changeButton.setOnClickListener(this);

        }


        @Override
        // DELETE CARDS
        public void onClick(View v) {

            if (itemView.findViewById(R.id.deleteButton).isPressed()) {

                Log.i(LOG_TAG, "MEDICAL TOGGLE");
                context = itemView.getContext();

                Log.i(LOG_TAG, "confirmed");

                AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());

                alertDialog.setTitle("Delete this Medication Entry?");
                alertDialog.setMessage("Are you sure you want to delete this Medication Entry?");

                alertDialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.myDB.deleteMed(medNameView.getText().toString());
                                final Intent intent2;
                                intent2 = new Intent(context, MedicationTable.class);
                                context.startActivity(intent2);
                            }
                        }
                );

                if (MainActivity.myDB.readMedData() == null) {

                }

                alertDialog.show();

            } else if (itemView.findViewById(R.id.changeTakingButton).isPressed()) {

                Log.i(LOG_TAG, "MEDICAL TOGGLE");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) v.getContext());

                alertDialog.setTitle("Medication Status: ");
                alertDialog.setMessage("Are you still taking this medication?");

                //TAKING
                alertDialog.setPositiveButton(
                        "Taking",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.myDB.changeMed(medNameView.getText().toString(), "Yes");
                                Log.i(LOG_TAG, "TAKING STATUS: " + medTaking.getText().toString());
                                itemView.findViewById(R.id.rXToggleLogo).setBackgroundResource(R.drawable.ic_medicationsintro);
                                final Intent intent;
                                intent = new Intent(context, MedicationTable.class);
                                context.startActivity(intent);
                            }
                        }
                );

                //NOT TAKING
                alertDialog.setNegativeButton(
                        "Not Taking",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.myDB.changeMed(medNameView.getText().toString(), "No");
                                Log.i(LOG_TAG, "TAKING STATUS: " + medTaking.getText().toString());
                                itemView.findViewById(R.id.rXToggleLogo).setBackgroundResource(R.drawable.ic_greyrxicon);
                                final Intent intent;
                                intent = new Intent(context, MedicationTable.class);
                                context.startActivity(intent);
                            }
                        }
                );

                alertDialog.show();

            }

            context = itemView.getContext();

        }

    } // end of class


    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void onLongClick(View view){

    }

    public MyMedicationRecyclerViewAdapter(ArrayList<MedicationsDataObject> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.med_card_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;

    }



    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.medNameView.setText(mDataset.get(position).getName());
        holder.medDoseView.setText(mDataset.get(position).getDose());
        holder.medDeliveryView.setText(mDataset.get(position).getDelivery());
        holder.medRxNumView.setText(mDataset.get(position).getRxNum());
        holder.medPharmNameView.setText(mDataset.get(position).getPharmName());
        holder.medPharmNumView.setText(mDataset.get(position).getMedPharmNum());
        holder.medFreqView.setText(mDataset.get(position).getMedFreq());
        holder.medTaking.setText(mDataset.get(position).getMedTaking());
        holder.callButton.setOnClickListener(mDataset.get(position).createCall());


        //CHANGE COLORS
        if (holder.medTaking.getText().toString().equals("Yes")){
            //Pink BG
            holder.itemView.findViewById(R.id.rXToggleLogo).setBackgroundResource(R.drawable.ic_medicationsintro);
        } else if (holder.medTaking.getText().toString().equals("No")) {
            //Gray BG
            holder.itemView.findViewById(R.id.rXToggleLogo).setBackgroundResource(R.drawable.ic_greyrxicon);
        }
    }



    public void addItem(MedicationsDataObject dataObj, int index) {
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