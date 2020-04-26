package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import hasan.mohamed.shehata.homathonfrontapp.MapActivity;
import hasan.mohamed.shehata.homathonfrontapp.R;
import hasan.mohamed.shehata.homathonfrontapp.databinding.DualTextDialogFragmentLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.databinding.PatientDialogFragmentLayoutBinding;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DualTextDialogFragment extends DialogFragment {



    private String primaryText;
    private String secondaryText;
    private DualTextDialogFragmentLayoutBinding mDataBinding;

    public DualTextDialogFragment(String primaryText,String secondaryText) {
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.dual_text_dialog_fragment_layout, container, false);
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

        mDataBinding.dualtextPrimaryTextTvId.setText(primaryText);
        mDataBinding.dualtextSecondaryTextTv.setText(secondaryText);

        mDataBinding.dualtextOkIdBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }



    public static DualTextDialogFragment newInstance(String primaryText, String secondaryText, Context context) {
        DualTextDialogFragment frag = new DualTextDialogFragment( primaryText, secondaryText);
        Bundle args = new Bundle();
        args.putString("title", "");
        frag.setArguments(args);
        try {
            FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
            frag.show(fm, "clinic");
        }
            catch (Exception e)
        {
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }
        return frag;
    }


}
