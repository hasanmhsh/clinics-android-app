package hasan.mohamed.shehata.homathonfrontapp.ui.home;

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
import hasan.mohamed.shehata.homathonfrontapp.databinding.FragmentHomeBinding;
import hasan.mohamed.shehata.homathonfrontapp.ui.DualTextItemRVAdapter;
import hasan.mohamed.shehata.homathonfrontapp.ui.RVType;
import hasan.mohamed.shehata.homathonfrontapp.ui.Refreshable;

public class HomeFragment extends Fragment implements Refreshable {

    private HomeViewModel homeViewModel;


    private FragmentHomeBinding mDataBinding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        ((MainActivity)getActivity()).hideFab();
        mDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false);
        View view = mDataBinding.getRoot();
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataBinding.patientsFragRvId.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mDataBinding.patientsFragRvId.setLayoutManager(layoutManager);
        mDataBinding.patientsFragRvId.setAdapter(new DualTextItemRVAdapter(RVType.PATIENTS_FRAGMENT,this));

        mDataBinding.patientsFragSwipeRefreshId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((DualTextItemRVAdapter)mDataBinding.patientsFragRvId.getAdapter()).refresh();
            }
        });

    }
    @Override
    public Object refresh(Object... data) {
        mDataBinding.patientsFragSwipeRefreshId.setRefreshing(false);
        return null;
    }
}