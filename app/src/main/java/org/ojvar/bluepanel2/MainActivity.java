package org.ojvar.bluepanel2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import org.ojvar.bluepanel2.App.GlobalData;
import org.ojvar.bluepanel2.Helpers.BaseActivity;
import org.ojvar.bluepanel2.Helpers.BluetoothHelper;
import org.ojvar.bluepanel2.Helpers.VibrationHelper;

import static org.ojvar.bluepanel2.App.GlobalData.setupBTEventHandler;

public class MainActivity extends BaseActivity {
    private Handler handler;

    /**
     * On Create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    /**
     * On Resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        setupBTEventHandler(btEvents);
    }

    /**
     * Setup
     */
    private void setup() {
        bindEvents();
    }

    /**
     * Bind events
     */
    private void bindEvents() {
        findViewById(R.id.resetButton).setOnClickListener(backButtonEvent);
        findViewById(R.id.settingButton).setOnClickListener(settingButtonEvent);

        findViewById(R.id.cmdAutomaticButton).setOnTouchListener(touchHandler);
        findViewById(R.id.cmdFullOpenButton).setOnTouchListener(touchHandler);
        findViewById(R.id.cmdLockButton).setOnTouchListener(touchHandler);
        findViewById(R.id.cmdOneWayButton).setOnTouchListener(touchHandler);
        findViewById(R.id.cmdWinterButton).setOnTouchListener(touchHandler);
    }

    private View.OnTouchListener touchHandler = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            VibrationHelper.vibrate(getApplicationContext());

            String cmd;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cmd = String.format(v.getTag() + "", "1");
                    BluetoothHelper.send(cmd);
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    cmd = String.format(v.getTag() + "", "0");
                    BluetoothHelper.send(cmd);
                    break;

                default:
                    return false;
            }

            return true;
        }
    };

    /**
     * Back button
     */
    private View.OnClickListener cmdButtonEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cmd = String.format(v.getTag() + "", "0s");

            BluetoothHelper.send(cmd);
        }
    };


    /**
     * Back button
     */
    private View.OnClickListener backButtonEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            BluetoothHelper.disconnect();
            finish();
        }
    };

    /**
     * Device Settings button
     */
    private View.OnClickListener settingButtonEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VibrationHelper.vibrate(getApplicationContext());

            Intent intent = new Intent(MainActivity.this, DeviceSettingActivity.class);
            startActivity(intent);
        }
    };

    /**
     * BTEvents
     */
    private BluetoothHelper.BluetoothEvents btEvents = new BluetoothHelper.BluetoothEvents() {
        @Override
        public void OnConnect() {
        }

        @Override
        public void OnDisconnect() {
        }

        @Override
        public void OnCommand(final String data) {
            GlobalData.updateDataModel(data);
        }
    };
}
