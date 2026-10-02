package com.victory.powermenu;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.accessibility.AccessibilityEvent;

/**
 * Core accessibility service. Performs the global actions that normally
 * require the physical power button:
 *  - open the system power dialog (Power off / Restart / Emergency)
 *  - lock the screen (API 28+)
 */
public class PowerMenuService extends AccessibilityService {

    /** Static handle so tiles/activities can reach the live instance. */
    private static volatile PowerMenuService sInstance;

    public static PowerMenuService getInstance() {
        return sInstance;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sInstance = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (sInstance == this) {
            sInstance = null;
        }
        return super.onUnbind(intent);
    }

    /** Open the system power menu (Power off / Restart). */
    public boolean openPowerMenu() {
        return performGlobalAction(GLOBAL_ACTION_POWER_DIALOG);
    }

    /** Lock the screen. Only available on Android 9 (API 28) and above. */
    public boolean lockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
        }
        return false;
    }

    public static void vibrate(short ms) {
        Vibrator v = null;
        PowerMenuService s = sInstance;
        if (s != null) {
            v = (Vibrator) s.getSystemService(VIBRATOR_SERVICE);
        }
        if (v == null || !v.hasVibrator()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(ms);
        }
    }

    // ---- Unused accessibility event plumbing: we only need global actions ----
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) { }

    @Override
    public void onInterrupt() { }
}
