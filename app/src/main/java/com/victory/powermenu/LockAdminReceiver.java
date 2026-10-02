package com.victory.powermenu;

import android.app.admin.DeviceAdminReceiver;

/**
 * Device admin receiver used ONLY for the force-lock policy, so one-tap lock
 * screen works even when the accessibility service cannot be enabled
 * (e.g. Android 13+ "restricted setting" for sideloaded apps).
 */
public class LockAdminReceiver extends DeviceAdminReceiver {
    // No extra logic needed: the framework handles activation and lockNow().
}
