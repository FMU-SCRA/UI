package com.fmu.pmarl.peedeehealthadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class EnterMedicationDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    //DatabaseHelper myDB;
    private Button enterData;
    private Button clearData;
    private Spinner spinnerType;
    private Spinner spinnerFrequency;
    private TextInputEditText medNameInput, medDoseInput, medDelivery, medRxNum, medPharmName, medPhoneNum;
    private RadioButton yesToggle, noToggle;
    String currentlyTaking;
    private Context context = this;

    @Override
    protected void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_enter_med_data);

        ImageButton home = findViewById(R.id.Home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });


        Button button;
        final TextView textview;
        final String[] value = new String[]{
                "Oral (Tablet)",
                "Oral (Liquid)",
                "Sublingual",
                "Rectal",
                "Topical (Cream)",
                "Topical (Eye Drop)",
                "Topical (Ear Drop)",
                "Intramuscular",
                "Subcutaneous"

        };

        button = (Button)findViewById(R.id.selectMedicationDeliveryButton);
        textview = (TextView)findViewById(R.id.medicationDeliveryName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(EnterMedicationDataActivity.this);


                alertdialogbuilder.setTitle("Select Medication Delivery");

                alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(value).get(which);

                        textview.setText(selectedText);

                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });


        // radio buttons
        yesToggle = (RadioButton) findViewById(R.id.takingYes);
        noToggle = (RadioButton) findViewById(R.id.takingNo);

        medNameInput = (TextInputEditText) findViewById(R.id.medicationNameInput);
        medDoseInput = (TextInputEditText) findViewById(R.id.medDoseInput);
        medDelivery = (TextInputEditText) findViewById(R.id.medFreqInput);
        medRxNum = (TextInputEditText) findViewById(R.id.medRXInput);
        medPharmName = (TextInputEditText) findViewById(R.id.medPharmNameInput);
        medPhoneNum = (TextInputEditText) findViewById(R.id.medPharmNumberInput);


        enterData = (Button) findViewById(R.id.enterData);

        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String medInputString = medNameInput.getText().toString() + " " +
                        " " + medNameInput.getText().toString() +  " " + textview.getText().toString() + " " +
                        medRxNum.getText().toString() +  " " + medPharmName.getText().toString() +
                        " " + medPhoneNum.getText().toString() + medDelivery.toString();
                if (medNameInput.getText().toString().equals("") || medDoseInput.getText().toString().equals("")
                        || textview.getText().toString().equals("Delivery") || medDelivery.getText().toString().equals("") || medRxNum.getText().toString().equals("")
                        || medPharmName.getText().toString().equals("") || medPhoneNum.getText().toString().equals("")) {
                    showDataNotEnteredWarning();

                } else {

                    if (yesToggle.isChecked()) {
                        currentlyTaking = "Yes";
                    }

                    else if (noToggle.isChecked()) {
                        currentlyTaking = "No";
                    }
                    boolean isInserted =
                            MainActivity.myDB.insertMedication(medNameInput.getText().toString(),
                                    medDoseInput.getText().toString(), textview.getText().toString(),
                                    medRxNum.getText().toString(), medPharmName.getText().toString(), medPhoneNum.getText().toString(), medDelivery.getText().toString(), currentlyTaking);

                    if (isInserted = true)
                        showDataEntryCheckmark();
                    else
                        showDataError();

                    launchPrevActivity();
                }
            }
        });

        clearData = (Button) findViewById(R.id.clearData);

        clearData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                medNameInput.setText("");
                medDoseInput.setText("");
//                spinnerType.setSelection(0);
                medRxNum.setText("");
                medPharmName.setText("");
                medPhoneNum.setText("");
                textview.setText("Delivery");

            }
        });


        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.medicationEntry);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager inputMethodManager = (InputMethodManager)  EnterMedicationDataActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(EnterMedicationDataActivity.this.getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        // this stops the screen from opening the keyboard on open, allowing the user to look.
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onBackPressed() {
        launchPrevActivity();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
    {
        if(position != 0)
        {
            String text = parent.getItemAtPosition(position).toString();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.takingYes:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.takingNo:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void launchMainActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void launchPrevActivity()
    {
        Intent intent = new Intent (this, SelectMedicationActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Show images in Toast prompt.
    private void showDataEntryCheckmark()
    {

        Toast toast = Toast.makeText(getApplicationContext(), "Medication Entered", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_checkmark);
        toastContentView.addView(imageView, 0);
        toast.show();

    }

    // Show images in Toast prompt.
    private void showDataNotEnteredWarning()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Fields", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_warning);
        toastContentView.addView(imageView, 0);
        toast.show();
    }

    // Show images in Toast prompt.
    private void showDataError()
    {

        Toast toast = Toast.makeText(getApplicationContext(), "ERROR: Please Try Again", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_error);
        toastContentView.addView(imageView, 0);
        toast.show();
    }
}
