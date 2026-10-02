package com.victory.powermenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView statusView;
    private Button powerBtn, lockBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = findViewById(R.id.txt_status);
        powerBtn = findViewById(R.id.btn_power_menu);
        lockBtn = findViewById(R.id.btn_lock);
        Button enableBtn = findViewById(R.id.btn_enable_service);

        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerMenuService svc = PowerMenuService.getInstance();
                if (svc == null) {
                    Toast.makeText(MainActivity.this, R.string.service_not_enabled, Toast.LENGTH_LONG).show();
                } else {
                    PowerMenuService.vibrate((short) 40);
                    svc.openPowerMenu();
                }
            }
        });
        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerMenuService svc = PowerMenuService.getInstance();
                if (svc == null) {
                    Toast.makeText(MainActivity.this, R.string.service_not_enabled, Toast.LENGTH_LONG).show();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    PowerMenuService.vibrate((short) 40);
                    svc.lockScreen();
                } else {
                    Toast.makeText(MainActivity.this, R.string.lock_unsupported, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStatus();
    }

    private void refreshStatus() {
        boolean on = PowerMenuService.getInstance() != null
                && isServiceEnabledInSettings();
        statusView.setText(on ? R.string.status_on : R.string.status_off);
        statusView.setBackgroundResource(on ? R.drawable.pill_on : R.drawable.pill_off);
        powerBtn.setEnabled(on);
        lockBtn.setEnabled(on);
        powerBtn.setAlpha(on ? 1f : 0.45f);
        lockBtn.setAlpha(on ? 1f : 0.45f);
    }

    /** Double-check the system settings, covers the "enabled but process restarted" edge case. */
    private boolean isServiceEnabledInSettings() {
        String enabled = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabled == null) {
            return false;
        }
        String flat = getPackageName() + "/" + PowerMenuService.class.getCanonicalName();
        return enabled.toLowerCase().contains(getPackageName().toLowerCase())
                || enabled.contains(flat);
    }
}
