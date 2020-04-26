package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hasan.mohamed.shehata.homathonfrontapp.internet.APIClient;
import hasan.mohamed.shehata.homathonfrontapp.internet.APIInterface;
import hasan.mohamed.shehata.homathonfrontapp.pojo.Clinic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DualTextItemRVAdapter extends RecyclerView.Adapter<DualTextItemViewHolder> implements Refreshable{
    private List<Clinic> dataSet;
    private APIInterface apiInterface;
    private int rvType = RVType.NONE;
    private Refreshable parent;

    public DualTextItemRVAdapter(int rvType,Refreshable parent) {
        this.rvType = rvType;
        this.parent = parent;
        if(!Refreshable.instances.contains(this))
            Refreshable.instances.add(this);

        refresh();
    }

    @NonNull
    @Override
    public DualTextItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DualTextItemViewHolder(new DualTextItemView(parent.getContext(),rvType));
    }

    @Override
    public void onBindViewHolder(@NonNull DualTextItemViewHolder holder, int position) {
        if(dataSet != null){
            if(position < dataSet.size()){
                ((ItemBindable<DualText>)holder.getItemView()).bind(dataSet.get(position),rvType);
            }
            else{
                ((ItemBindable<DualText>)holder.getItemView()).bind(null,RVType.NONE);
            }
        }
        else{
            ((ItemBindable<DualText>)holder.getItemView()).bind(null,RVType.NONE);
        }
    }


    private static final int BOTTOM_SEPARATION = 2;
    @Override
    public int getItemCount() {
        if(dataSet != null)
            return dataSet.size()+BOTTOM_SEPARATION;
        else
            return BOTTOM_SEPARATION;
    }


    @Override
    public Object refresh(Object...objects) {
        try {

            this.apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<List<Clinic>> call = apiInterface.doGetListClinics();
            call.enqueue(new Callback<List<Clinic>>() {
                @Override
                public void onResponse(Call<List<Clinic>> call, Response<List<Clinic>> response) {


                    if(response.isSuccessful()){
                        List<Clinic> clinics = response.body();
                        dataSet = clinics;
                        notifyDataSetChanged();

                    }
                    parent.refresh();
                }

                @Override
                public void onFailure(Call<List<Clinic>> call, Throwable t) {
                    call.cancel();
                    if(parent!=null)
                        parent.refresh();
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
