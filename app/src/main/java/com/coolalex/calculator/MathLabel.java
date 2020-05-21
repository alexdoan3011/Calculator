package com.coolalex.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

public class MathLabel extends WebView {
    private String TAG = "error_log";
    private static final float default_text_size = 18;
    private int text_color;
    private int text_size;

    public MathLabel(Context context) {
        super(context);
        initialize();
    }

    public MathLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathLabel,
                0, 0);
        try {
            String a = mTypeArray.getString(R.styleable.MathLabel_setTextMathLabel);
            setTextColor(mTypeArray.getColor(R.styleable.MathLabel_setTextColorMathLabel, ContextCompat.getColor(context, android.R.color.black)));
            pixelSizeConversion(mTypeArray.getDimension(R.styleable.MathLabel_setTextSizeMathLabel, default_text_size));
            setBackgroundColor(mTypeArray.getInteger(R.styleable.MathLabel_setViewBackgroundColorMathLabel, ContextCompat.getColor(context, android.R.color.transparent)));
            if (a != null && !a.equals("")) {
                load(a);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception:" + e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private void pixelSizeConversion(float dimension) {
        if (dimension == default_text_size) {
            setTextSize((int) default_text_size);
        } else {
            int pixel_dimen_equivalent_size = (int) ((double) dimension / 1.6);
            setTextSize(pixel_dimen_equivalent_size);
        }
    }

    public void setTextSize(int size) {
        this.text_size = size;
    }


    private String getHexColor(int intColor) {
        //Android and javascript color format differ javascript support Hex color, so the android color which user sets is converted to hexcolor to replicate the same in javascript.
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        Log.d(TAG, "Hex Color:" + hexColor);
        return hexColor;
    }

    public void setViewBackgroundColor(int color) {
        setBackgroundColor(color);
        this.invalidate();
    }

    public void setTextColor(int color) {
        this.text_color = color;
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initialize() {
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        this.setFocusable(false);
    }

    public void load(String data) {
        String str = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Auto-render test</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/katex/katex.min.css\">\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/themes/style.css\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\"/>\n" +
                "    <link rel=\"stylesheet\" href=\"file:///android_asset/webviewstyle.css\"/>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            padding: 0;\n" +
                "            margin: 0;\n" +
                "            font-size:" + this.text_size + "px;\n" +
                "            color:" + getHexColor(this.text_color) + ";" +
                "            display: none;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "{formula}\n" +
                "</body>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/katex.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/contrib/auto-render.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/contrib/auto-render.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/latex_parser.js\"></script>\n" +
                "<script>\n" +
                "    window.onload = function () {\n" +
                "        f();\n" +
                "        document.getElementsByTagName('BODY')[0].style.display = \"block\";\n" +
                "    }\n" +
                "</script>" +
                "</html>\n";
        str = str.replace("{formula}", data);
        this.loadDataWithBaseURL("null", str, "text/html", "UTF-8", "about:blank");
    }
}
