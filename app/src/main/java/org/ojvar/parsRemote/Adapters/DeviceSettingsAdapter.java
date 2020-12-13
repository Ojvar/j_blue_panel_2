package org.ojvar.parsRemote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.ojvar.parsRemote.Models.DeviceSettingModel;
import org.ojvar.parsRemote.R;

public class DeviceSettingsAdapter extends RecyclerView.Adapter<DeviceSettingsAdapter.ViewHolder> {
    private final Context context;
    private final DeviceSettingModel[] mData;
    private final LayoutInflater mInflater;
    private IOnItemChange onItemChangeEvents;

    /**
     * data is passed into the constructor
     *
     * @param context
     * @param data
     */
    public DeviceSettingsAdapter(Context context, DeviceSettingModel[] data) {
        this.context = context;
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
        View view = mInflater.inflate(R.layout.device_setting_row, parent, false);

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
        DeviceSettingModel model = mData[position];

        int min = model.getRange().min;
        int max = model.getRange().max;

        holder.model = model;
        holder.idTextView.setText(String.valueOf(model.getId()));
        holder.idTextView.setText(model.getId() + " : " + model.getTitle());

        holder.valueEditText.setEnabled(!model.isReadOnly());
        holder.valueEditText.setText(String.valueOf(model.getValue()));
        holder.valueEditText.setHint(min + "~" + max);
    }

    /**
     * total number of rows
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.length;
    }

    /**
     * Set event listener
     *
     * @return
     */
    public IOnItemChange getOnItemChangeEvents() {
        return onItemChangeEvents;
    }

    /**
     * Get event listener
     *
     * @param onItemChangeEvents
     */
    public void setOnItemChangeEvents(IOnItemChange onItemChangeEvents) {
        this.onItemChangeEvents = onItemChangeEvents;
    }

    /**
     * stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public DeviceSettingModel model;
        private final ImageView helpImageView;
        private final TextView idTextView;
        private final EditText valueEditText;

        ViewHolder(View itemView) {
            super(itemView);

            helpImageView = itemView.findViewById(R.id.helpImageView);
            idTextView = itemView.findViewById(R.id.idTextView);
            valueEditText = itemView.findViewById(R.id.valueEditText);

            final int number = Integer.valueOf("0" + valueEditText.getText().toString());

            helpImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemChangeEvents.onHelpClicked(model, number);
                }
            });

            valueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if ((null != onItemChangeEvents) && !hasFocus) {
                        onItemChangeEvents.onItemValueChanged(model, number);
                    }
                }
            });
        }
    }

    /**
     * On Item change interface
     */
    public interface IOnItemChange {
        void onItemValueChanged(DeviceSettingModel model, int newValue);

        void onHelpClicked(DeviceSettingModel model, int number);
    }
}