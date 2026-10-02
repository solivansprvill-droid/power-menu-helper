package com.victory.powermenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Invisible trampoline. Receives "power_menu" / "lock" actions from the
 * Quick Settings tile. Lock falls back to device-admin force-lock when the
 * accessibility service is unavailable.
 */
public class TriggerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent() != null ? getIntent().getAction() : null;
        PowerMenuService svc = PowerMenuService.getInstance();

        if (PowerTileService.ACTION_LOCK.equals(action)) {
            if (svc != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PowerMenuService.vibrate((short) 40);
                svc.lockScreen();
            } else if (AdminLock.isDeviceAdminActive(this)) {
                PowerMenuService.vibrate((short) 40);
                AdminLock.lockNow(this);
            } else {
                Toast.makeText(this, R.string.lock_needs_setup, Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            if (svc == null) {
                Toast.makeText(this, R.string.service_not_enabled, Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                PowerMenuService.vibrate((short) 40);
                svc.openPowerMenu();
            }
        }

        finish();
    }
}
