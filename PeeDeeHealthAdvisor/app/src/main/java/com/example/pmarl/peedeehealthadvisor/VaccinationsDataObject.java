package com.example.pmarl.peedeehealthadvisor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class VaccinationsDataObject {

    /*
    * HOW TO ADD VACCINATION
    * STEP 1: add the new vaccination name to the string array in the strings.xml file.
    * STEP 2: add new VaccinationsDataObject object with name in VaccinationGraph.java using results.add()
    * STEP 3: test to make sure the vaccination's card is showing up.
    * STEP 4: make new if-statements inside of while-loop with cursor for finding if new vaccination added to results list.
    * STEP 5: make new if-statements for notifications if they apply.
    * STEP 6: test to make sure the vaccination's card is updating if taken.
    * */


    private String vaccinationName;
    private String vaccinationTaken;
    private String vaccinationDate;
    private String vaccineEpoch;



    VaccinationsDataObject(String name, String taken, String date, String epoch) {
        vaccinationName = name;
        vaccinationTaken = taken;
        vaccinationDate = date;
        vaccineEpoch = epoch;

    }


    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public String getVaccinationTaken() {
        return vaccinationTaken;
    }

    public void setVacciantionTaken(String vaccinationTaken) {
        this.vaccinationTaken = vaccinationTaken;
    }

    public String getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(String vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getVaccineEpoch() {
        return vaccineEpoch;
    }

    public void setVaccineEpoch(String vaccineEpoch) {
        this.vaccineEpoch = vaccineEpoch;
    }

}