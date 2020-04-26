package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import java.util.Map;

import hasan.mohamed.shehata.homathonfrontapp.MapActivity;
import hasan.mohamed.shehata.homathonfrontapp.R;
import hasan.mohamed.shehata.homathonfrontapp.databinding.DualTextRvItemViewBinding;
import hasan.mohamed.shehata.homathonfrontapp.databinding.PatientDialogFragmentLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DualTextItemView extends FrameLayout implements ItemBindable<DualText>, View.OnClickListener {
    private DualTextRvItemViewBinding mDataBinding;
    private int rvType;
    private APIInterface apiInterface;
    public DualTextItemView(Context context, int rvType) {
        super(context);
        this.rvType = rvType;
        setWillNotDraw(false);
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater)this.getContext().getSystemService(infService);
        this.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        //attach to parent must be true to be displayed
        mDataBinding = DataBindingUtil.inflate(li, R.layout.dual_text_rv_item_view, this, true);
        View view = mDataBinding.getRoot();


        mDataBinding.clinicViewContainer.setOnClickListener(this);

        if(rvType == RVType.PATIENTS_FRAGMENT){
            mDataBinding.deleteClinicBut.setVisibility(GONE);
        }
        else if(rvType == RVType.CLINICS_FRAGMENT){
            mDataBinding.deleteClinicBut.setVisibility(VISIBLE);
        }

        mDataBinding.deleteClinicBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<Void> call = apiInterface.deleteClinic(String.valueOf(((Clinic)mDataBinding.getDualtext()).getClinicid()));
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {


                            if(response.isSuccessful()){
                                for(Refreshable refreshable : Refreshable.instances){
                                    if(refreshable != null)
                                        refreshable.refresh();
                                }
                            }
                            else {
                                Toast.makeText(getContext(), "1-" + getContext().getString(R.string.Failure), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            call.cancel();
                            Toast.makeText(getContext(),"2-"+getContext().getString(R.string.Failure),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(rvType == RVType.CLINICS_FRAGMENT){
            try {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                ClinicTemplate addQuestionDialogFragment = ClinicTemplate.newInstance((Clinic)mDataBinding.getDualtext(),false);
                addQuestionDialogFragment.show(fm, "clinic");
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else if(rvType == RVType.PATIENTS_FRAGMENT){
            try {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                PatientDialogFragment addQuestionDialogFragment = PatientDialogFragment.newInstance((Clinic)mDataBinding.getDualtext(),false);
                addQuestionDialogFragment.show(fm, "patient");
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


    private static final DualText EMPTY_TEXT = new DualText() {
        @Override
        public String getPrimaryText() {
            return "";
        }

        @Override
        public String getSecondaryText() {
            return "";
        }

        @Override
        public String getNumber1() {
            return "";
        }
    };

    public void bind(DualText dualText,int rvType) {
//        this.rvType=rvType;
        if(dualText!=null) {
            mDataBinding.setDualtext(dualText);
            this.setVisibility(VISIBLE);
            mDataBinding.clinicViewContainer.setVisibility(VISIBLE);
        }
        else{
            mDataBinding.setDualtext(EMPTY_TEXT);
            this.setVisibility(INVISIBLE);
            mDataBinding.clinicViewContainer.setVisibility(INVISIBLE);
        }
    }

}
