package com.victory.powermenu;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Device-admin based lock screen (force-lock policy). Unlike the
 * accessibility path, activating a device admin is NOT blocked by the
 * Android 13+ "restricted setting" policy for sideloaded apps, so one-tap
 * lock keeps working even when accessibility cannot be enabled.
 */
public class AdminLock {

    public static ComponentName componentName(Context ctx) {
        return new ComponentName(ctx, LockAdminReceiver.class);
    }

    public static boolean isDeviceAdminActive(Context ctx) {
        DevicePolicyManager dpm =
                (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return dpm != null && dpm.isAdminActive(componentName(ctx));
    }

    /** Open the system "activate device admin" confirmation dialog. */
    public static void requestActivation(Context ctx) {
        Intent i = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        i.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName(ctx));
        i.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                ctx.getString(R.string.admin_explanation));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    /** Lock the screen immediately (requires active device admin). */
    public static void lockNow(Context ctx) {
        DevicePolicyManager dpm =
                (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm != null && dpm.isAdminActive(componentName(ctx))) {
            dpm.lockNow();
        }
    }
}
