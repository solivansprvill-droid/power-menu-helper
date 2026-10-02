package com.victory.powermenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Invisible trampoline activity. Receives an action ("power_menu" or "lock")
 * from the Quick Settings tile, forwards it to the accessibility service and
 * finishes immediately.
 */
public class TriggerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent() != null ? getIntent().getAction() : null;
        PowerMenuService svc = PowerMenuService.getInstance();

        if (svc == null) {
            Toast.makeText(this, R.string.service_not_enabled, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        } else if (PowerTileService.ACTION_LOCK.equals(action)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PowerMenuService.vibrate((short) 40);
                svc.lockScreen();
            } else {
                Toast.makeText(this, R.string.lock_unsupported, Toast.LENGTH_LONG).show();
            }
        } else {
            PowerMenuService.vibrate((short) 40);
            svc.openPowerMenu();
        }

        finish();
    }
}
