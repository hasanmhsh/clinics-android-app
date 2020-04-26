package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.MapActivity;
import hasan.mohamed.shehata.homathonfrontapp.R;
import hasan.mohamed.shehata.homathonfrontapp.databinding.ClinicTemplateLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//key=AIzaSyCxbl_2A21jFKP1xvUahxGqbSM2oEcfA-s

public class ClinicTemplate extends DialogFragment {


    private Clinic clinic;
    private boolean isCreateNewClinic = false;
    private ClinicTemplateLayoutBinding mDataBinding;
    private DialogFragment this_;

    public ClinicTemplate(Clinic clinic,boolean isCreateNewClinic) {
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
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.clinic_template_layout, container, false);
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

        mDataBinding.clinicSelectLocOnMapIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra(MapActivity.INTENT_LOC_LAT_KEY,mDataBinding.getClinic().getLatitudeDouble());
                intent.putExtra(MapActivity.INTENT_LOC_LNG_KEY,mDataBinding.getClinic().getLongitudeDouble());
                startActivityForResult(intent,MapActivity.PICK_LOCATION_REQUEST);
            }
        });



        mDataBinding.cliniTmplateSaveIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDbAndClose();
            }
        });

        mDataBinding.cliniTmplateCancelIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDataBinding.incrementCurrentTurnIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long turn = Long.parseLong(clinic.getCurrentturn());
                try{
                    turn = Long.parseLong(mDataBinding.currentTurnTmplateIdEt.getText().toString());
                }
                catch (Exception e){

                }
                mDataBinding.currentTurnTmplateIdEt.setText(String.valueOf(turn+1));
            }
        });

        mDataBinding.decrementCurrentTurnIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long turn = Long.parseLong(clinic.getCurrentturn());
                try{
                    turn = Long.parseLong(mDataBinding.currentTurnTmplateIdEt.getText().toString());
                }
                catch (Exception e){

                }
                if(turn > 0)
                    mDataBinding.currentTurnTmplateIdEt.setText(String.valueOf(turn-1));
            }
        });


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
                    mDataBinding.connectionErrorIdTv.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    call.cancel();
                    mDataBinding.connectionErrorIdTv.setVisibility(View.VISIBLE);
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
            String time = String.valueOf(mDataBinding.clinicStarttimeTimepicker.getHour())+"-"+String.valueOf(mDataBinding.clinicStarttimeTimepicker.getMinute());
            clinic.setStarttime(time);

            clinic.setName(mDataBinding.ClinicNameEtId.getText().toString());
            clinic.setDetails(mDataBinding.clinicDetailsEtId.getText().toString());
            clinic.setTurntimeinminutes(mDataBinding.clinicSessionEstimatedTimeEt.getText().toString());
            clinic.setCurrentturn(mDataBinding.currentTurnTmplateIdEt.getText().toString());
            clinic.setNumberofpatients(mDataBinding.numberOfPatientsIdEt.getText().toString());
        }
    }

    private void setDataToViews(){
        if(mDataBinding!=null){
            String time = mDataBinding.getClinic().getStarttime();
            String [] time2 = time.split("-");
            try{
                mDataBinding.clinicStarttimeTimepicker.setHour(Integer.parseInt(time2[0]));
                mDataBinding.clinicStarttimeTimepicker.setMinute(Integer.parseInt(time2[1]));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static ClinicTemplate newInstance(Clinic clinic, boolean isCreateNewClinic) {
        ClinicTemplate frag = new ClinicTemplate(clinic,isCreateNewClinic);
        Bundle args = new Bundle();
        args.putString("title", "أضف سؤال");
        frag.setArguments(args);
        return frag;
    }


}
