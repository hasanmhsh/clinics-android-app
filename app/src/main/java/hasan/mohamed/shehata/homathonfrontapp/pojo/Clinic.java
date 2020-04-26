package hasan.mohamed.shehata.homathonfrontapp.pojo;
import android.content.Intent;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.ui.DualText;

public class Clinic extends BaseObservable implements DualText {

    @SerializedName("clinicid")
    private long clinicid;

    @SerializedName("name")
    private String name;

    @SerializedName("details")
    private String details;

    @SerializedName("currentturn")
    private long currentturn;

    @SerializedName("starttime")
    private String starttime;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("turntimeinminutes")
    private long turntimeinminutes;

    @SerializedName("numberofpatients")
    private long numberofpatients;

    @SerializedName("patients")
    private List<Patient> patients = null;

    public Clinic(){

    }

    public Clinic(final Clinic clinic){
        this.clinicid = clinic.clinicid;
        this.name = clinic.name;
        this.details = clinic.details;
        this.currentturn = clinic.currentturn;
        this.starttime = clinic.starttime;
        this.latitude = clinic.latitude;
        this.longitude = clinic.longitude;
        this.turntimeinminutes = clinic.turntimeinminutes;
        this.numberofpatients = clinic.numberofpatients;
        if(clinic.patients != null) {
            this.patients = new ArrayList<Patient>();
            for (Patient p : clinic.patients){
                this.patients.add(new Patient(p));
            }
        }
    }

    @Bindable
    public long getClinicid() {
        return clinicid;
    }

    public void setClinicid(long clinicid) {
        //Avoid infinite loop
        //if(this.clinicid != clinicid) {
            this.clinicid = clinicid;
            //notifyPropertyChanged(BR.clinicid);
        //}
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        //Avoid infinite loop
        if(this.name != name) {
            this.name = name;
            notifyPropertyChanged(BR.name);
        }
    }

    @Bindable
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        //Avoid infinite loop
        if(this.details != details) {
            this.details = details;
            notifyPropertyChanged(BR.details);
        }
    }

    @Bindable
    public String getCurrentturn() {
        return String.valueOf(currentturn);
    }

    public void setCurrentturn(String currentturn) {
        //Avoid infinite loop
        if(String.valueOf(this.currentturn) != currentturn) {
            this.currentturn = Long.parseLong(currentturn);
            notifyPropertyChanged(BR.currentturn);
        }
    }

    @Bindable
    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        //Avoid infinite loop
        if(this.starttime != starttime) {
            this.starttime = starttime;
            notifyPropertyChanged(BR.starttime);
        }
    }

    @Bindable
    public String getLatitude() {
        return String.valueOf(latitude);
    }

    public double getLatitudeDouble() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        //Avoid infinite loop
        if(String.valueOf(this.latitude) != latitude) {
            this.latitude = Double.parseDouble(latitude);
            notifyPropertyChanged(BR.latitude);
        }
    }

    public void setLatitudeDouble(double latitude) {
        //Avoid infinite loop
        if(this.latitude != latitude) {
            this.latitude = latitude;
            notifyPropertyChanged(BR.latitude);
        }
    }

    @Bindable
    public String  getLongitude() {
        return String.valueOf(longitude);
    }

    public double getLongitudeDouble() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        //Avoid infinite loop
        if(String.valueOf(this.longitude) != longitude) {
            this.latitude = Double.parseDouble(longitude);
            notifyPropertyChanged(BR.longitude);
        }
    }

    public void setLongitudeDouble(double longitude) {
        //Avoid infinite loop
        if(this.longitude != longitude) {
            this.longitude = longitude;
            notifyPropertyChanged(BR.longitude);
        }
    }

    @Bindable
    public String getTurntimeinminutes() {
        return String.valueOf(turntimeinminutes);
    }

    public void setTurntimeinminutes(String turntimeinminutes) {
        //Avoid infinite loop
        if(String.valueOf(this.turntimeinminutes) != turntimeinminutes) {
            this.turntimeinminutes = Long.parseLong(turntimeinminutes);
            notifyPropertyChanged(BR.turntimeinminutes);
        }
    }

    @Bindable
    public String getNumberofpatients() {
        return String.valueOf(numberofpatients);
    }

    public void setNumberofpatients(String numberofpatients) {
        //Avoid infinite loop
        if(String.valueOf(this.numberofpatients) != numberofpatients) {
            this.numberofpatients = Long.parseLong(numberofpatients);
            notifyPropertyChanged(BR.numberofpatients);
        }
    }

    @Bindable
    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        //notifyPropertyChanged(BR._all);
    }



    @Override
    public String getPrimaryText() {
        return name;
    }

    @Override
    public String getSecondaryText() {
        return details;
    }

    @Override
    public String getNumber1() {
        return String.valueOf(currentturn);
    }


}
