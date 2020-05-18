package org.ojvar.bluepanel2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.ojvar.bluepanel2.Models.DeviceModel;
import org.ojvar.bluepanel2.R;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    private List<DeviceModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * data is passed into the constructor
     *
     * @param context
     * @param data
     */
    public DevicesAdapter(Context context, List<DeviceModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    /**
     * inflates the row layout from xml when needed
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_row, parent, false);

        return new ViewHolder(view);
    }

    /**
     * binds the data to the TextView in each row
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceModel model = mData.get(position);

        holder.myTextView.setText(model.toString());
    }

    /**
     * total number of rows
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /**
     * convenience method for getting data at click position
     *
     * @param id
     * @return
     */
    DeviceModel getItem(int id) {
        return mData.get(id);
    }

    /**
     * allows clicks events to be caught
     *
     * @param itemClickListener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * parent activity will implement this method to respond to click events
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}