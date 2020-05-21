package katex.hourglass.in.mathlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MathView extends WebView {
    private static final String TAG = "error_log";
    private static final float default_text_size = 18;
    private String display_text;
    boolean blink;
    private int text_color;
    private int text_size;
    private boolean clickable = false;
    private boolean scrollable = true;


    public MathView(Context context) {
        super(context);
        configurationSettingWebView(scrollable);
        setDefaultTextColor(context);
        setDefaultTextSize();
    }


    public MathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configurationSettingWebView(scrollable);
        TypedArray mTypeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MathView,
                0, 0);
        try {
            setBackgroundColor(mTypeArray.getInteger(R.styleable.MathView_setViewBackgroundColor, ContextCompat.getColor(context, android.R.color.transparent)));
            setTextColor(mTypeArray.getColor(R.styleable.MathView_setTextColor, ContextCompat.getColor(context, android.R.color.black)));
            pixelSizeConversion(mTypeArray.getDimension(R.styleable.MathView_setTextSize, default_text_size));
            setDisplayText(mTypeArray.getString(R.styleable.MathView_setText));
            setClickable(mTypeArray.getBoolean(R.styleable.MathView_setClickable, true));
        } catch (Exception e) {
            Log.d(TAG, "Exception:" + e.toString());
        }
    }

    public void setViewBackgroundColor(int color) {
        setBackgroundColor(color);
        this.invalidate();
    }

    private void pixelSizeConversion(float dimension) {
        if (dimension == default_text_size) {
            setTextSize((int) default_text_size);
        } else {
            int pixel_dimen_equivalent_size = (int) ((double) dimension / 1.6);
            setTextSize(pixel_dimen_equivalent_size);
        }
    }


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void configurationSettingWebView(boolean enable_zoom_in_controls) {
        this.setFocusable(false);
        this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.setHorizontalScrollBarEnabled(enable_zoom_in_controls);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        /**This is for to set wide */
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setDisplayZoomControls(enable_zoom_in_controls);
//        settings.setBuiltInZoomControls(enable_zoom_in_controls);
//        settings.setSupportZoom(enable_zoom_in_controls);
        Log.d(TAG, "Zoom in controls:" + enable_zoom_in_controls);
    }


    public void setDisplayText(String formula_text) {
        this.display_text = formula_text;
        loadData();
    }

    public void loadDataFirstTime() {
        if (this.display_text != null) {
            this.loadDataWithBaseURL("null", getOfflineKatexConfig(), "text/html", "UTF-8", "about:blank");
        }
    }


    private String getOfflineKatexConfig() {
//        String offline_config = "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "\n" +
//                "<head>\n" +
//                "    <meta charset=\"utf-8\">\n" +
//                "    <link rel=\"stylesheet\" href=\"file:///android_asset/jqmath-0.4.3.css\">\n" +
//                "    <style type='text/css'>body {\n" +
//                "        padding: 0;" +
//                "        margin: 0;" +
//                "        font-size:" + this.text_size + "px;\n" +
//                "        color:" + getHexColor(this.text_color) + ";" +
//                "    } </style>\n" +
//                "</head>\n" +
//                "<script src=\"file:///android_asset/jquery-1.4.3.min.js\"></script>\n" +
//                "<script src=\"file:///android_asset/jqmath-etc-0.4.6.min.js\" charset=\"utf-8\"></script>\n" +
//                "<body style=\"display: none\" onload=\"myFunction()\">{formula}\n" +
//                "</body>\n" +
//                "<script src=\"file:///android_asset/lastResort.js\"></script>\n" +
//                "</html>";
        return "<!DOCTYPE html>\n" +
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
                "            display: none;" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "</body>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/ASCIIMathTeXImg.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/lastResort2.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/katex.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/contrib/auto-render.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/katex/contrib/auto-render.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/jquery.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/latex_parser.js\"></script>\n";
    }

    private static String formatTex(String str) {
        if (str != null) {
            str = str.replace("Ans/", "{Ans}/");
            str = str.replace("/Ans", "/{Ans}");
//            str = str.replaceAll("([^+)]+|\\([^()]+\\))\\/", "{" + "$1" + "}/");
//            Log.d(TAG, str);
//            str = str.replace("()", "");
        }
//        str = matchingBrackets(str);
//        if (str.length() > 0) {
//            str = "$" + str + "$";
//        }
        return str;
    }

//    private static String matchingBrackets(String str) {
//        char[] toReturn = str.toCharArray();
//        ArrayList<Integer> temp = new ArrayList<>();
//        ArrayList<Integer> temp2 = new ArrayList<>();
//        for (int i = 0; i < str.length(); i++) {
//            if (str.charAt(i) == '(') {
//                if (i > 0 && str.charAt(i - 1) == '/') {
//                    temp2.add(i);
//                }
//                temp.add(i);
//            }
//            if (str.charAt(i) == ')') {
//                if (!temp.isEmpty() && !temp2.isEmpty() && temp.get(temp.size() - 1).equals(temp2.get(temp2.size() - 1))) {
//                    temp2.remove(temp2.size() - 1);
//                    toReturn[i] = '}';
//                    toReturn[temp.get(temp.size() - 1)] = '{';
//                }
//                if (i < str.length() - 2 && str.charAt(i + 1) == '/') {
//                    toReturn[i] = '}';
//                    toReturn[temp.get(temp.size() - 1)] = '{';
//                }
//                if (!temp.isEmpty()) {
//                    temp.remove(temp.size() - 1);
//                }
//            }
//        }
//        StringBuilder strBuilder = new StringBuilder(new String(toReturn));
//        while (!temp.isEmpty()) {
//            temp.remove(temp.size() - 1);
//            strBuilder.append(")");
//        }
//        return strBuilder.toString();
//        return new String(toReturn);
//    }

    public void loadData() {
        if (this.display_text == null) {
            this.display_text = "";
        }
        ArrayList<String> scripts = new ArrayList<>();
        String script1;
        if (blink) {
            script1 = "myFunction(\"{formula}\", true);";
        } else {
            script1 = "myFunction(\"{formula}\", false);";
        }
        script1 = script1.replace("{formula}", formatTex(this.display_text));
        scripts.add(script1);
        scripts.add("f();");
        scripts.add("document.getElementsByTagName('BODY')[0].style.display = \"inline\";");
        if (this.display_text != null) {
            for (String script : scripts) {
                this.evaluateJavascript(script, null);
            }
        }
    }

    public void setBlink(boolean blink) {
        this.blink = blink;
    }

    public void setTextSize(int size) {
        this.text_size = size;
        loadData();

    }

    public void setTextColor(int color) {
        this.text_color = color;
        loadData();
    }

    private String getHexColor(int intColor) {
        //Android and javascript color format differ javascript support Hex color, so the android color which user sets is converted to hexcolor to replicate the same in javascript.
        String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
        Log.d(TAG, "Hex Color:" + hexColor);
        return hexColor;
    }


    private void setDefaultTextColor(Context context) {
        //sets default text color to black
        this.text_color = ContextCompat.getColor(context, android.R.color.black);

    }

    private void setDefaultTextSize() {
        //sets view default text size to 18
        this.text_size = (int) default_text_size;
    }

    public void setClickable(boolean is_clickable) {
        this.setEnabled(true);
        this.clickable = is_clickable;
        this.scrollable = !is_clickable;
        configurationSettingWebView(this.scrollable);
        this.invalidate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.clickable && event.getAction() == MotionEvent.ACTION_DOWN) {
            this.callOnClick();
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

}
