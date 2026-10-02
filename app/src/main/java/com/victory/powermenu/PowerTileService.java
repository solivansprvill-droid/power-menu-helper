package com.victory.powermenu;

import android.content.Intent;
import android.service.quicksettings.TileService;

/**
 * Quick Settings tile. Click = open power menu; long-press behaves like the
 * normal tile settings. The actual global action is delegated to the
 * accessibility service through TriggerActivity, because a TileService by
 * itself cannot perform global actions.
 */
public class PowerTileService extends TileService {

    public static final String ACTION_POWER_MENU = "com.victory.powermenu.ACTION_POWER_MENU";
    public static final String ACTION_LOCK = "com.victory.powermenu.ACTION_LOCK";

    @Override
    public void onClick() {
        super.onClick();
        Intent i = new Intent(this, TriggerActivity.class);
        i.setAction(ACTION_POWER_MENU);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // TileService.startActivityAndCollapse(Intent) works on all supported
        // versions; deprecated on API 34+ in favour of
        // Tile.setActivityLaunchForClick(PendingIntent), but still functional.
        startActivityAndCollapse(i);
    }
}
