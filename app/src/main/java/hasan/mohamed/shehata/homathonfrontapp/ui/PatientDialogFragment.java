package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import hasan.mohamed.shehata.homathonfrontapp.MapActivity;
import hasan.mohamed.shehata.homathonfrontapp.R;
import hasan.mohamed.shehata.homathonfrontapp.databinding.ClinicTemplateLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.databinding.PatientDialogFragmentLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDialogFragment extends DialogFragment {


    private Clinic clinic;
    private boolean isCreateNewClinic = false;
    private PatientDialogFragmentLayoutBinding mDataBinding;
    private DialogFragment this_;

    public PatientDialogFragment(Clinic clinic,boolean isCreateNewClinic) {
        this_ = this;
        this.isCreateNewClinic = isCreateNewClinic;
        if(clinic == null){
            this.clinic = new Clinic();
            this.isCreateNewClinic = true;
        }
        else {
            this.clinic = new Clinic(clinic);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.patient_dialog_fragment_layout, container, false);
        mDataBinding.setClinic(this.clinic);
        View view = mDataBinding.getRoot();
        return view;
    }








    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Show soft keyboard automatically and request focus to field

        if(!isCreateNewClinic){
            setDataToViews();
        }
        final Runnable before = new Runnable() {
            @Override
            public void run() {
                //
            }
        };

        final Runnable after = new Runnable() {
            @Override
            public void run() {
                //
                dismiss();

            }
        };

        mDataBinding.patientSelectLocOnMapIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra(MapActivity.INTENT_LOC_LAT_KEY,mDataBinding.getClinic().getLatitudeDouble());
                intent.putExtra(MapActivity.INTENT_LOC_LNG_KEY,mDataBinding.getClinic().getLongitudeDouble());
                startActivityForResult(intent,MapActivity.PICK_LOCATION_REQUEST);
            }
        });



        mDataBinding.patientFragCalculateIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDepartureTime();
            }
        });

        mDataBinding.patientFragBackIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }

    private void calculateDepartureTime() {
        long turn = Long.parseLong(mDataBinding.patientTurnnumberLabelEt.getText().toString());
        long examinationTimePerPatientInMinutes = Long.parseLong(clinic.getTurntimeinminutes());
        String [] time = clinic.getStarttime().split("-");
        long hour = Long.parseLong(time[0]);
        long minute = Long.parseLong(time[1]);
        long drivingTimeInMinutes = 33;

        long delayTimeInMinutes = (drivingTimeInMinutes-((turn-1)*examinationTimePerPatientInMinutes));
        long delayHours = delayTimeInMinutes / 60;
        long delayMinutes = delayTimeInMinutes % 60;

        long depHour,depMin;

        if(delayMinutes > minute){
            depHour = hour - delayHours -1;
            depMin = 60+ minute - delayMinutes;
        }
        else{
            depHour = hour - delayHours;
            depMin = minute - delayMinutes;
        }

        String depTime = String.valueOf(depHour) + ":" + String.valueOf(depMin);


        DualTextDialogFragment.newInstance("الوقت الأفضل لبدأ الرحلة",depTime,getContext());
    }

    private APIInterface apiInterface;
    private void saveToDbAndClose() {
        try {

            this.apiInterface = APIClient.getClient().create(APIInterface.class);
            getTDataFromViews();
            Call<Void> call;
            if(isCreateNewClinic) {
                call = apiInterface.createNewClinic(clinic);
            }
            else{
                call = apiInterface.updateClinic(String.valueOf(clinic.getClinicid()),clinic);
            }
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {


                    if(response.isSuccessful()){
                        for(Refreshable refreshable : Refreshable.instances){
                            if(refreshable != null)
                                refreshable.refresh();
                        }
                        dismiss();
                    }
//                    mDataBinding.connectionErrorIdTv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    call.cancel();
//                    mDataBinding.connectionErrorIdTv.setVisibility(View.VISIBLE);
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == MapActivity.PICK_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == MapActivity.RESULT_OK) {
                double lat = data.getDoubleExtra(MapActivity.INTENT_LOC_LAT_KEY,mDataBinding.getClinic().getLatitudeDouble());
                double lng = data.getDoubleExtra(MapActivity.INTENT_LOC_LNG_KEY,mDataBinding.getClinic().getLongitudeDouble());
                clinic.setLatitudeDouble(lat);
                clinic.setLongitudeDouble(lng);
                mDataBinding.getClinic().setLatitudeDouble(lat);
                mDataBinding.getClinic().setLongitudeDouble(lng);
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    private void getTDataFromViews(){
        if(mDataBinding!=null){

        }
    }

    private void setDataToViews(){
        if(mDataBinding!=null){

        }
    }

    public static PatientDialogFragment newInstance(Clinic clinic, boolean isCreateNewClinic) {
        PatientDialogFragment frag = new PatientDialogFragment(clinic,isCreateNewClinic);
        Bundle args = new Bundle();
        args.putString("title", "");
        frag.setArguments(args);
        return frag;
    }


}
