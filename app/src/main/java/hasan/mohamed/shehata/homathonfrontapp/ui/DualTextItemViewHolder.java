package hasan.mohamed.shehata.homathonfrontapp.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DualTextItemViewHolder extends RecyclerView.ViewHolder {
    DualTextItemView itemView_;
    public DualTextItemView getItemView(){
        return itemView_;
    }
    public DualTextItemViewHolder(@NonNull DualTextItemView itemView) {
        super(itemView);
        itemView_ = itemView;
    }
}
