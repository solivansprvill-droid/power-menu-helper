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
    private View restrictedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusView = findViewById(R.id.txt_status);
        powerBtn = findViewById(R.id.btn_power_menu);
        lockBtn = findViewById(R.id.btn_lock);
        restrictedCard = findViewById(R.id.card_restricted);
        Button enableBtn = findViewById(R.id.btn_enable_service);
        Button fixRestrictedBtn = findViewById(R.id.btn_fix_restricted);

        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });

        // Android 13+: sideloaded apps are blocked from enabling accessibility
        // until the user taps "Allow restricted settings" on the App Info page.
        fixRestrictedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            android.net.Uri.fromParts("package", getPackageName(), null));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.service_not_enabled, Toast.LENGTH_LONG).show();
                }
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
        // Show the sideload-restriction helper only on Android 13+ while the
        // service is still disabled (once enabled, the problem is solved).
        restrictedCard.setVisibility(
                (!on && Build.VERSION.SDK_INT >= 33) ? View.VISIBLE : View.GONE);
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
