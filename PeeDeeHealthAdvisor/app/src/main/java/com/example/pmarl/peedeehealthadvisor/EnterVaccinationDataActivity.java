/*
    Author: Patrick Marlowe
    Email Address: pmarlowe782@gmail.com
    Written: June 22, 2018
    Last Updated: July 25, 2018

    Compilation:
    Execution:

    Description of Execution:
 */
package com.example.pmarl.peedeehealthadvisor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Notification;

public class EnterVaccinationDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private Button enterData, clearData;
    private ImageButton home;
    private Spinner Spinner;
    Context context = this;
    EditText editDate;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "MMM dd yyyy ";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    private NotificationManagerCompat notificationManager;
    Cursor cursorVaccinations = MainActivity.myDB.readVaccinationRecords();
    String dateQuery = "";
    String vaccinationName = "";
    Date date1;
    long epoch;
    long check;
    long futureInMillis;



    @Override
    protected void  onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_enter_vaccination_data);
        notificationManager = NotificationManagerCompat.from(this);

        editDate = (EditText) findViewById(R.id.editDate);

// init - set date to current date
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);

// set calendar date and update editDate
        date = new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

// onclick - popup datepicker
        editDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        home = (ImageButton) findViewById(R.id.Home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });

        Spinner =  findViewById(R.id.vaccinationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.vaccinations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
        Spinner.setOnItemSelectedListener(this);


        clearData = (Button) findViewById(R.id.clearData);
        clearData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // needs implementations
                Spinner.setSelection(0);
                editDate.setText("");
            }
        });

        enterData = (Button) findViewById(R.id.enterData);
        enterData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(Spinner.getSelectedItemPosition()==0)
                {
                    showDataNotEnteredWarning();
                }
                else
                {
                    // needs implementations
                    boolean isInserted = MainActivity.myDB.insertVaccination(editDate.getText().toString(), Spinner.getSelectedItem().toString());

                    if (isInserted = true) {

                        showDataEntryCheckmark();

                        if(cursorVaccinations != null) {
                            cursorVaccinations.moveToLast();
                        }

                        // finds the correct notification to send
                        do {

                            vaccinationName = cursorVaccinations.getString(3);

                            try {

                                dateQuery = cursorVaccinations.getString(0) + cursorVaccinations.getString(1);
                                date1 = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz").parse(dateQuery);
                                epoch = date1.getTime();
                                futureInMillis = new EnterVaccinationDataActivity().futureMillisTimeCalculator(epoch, 10000); // makes three for four of these for the different types
                                check = futureInMillis-System.currentTimeMillis();
                                // 86400000 is one day in our time we need epoch milliseconds

                            } catch (ParseException e) {
                                e.printStackTrace();
                                showDataError();
                            }

                            if(vaccinationName.equals("Flu Shot") && check < -1000) {
                                scheduleNotification(getNotification("Past due For Flu Shot", "Flu Shot Reminder"), futureInMillis);
                                break;
                            }

                            if(vaccinationName.equals("Flu Shot")) {
                                scheduleNotification(getNotification("Remember to get your Flu Shot", "Flu Shot Reminder"), futureInMillis);
                                break;
                            }

                            if(vaccinationName.equals("Shingle") && check < -1000) {
                                scheduleNotification(getNotification("Past due for your Shingle Vaccination!", "Shingle Vaccination Reminder"), futureInMillis);
                                break;
                            }

                            if(vaccinationName.equals("Shingle")) {
                                scheduleNotification(getNotification("Remember to get your Shingle Vaccination!", "Shingle Vaccination Reminder"), futureInMillis);
                                break;
                            }

                            if(vaccinationName.equals("Pneumonia") && check < -1000) {
                                scheduleNotification(getNotification("Past due for your Pneumonia Shot!", "Pneumonia Reminder"), futureInMillis);
                                break;
                            }

                            if(vaccinationName.equals("Pneumonia")) {
                                scheduleNotification(getNotification("Remember to get your Pneumonia Shot!", "Pneumonia Reminder"), futureInMillis);
                                break;
                            }


                        } while (cursorVaccinations.moveToPrevious());

                    } else {

                        showDataError();
                    }

                    launchPrevActivity();
                }


            }
        });


    }

    private void updateDate()
    {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        launchPrevActivity();
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
        Intent intent = new Intent (this, SelectVaccinationActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
    {
        if(position != 0)
        {
            String text = parent.getItemAtPosition(position).toString();
//            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Show images in Toast prompt.
    private void showDataEntryCheckmark()
    {

        Toast toast = Toast.makeText(getApplicationContext(), "Vaccination Entered", Toast.LENGTH_SHORT);
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

    private long futureMillisTimeCalculator(long epoch, long delay){

        long futureInMillis = epoch + delay;

        return futureInMillis;
    }

    private void scheduleNotification(Notification notification, long futureInMillis) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 4);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent); // can use a repeating method to make notifications repeat after time or hard code
    }

    private Notification getNotification(String content, String title) {

        // this is for making the app open on this screen if the notification is clicked.
        Intent intent = new Intent(this, EnterVaccinationDataActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_4_ID")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_vaccinations)
                .setContentText(content)
                .setPriority(1)
                // can be used to make the notifications longer. Just add another parameter to the getNotification() and add it in.
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Remember to get your vaccinations in the recommended time-frame."))
                .setContentIntent(pendingIntent)
                .build();

        return notification;
    }

}
