package com.gabyquiles.eventy.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Description
 *
 * @author gabrielquiles-perez
 */
public class NextEventWidgetProvider extends AppWidgetProvider {
    private final String LOG_TAG = NextEventWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        context.startService(new Intent(context, NextEventWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
//        context.startService(new Intent(context, NextEventWidgetIntentService.class));
    }
}
