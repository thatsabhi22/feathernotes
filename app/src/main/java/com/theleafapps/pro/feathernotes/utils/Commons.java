package com.theleafapps.pro.feathernotes.utils;

import android.graphics.Rect;
import android.widget.EditText;

/**
 * Created by aviator on 08/12/16.
 */

public class Commons {

    public static Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }
}
