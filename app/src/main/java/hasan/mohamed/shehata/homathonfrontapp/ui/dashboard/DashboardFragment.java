package hasan.mohamed.shehata.homathonfrontapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import hasan.mohamed.shehata.homathonfrontapp.MainActivity;
import hasan.mohamed.shehata.homathonfrontapp.R;
import hasan.mohamed.shehata.homathonfrontapp.databinding.FragmentDashboardBinding;
import hasan.mohamed.shehata.homathonfrontapp.ui.DualTextItemRVAdapter;
import hasan.mohamed.shehata.homathonfrontapp.ui.RVType;
import hasan.mohamed.shehata.homathonfrontapp.ui.Refreshable;

public class DashboardFragment extends Fragment implements Refreshable {

    private DashboardViewModel dashboardViewModel;

    private FragmentDashboardBinding mDataBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        mDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dashboard, container, false);
        View view = mDataBinding.getRoot();

        ((MainActivity)getActivity()).showFab();

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataBinding.clinicsFragRvId.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mDataBinding.clinicsFragRvId.setLayoutManager(layoutManager);
        mDataBinding.clinicsFragRvId.setAdapter(new DualTextItemRVAdapter(RVType.CLINICS_FRAGMENT,this));

        mDataBinding.clinicsFragSwipeRefreshId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((DualTextItemRVAdapter)mDataBinding.clinicsFragRvId.getAdapter()).refresh();
            }
        });

    }

    @Override
    public Object refresh(Object... data) {
        mDataBinding.clinicsFragSwipeRefreshId.setRefreshing(false);
        return null;
    }
}