package com.yjkmust.fourleafclover.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 11432 on 2018/7/31.
 */

public class UiUtils {
    static public Toast mToast;
    public static void makeText(Context context,String string) {
        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }
}
