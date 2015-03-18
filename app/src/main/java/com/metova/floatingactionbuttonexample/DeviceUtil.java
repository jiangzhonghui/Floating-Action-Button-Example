package com.metova.floatingactionbuttonexample;

import android.content.Context;

public class DeviceUtil {

    // See http://developer.android.com/guide/practices/screens_support.html
    public static float pxFromDp(Context context, float dp) {

        return dp * context.getResources().getDisplayMetrics().density + 0.5f;
    }

}
