package com.btk.mnj.forecast.Util;

import android.content.Context;
import android.graphics.Typeface;

public class Util {

    public static Typeface getXLightTypeface(Context context) {
        String mTtfFontName = "GothamNarrow-XLight.otf";
        Typeface typeface =Typeface.createFromAsset(context.getAssets(), mTtfFontName);
        return typeface;
    }
}
