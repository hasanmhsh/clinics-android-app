package hasan.mohamed.shehata.homathonfrontapp.pojo;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.ui.DualText;

public class Patient implements DualText {

    @SerializedName("patientid")
    public long patientid;

    @SerializedName("name")
    public String name;

    @SerializedName("patientturn")
    public long patientturn;

    public Patient(){

    }

    public Patient(Patient patient){
        this.name = patient.name;
        this.patientid = patient.patientid;
        this.patientturn = patient.patientturn;
    }

    @Override
    public String getPrimaryText() {
        return name;
    }

    @Override
    public String getSecondaryText() {
        return String.valueOf(patientturn);
    }

    @Override
    public String getNumber1() {
        return null;
    }
}
