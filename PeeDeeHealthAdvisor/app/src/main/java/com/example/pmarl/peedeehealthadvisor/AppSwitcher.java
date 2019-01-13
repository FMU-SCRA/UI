package com.example.pmarl.peedeehealthadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppSwitcher extends AppCompatActivity {

    private ViewPager mSlideViewPagerSwitcher;
    private LinearLayout mDotLayoutSwitcher;
//    private ImageButton home;


    private TextView[] mDotsSwitcher;

    private AppSwitcherAdapter sliderAdapterSwitcher;

    private Button mNextBtn;
    private ImageButton topLeft;
    private ImageButton topRight;
    private ImageButton bottomLeft;
    private ImageButton bottomRight;
    private ImageButton reports;

    private int mCurrentPage;


    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.card_view);


//        home = (ImageButton) findViewById(R.id.Home);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                launchMainActivity();
//            }
//        });

        mSlideViewPagerSwitcher = (ViewPager) findViewById(R.id.slideViewPagerSwitcher);
        mDotLayoutSwitcher = (LinearLayout) findViewById(R.id.dotsLayoutSwitcher);

      mNextBtn = (Button) findViewById(R.id.finishButton);

        topLeft = (ImageButton) findViewById(R.id.bloodPressureTEST);
        topRight = (ImageButton) findViewById(R.id.bloodSugarTEST);
        bottomLeft = (ImageButton) findViewById(R.id.cholesterolTEST);
        bottomRight = (ImageButton) findViewById(R.id.vaccinationsTEST);
        reports = (ImageButton) findViewById(R.id.bloodPressureTEST);

        sliderAdapterSwitcher = new AppSwitcherAdapter(this);

        mSlideViewPagerSwitcher.setAdapter(sliderAdapterSwitcher);

        addDotsIndicator(0);

        mSlideViewPagerSwitcher.addOnPageChangeListener(viewListener);

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEnterBloodPressureActivity();
            }
        });

        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { launchEnterBloodSugarActivity();
            }
        });

        bottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEnterCholesterolActivity();
            }
        });

        bottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEnterVaccinationDataActivity();
            }
        });




    }


    public void addDotsIndicator(int position) {
        mDotsSwitcher = new TextView[2]; // actual amount
        mDotLayoutSwitcher.removeAllViews();

        for (int i = 0; i < mDotsSwitcher.length; i++) {
            mDotsSwitcher[i] = new TextView(this);
            mDotsSwitcher[i].setTextSize(35);
            mDotsSwitcher[i].setText(Html.fromHtml("&#8226;"));
            mDotsSwitcher[i].setTextColor(getResources().getColor(R.color.GreenHuesLight));

            mDotLayoutSwitcher.addView(mDotsSwitcher[i]);
        }

        if(mDotsSwitcher.length > 0) {
            mDotsSwitcher[position].setTextColor(getResources().getColor(R.color.GreenHuesDark));
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);
            mCurrentPage = i; // 0

            if(i == 0) { // page 1 settings: starts at 0
                topLeft.setEnabled(true);
                topRight.setEnabled(true);
                bottomLeft.setEnabled(true);
                bottomRight.setEnabled(true);
//                reports.setEnabled(false);

                topLeft.setOnClickListener(new View.OnClickListener() { // overriding the onClick so it will go to Blood Pressure on first screen
                    @Override
                    public void onClick(View view) {
                        launchEnterBloodPressureActivity();
                    }
                });

                topRight.setOnClickListener(new View.OnClickListener() { // overriding the onClick so it will go to Blood Sugar on first screen
                    @Override
                    public void onClick(View view) {
                        launchEnterBloodSugarActivity();
                    }
                });

                bottomLeft.setOnClickListener(new View.OnClickListener() { // overriding the onClick so it will go to Cholesterol on first screen
                    @Override
                    public void onClick(View view) {
                        launchEnterCholesterolActivity();
                    }
                });

                bottomRight.setOnClickListener(new View.OnClickListener() { // overriding the onClick so it will go to Vaccinations on first screen
                    @Override
                    public void onClick(View view) {
                        launchEnterVaccinationDataActivity();
                    }
                });

            } else if (i == 1) { // page 2 settings
//                bloodPressure.setEnabled(false);
                topLeft.setEnabled(true);
                topRight.setEnabled(false);
                bottomLeft.setEnabled(false);
                bottomRight.setEnabled(false);
//                reports.setEnabled(true);

                topLeft.setOnClickListener(new View.OnClickListener() { // overriding the onClick so it will go to reports on second screen
                    @Override
                    public void onClick(View view) {
                        launchMainActivity();
                    }
                });

            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

    };


    private void launchPrevActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void launchMainActivity()
    {
        Intent intent = new Intent (this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void launchEnterBloodPressureActivity()
    {
        Intent intent = new Intent (this, SelectBloodPressureActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void launchEnterBloodSugarActivity()
    {
        Intent intent = new Intent (this, SelectBloodSugarActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void launchEnterCholesterolActivity()
    {
        Intent intent = new Intent (this, SelectCholesterolActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void launchEnterVaccinationDataActivity()
    {
        Intent intent = new Intent (this, SelectVaccinationActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        launchPrevActivity();
    }
}