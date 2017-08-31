package com.videomaker.photowithsong.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DaiPhongPC on 8/30/2017.
 */

public class CustomTextViewSanFranciscoBold extends TextView {
    public CustomTextViewSanFranciscoBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewSanFranciscoBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewSanFranciscoBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SanFranciscoBold.ttf", context);
        setTypeface(customFont);
    }
}
