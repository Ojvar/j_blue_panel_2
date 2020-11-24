package org.ojvar.bluepanel2.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.ojvar.bluepanel2.Helpers.InputFilterMinMax;
import org.ojvar.bluepanel2.Models.DeviceSettingModel;
import org.ojvar.bluepanel2.R;

public class DeviceSettingsAdapter extends RecyclerView.Adapter<DeviceSettingsAdapter.ViewHolder> {
    private DeviceSettingModel[] mData;
    private LayoutInflater mInflater;
    private IOnItemChange onItemChangeEvents;

    /**
     * data is passed into the constructor
     *
     * @param context
     * @param data
     */
    public DeviceSettingsAdapter(Context context, DeviceSettingModel[] data) {
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
        holder.titleTextView.setText(String.valueOf(model.getTitle()));
        holder.idTextView.setText(String.valueOf(model.getId()));

        holder.valueEditText.setText(String.valueOf(model.getValue()));
        holder.valueEditText.setHint(min + "~" + max);
//        holder.valueEditText.setFilters(new InputFilter[]{new InputFilterMinMax(min, max)});
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
        private ImageView helpImageView;
        private TextView idTextView;
        private TextView titleTextView;
        private TextInputEditText rowTextInput;
        private EditText valueEditText;

        ViewHolder(View itemView) {
            super(itemView);

            helpImageView = itemView.findViewById(R.id.helpImageView);
            idTextView = itemView.findViewById(R.id.idTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
//            rowTextInput = itemView.findViewById(R.id.rowTextInput);
            valueEditText = itemView.findViewById(R.id.valueEditText);

            valueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if ((null != onItemChangeEvents) && !hasFocus) {
                        int number = Integer.valueOf("0" + valueEditText.getText().toString());
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
    }
}