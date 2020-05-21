package com.coolalex.calculator;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


public class Utils {

    private static final int DEFAULT_VIBRATION_STRENGTH = 50;
    private static int vibrationStrength = DEFAULT_VIBRATION_STRENGTH;

    public void setVibrationStrength(int vibrationStrength) {
        Utils.vibrationStrength = vibrationStrength;
    }

    static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    static void haptic(View view) {
//        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
        Vibrator vb = ContextCompat.getSystemService(view.getContext(), Vibrator.class);
        assert vb != null;
        vb.vibrate(vibrationStrength);
    }
}
