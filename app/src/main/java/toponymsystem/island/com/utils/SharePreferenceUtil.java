package toponymsystem.island.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class SharePreferenceUtil {
    private static String preference_name = "Preference";
    private static int mode = 0;

    public static void putString(Context paramContext, String key, String value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(preference_name, 0).edit();
        localEditor.putString(key, value);
        localEditor.commit();
    }

    public static String getString(Context paramContext, String key,
                                   String defaultValue) {

        if (paramContext == null)
            return defaultValue;

        return paramContext.getSharedPreferences(preference_name, mode)
                .getString(key, defaultValue);
    }

    public static String getString(Context paramContext, String key) {
        return getString(paramContext, key, null);
    }

    public static void putBoolean(Context paramContext, String key,
                                  boolean value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(preference_name, 0).edit();
        localEditor.putBoolean(key, value);
        localEditor.commit();
    }

    public static boolean getBoolean(Context paramContext, String key,
                                     boolean defaultValue) {
        return paramContext.getSharedPreferences(preference_name, mode)
                .getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context paramContext, String key) {
        return getBoolean(paramContext, key, false);
    }

    public static void putInt(Context paramContext, String key, int value) {
        SharedPreferences.Editor localEditor = paramContext
                .getSharedPreferences(preference_name, 0).edit();
        localEditor.putInt(key, value);
        localEditor.commit();
    }

    public static int getInt(Context paramContext, String key, int defaultValue) {
        return paramContext.getSharedPreferences(preference_name, mode).getInt(
                key, defaultValue);
    }

    public static int getInt(Context paramContext, String key) {
        return getInt(paramContext, key, -1);
    }

}