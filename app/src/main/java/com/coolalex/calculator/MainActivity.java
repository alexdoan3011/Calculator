package com.coolalex.calculator;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;
import java.util.Locale;

import katex.hourglass.in.mathlib.MathView;

public class MainActivity extends AppCompatActivity {
    private static boolean activityVisible;

    //    private AppBarConfiguration mAppBarConfiguration;
    private static final ArrayList<String> displayString = new ArrayList<>();
    private static final ArrayList<String> displayStringNatural = new ArrayList<>();
    private static final ArrayList<String> calculate = new ArrayList<>();
    //    private static boolean hapticPerformed = false;
    //    static ArrayList<String> openBrackets = new ArrayList<>();
//    static ArrayList<String> closeBracketMissing = new ArrayList<>();
    private static final String TAG = "coolalex_display";
    private static boolean exit = false;
    private static PopupWindow popupWindow;
    private static Object popup;
    private static int editor;
    //    private static boolean lineBreak = false;
    private static double ans;
    private static boolean calculated = false;
    private static final Boo blinkOn = new Boo();
    private static boolean blink = true;
    private Button left;
    private Button right;
    private Button num0;
    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button plus;
    private Button minus;
    private Button multiply;
    private Button divide;
    private Button enter;
    private Button dot;
    private Button clr;
    private Button openBracket;
    private Button closeBracket;
    private Button shift;
    private Button backSpace;
    private Button tab;
    //    TextView display;
    private TextView result;
    private MathView mathView;
    private FrameLayout loading;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loading();
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        //initialize elements
        initialize();
        blinkOn.setListener(new Boo.ChangeListener() {
            @Override
            public void onChange() {
                if (blink && isActivityVisible()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            String str;
//                            if (blinkOn.isTrue()) {
//                                ArrayList<String> temp = new ArrayList<>(MainActivity.displayString);
//                                temp.add(editor, "|");
//                                str = displayArrayListToString(temp);
//                            } else {
//                                str = displayArrayListToString(MainActivity.displayString);
//                            }
                            display(null, null, blinkOn.isTrue());
                        }
                    });
                }
            }
        });
//        display.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (display.getText().equals("")) {
//                    calculated = true;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (display.getText().equals("")) {
//                    calculated = true;
//                }
//            }
//        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                if (calculated) {
                    calculated = false;
                    blink = true;
                } else {
                    if (editor > 0) {
                        editor--;
                        if (editor > 2 && displayString.get(editor - 1).equals(")") && displayString.get(editor).equals("/(")) {
                            editor--;
                        }
                        display(null, null, true);
                    }
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                if (calculated) {
                    calculated = false;
                    blink = true;
                } else {
                    if (editor == calculate.size()) {
                        if (bracketUnbalanced()) {
//                            hapticPerformed = true;
                            closeBracket.performClick();
                        } else if (editor < displayString.size()) {
                            editor++;
                            display(null, null, true);
                        }
                    }
                    if (editor < displayString.size()) {
                        editor++;
                        if (editor > 1 && editor < displayString.size() && displayString.get(editor - 1).equals(")") && displayString.get(editor).equals("/(")) {
                            editor++;
                        }
                        display(null, null, true);
                    }
                }
            }
        });
        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "0");
            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "1");
            }
        });
        longClickButtonListener(num1, true, true, R.string._1_alternate_display, R.string._1_alternate_calculate, R.string._1_alternate_display_natural, R.string._1_alternate_label);

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "2");
            }
        });

        longClickButtonListener(num2, false, true, R.string._2_alternate_display, R.string._2_alternate_calculate, R.string._2_alternate_display_natural, R.string._2_alternate_label);

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "3");
            }
        });
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "4");
            }
        });
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "5");
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "6");
            }
        });
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "7");
            }
        });
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "8");
            }
        });
        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                numberButtonOnClick(view, "9");
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                operandButtonOnClick(view, false, "+");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                operandButtonOnClick(view, false, "-");
            }
        });
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                operandButtonOnClick(view, false, "×", "*");
            }
        });
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                operandButtonOnClick(view, true, "/(");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                stringButtonOnClick(view, false, R.string.dot);
            }
        });
        openBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                stringButtonOnClick(view, true, R.string.open_bracket);
            }
        });
        closeBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                if (bracketUnbalanced()) {
                    stringButtonOnClick(view, false, R.string.close_bracket);
                }
            }
        });
        shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                Utils.showToast(getApplicationContext(), "Function in progress");
//                stringButtonOnClick(view, false, R.string.ans);
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                calculated = true;
                clear(null);
            }
        });
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                if (displayString.size() > 0 && editor != 0) {
                    calculated = false;
                    blink = true;
                    editor--;
                    String regexOpenBracket = "^[^(]*\\($";
                    if (calculate.get(editor).matches(regexOpenBracket)) {
                        int count = 0;
                        int size = calculate.size();
                        for (int i = editor + 1; i < size; i++) {
                            if (calculate.get(i).matches(regexOpenBracket)) {
                                count++;
                            } else if (calculate.get(i).equals(")")) {
                                if (count == 0) {
                                    removeFromArrayLists(i);
                                    break;
                                }
                                count--;
                            }
                        }
                    }
                    if (editor >= 2 && calculate.get(editor).equals("/(")) {
                        if (calculate.get(editor - 1).equals(")")) {
                            removeFromArrayLists(editor - 1);
                            editor--;
                        }
                        for (int i = editor - 1; i >= 0; i--) {
                            if (calculate.get(i).equals("+") || calculate.get(i).equals("-") || calculate.get(i).equals("*") || calculate.get(i).equals("/(")) {
                                break;
                            }
                            if (calculate.get(i).matches(regexOpenBracket)) {
                                removeFromArrayLists(i);
                                editor--;
                                break;
                            }
                        }
                    }
//                    String regex = "^[^)]*_(\\d*)$";
//                    String regex2 = "^\\)*_(\\d*)$";
//                    String temp = displayString.get(editor);
//                    if (temp.matches(regex2)) {
//                        Pattern pattern = Pattern.compile(regex2);
//                        Matcher matcher = pattern.matcher(temp);
//                        if (matcher.find()) {
//                            closeBracketMissing.add(matcher.group(1));
//                        }
////                            editor--;
//                    }
//                    if (temp.matches(regex)) {
//                        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
//                        Matcher matcher = pattern.matcher(temp);
//                        String code = "";
//                        if (matcher.find()) {
//                            code = matcher.group(1);
//                        }
//                        regex = "^[^_]*_" + code + "$";
//                        for (int i = displayString.size() - 1; i >= 0; i--) {
//                            if (displayString.get(i).matches(regex)) {
//                                displayString.remove(i);
//                                displayStringNatural.remove(i);
//                                calculate.remove(i);
//                            }
//                        }
//                    } else {
                    removeFromArrayLists(editor);
//                    }
                    display(null, null, true);
                }
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                while (bracketUnbalanced()) {
                    displayString.add(")");
                    displayStringNatural.add(")");
                    calculate.add(")");
                }
                calculated = true;
                blink = false;
                blinkOn.is(false);
                display(null, null, false);
                String calculateTemp = calculateArrayListToString();
                Double resultNum = calculate(calculateTemp);
                String temp = String.format(Locale.CANADA, "%f", resultNum);
                if (resultNum == null) {
                    temp = "Syntax Error";
                    result.setText(temp);
                    return;
                }
                if (Double.isInfinite(resultNum) || temp.equals("NaN") || resultNum > Math.pow(10, 99)) {
                    temp = "Math Error";
                    result.setText(temp);
                    return;
                }
                if (resultNum > Math.pow(10, 10) && !Double.isInfinite(resultNum)) {
                    ans = resultNum;
                    int pow = 0;
                    double resultNumTemp = resultNum;
                    while (resultNumTemp > 1000) {
                        resultNumTemp = resultNumTemp / 10;
                        pow++;
                    }
                    temp = (double) ((int) resultNumTemp) / 100 + "E" + (pow + 2);
                } else {
                    ans = resultNum;
                    int cutIndex = -1;
                    int size = temp.length();
                    for (int i = size - 1; i >= 0; i--) {
                        if (temp.charAt(i) == '0') {
                            if (temp.charAt(i - 1) == '.') {
                                cutIndex = i - 1;
                                break;
                            }
                            cutIndex = i;
                        } else {
                            break;
                        }
                    }
                    if (cutIndex != -1) {
                        temp = temp.substring(0, cutIndex);
                    }
                }
                result.setText(temp);
            }
        });
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.haptic(view);
                Utils.showToast(getApplicationContext(), "Function in progress");
            }
        });

        longClickButtonListener(enter, false, false, R.string.ans);
    }

    public static void removeFromArrayLists(int index) {
        displayString.remove(index);
        displayStringNatural.remove(index);
        calculate.remove(index);
    }

    static boolean bracketUnbalanced() {
        int count = 0;
        int size = displayString.size();
        for (int i = 0; i < size; i++) {
            if (displayString.get(i).matches("[^(]*\\([^(]*")) {
                count++;
            }
            if (displayString.get(i).equals(")")) {
                count--;
            }
        }
        return count > 0;
    }

    private void loading() {
        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(200);
        loading = findViewById(R.id.loading);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeOut);
        loading.setAnimation(animation);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
//        progressBar(getColor(android.R.color.black));
        progressBar.setIndeterminate(false);
        progressBar.setProgress(0);
        progressBar.setMax(400);
        (new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int count = 0;
                int count2 = 0;
                while (count <= 500) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count2 += 1;
                    count += count2 * count2;
                    progressBar.setProgress(count);
                }
            }

        }).start();
        loading.setVisibility(View.INVISIBLE);
    }

    public void longClickButtonListener(final View button, final boolean closeBracket, final boolean math, final int... alternateFunction) {
        final String alternateFunctionLabel = alternateFunction.length == 4 ? getString(alternateFunction[3]) : getString(alternateFunction[0]);
        button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// The touch just ended
                    LayoutInflater inflater = getLayoutInflater();
                    final Rect temp = locateView(num0);
                    if (math) {
                        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.keypopmath, null);
                        popupWindow = new PopupWindow(popupView, temp.width(), temp.height());
                        popup = popupWindow.getContentView().findViewById(R.id.pop);
                        ((MathLabel) popup).setVisibility(View.INVISIBLE);
                        ((MathLabel) popup).load("$" + alternateFunctionLabel + "$");
                    } else {
                        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.keypop, null);
                        popupWindow = new PopupWindow(popupView, temp.width(), temp.height());
                        popup = popupWindow.getContentView().findViewById(R.id.pop);
                        ((TextView) popup).setVisibility(View.INVISIBLE);
                        ((TextView) popup).setText(alternateFunctionLabel);
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {// The touch just ended
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickPopup(button, popup, math);
                stringButtonOnClick(null, closeBracket, alternateFunction);
                return true;
            }
        });
    }

    private static void clickPopup(final View button, Object popup, boolean math) {
        if (popup != null) {
            final Rect temp = locateView(button);
            popupWindow.showAsDropDown(button, 0, -temp.height() * 2, Gravity.CENTER);
            if (math) {
                ((MathLabel) popup).setVisibility(View.VISIBLE);
            } else {
                ((TextView) popup).setVisibility(View.VISIBLE);
            }
        }
    }

    private static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!event.isShiftPressed()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (exit) {
                        clear(null);
                        finish();
                    } else {
                        exit = true;
                        Utils.showToast(getApplicationContext(), "Press back again to exit");
                        (new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                exit = false;
                            }
                        }).start();
                    }
                    return true;
                case KeyEvent.KEYCODE_1:
                case KeyEvent.KEYCODE_NUMPAD_1:
                    num1.performClick();
                    return true;
                case KeyEvent.KEYCODE_2:
                case KeyEvent.KEYCODE_NUMPAD_2:
                    num2.performClick();
                    return true;
                case KeyEvent.KEYCODE_3:
                case KeyEvent.KEYCODE_NUMPAD_3:
                    num3.performClick();
                    return true;
                case KeyEvent.KEYCODE_4:
                case KeyEvent.KEYCODE_NUMPAD_4:
                    num4.performClick();
                    return true;
                case KeyEvent.KEYCODE_5:
                case KeyEvent.KEYCODE_NUMPAD_5:
                    num5.performClick();
                    return true;
                case KeyEvent.KEYCODE_6:
                case KeyEvent.KEYCODE_NUMPAD_6:
                    num6.performClick();
                    return true;
                case KeyEvent.KEYCODE_7:
                case KeyEvent.KEYCODE_NUMPAD_7:
                    num7.performClick();
                    return true;
                case KeyEvent.KEYCODE_8:
                case KeyEvent.KEYCODE_NUMPAD_8:
                    num8.performClick();
                    return true;
                case KeyEvent.KEYCODE_9:
                case KeyEvent.KEYCODE_NUMPAD_9:
                    num9.performClick();
                    return true;
                case KeyEvent.KEYCODE_0:
                case KeyEvent.KEYCODE_NUMPAD_0:
                    num0.performClick();
                    return true;
                case KeyEvent.KEYCODE_NUMPAD_ADD:
                    plus.performClick();
                    return true;
                case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                case KeyEvent.KEYCODE_MINUS:
                    minus.performClick();
                    return true;
                case KeyEvent.KEYCODE_NUMPAD_MULTIPLY:
                    multiply.performClick();
                    return true;
                case KeyEvent.KEYCODE_SLASH:
                    divide.performClick();
                    return true;
                case KeyEvent.KEYCODE_DEL:
                case KeyEvent.KEYCODE_FORWARD_DEL:
                    backSpace.performClick();
                    return true;
                case KeyEvent.KEYCODE_NUMPAD_DOT:
                case KeyEvent.KEYCODE_PERIOD:
                    dot.performClick();
                    return true;
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                    enter.performClick();
                    return true;
                case KeyEvent.KEYCODE_TAB:
                    tab.performClick();
                    return true;
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_EQUALS:
                    plus.performClick();
                    return true;
                case KeyEvent.KEYCODE_8:
                    multiply.performClick();
                    return true;
                case KeyEvent.KEYCODE_9:
                    openBracket.performClick();
                    return true;
                case KeyEvent.KEYCODE_0:
                    closeBracket.performClick();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * initialize all interact elements
     */
    private void initialize() {
        mathView = findViewById(R.id.mathView);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        num0 = findViewById(R.id.num0);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        multiply = findViewById(R.id.multiply);
        divide = findViewById(R.id.divide);
        openBracket = findViewById(R.id.openBracket);
        closeBracket = findViewById(R.id.closeBracket);
        backSpace = findViewById(R.id.backSpace);
        enter = findViewById(R.id.enter);
        dot = findViewById(R.id.dot);
        shift = findViewById(R.id.shift);
        clr = findViewById(R.id.clr);
//        display = findViewById((R.id.display));
        result = findViewById((R.id.result));
        tab = findViewById(R.id.tab);
        display(null, null, true);
        mathView.setTextSize(20);
        mathView.setClickable(false);
        mathView.loadDataFirstTime();

        new Thread() {
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                while (true) if (blink && isActivityVisible()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (blink) {
                        blinkOn.is(true);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (blink) {
                        blinkOn.is(false);
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.activityPaused();
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    public void numberButtonOnClick(View view, String num) {
        blink = false;
        if (calculated) {
            calculated = false;
            clear(null);
        }
        if (displayString.size() <= 80) {
            displayString.add(editor, num);
            displayStringNatural.add(editor, num);
            calculate.add(editor, num);
            editor++;
            display(null, null, true);
        }
        blink = true;
    }
//
//    public static void Utils.haptic(View view) {
//        view.performUtils.HapticFeedback(Utils.HapticFeedbackConstants.VIRTUAL_KEY, Utils.HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
//    }

    public void display(String custom, String customNatural, boolean indicator) {
        if (editor > displayString.size()) {
            editor = displayString.size();
        }
        String temp;
        String temp2;
        mathView.setBlink(indicator);
//        String str2 = displayArrayListToString(temp);
        if (custom == null && customNatural != null) {
            ArrayList<String> a = new ArrayList<>(displayString);
            a.add(editor, "|");
            temp = displayArrayListToString(a);
            temp2 = customNatural;
            Log.d(TAG, temp);
//            display.setText(temp);
            mathView.setDisplayText(temp2);
        } else if (custom != null && customNatural == null) {
//            temp = custom;
//            Log.d(TAG, temp);
//            display.setText(temp);
            Log.d(TAG, custom);
            ArrayList<String> temp3 = new ArrayList<>(MainActivity.displayStringNatural);
            temp3.add(editor, "|");
            temp2 = displayArrayListToString(temp3);
            mathView.setDisplayText(temp2);
        } else if (custom != null) {
            temp2 = customNatural;
            Log.d(TAG, custom);
//            display.setText(temp);
            mathView.setDisplayText(temp2);
        } else {
            ArrayList<String> a = new ArrayList<>(displayString);
            a.add(editor, "|");
            temp = displayArrayListToString(a);
            Log.d(TAG, temp);
//            display.setText(temp);
            ArrayList<String> temp3 = new ArrayList<>(MainActivity.displayStringNatural);
            temp3.add(editor, "|");
            temp2 = displayArrayListToString(temp3);
            mathView.setDisplayText(temp2);
        }
    }

//    private String bracketToClose() {
//        String regex = "^\\(*_(\\d*)$";
//        for (int i = editor - 1; i >= 0; i--) {
//            String temp = displayString.get(i);
//            if (temp.matches(regex)) {
//                temp = temp.replace("(_", "");
//                if (closeBracketMissing.contains(temp)) {
//                    closeBracketMissing.remove(temp);
//                    return temp;
//                }
//            }
//        }
//        return ")";
//    }

    public static String displayArrayListToString(ArrayList<String> displayString) {
        StringBuilder temp = new StringBuilder();
        for (String s : displayString) {
            temp.append(s);
        }
        return temp.toString();
    }

    public static String calculateArrayListToString() {
        StringBuilder temp = new StringBuilder();
        for (String s : calculate) {
            temp.append(s);
        }
        String str = temp.toString();
        str = str.replaceAll("(\\d)[Aa]ns", "$1" + "*" + "ans");
        str = str.replaceAll("[Aa]ns(\\d)", "not allowed");
        str = str.replaceAll("(\\d)sqrt\\(", "$1" + "*" + "sqrt(");
        str = str.replaceAll("(\\d)\\(", "$1" + "*" + "(");
        str = str.toLowerCase().replace("ans", Double.toString(ans));
        return str;
    }

    public void stringButtonOnClick(View view, boolean closeBracket, int... strings) {
        blink = false;
        String tmp;
//        if (getString(strings[0]).equals(")")) {
//            tmp = ")_"+bracketToClose();
//        } else {
        tmp = getString(strings[0]);
//        }
        final String stringDisplay = tmp;
        final String stringCalculate = strings.length > 1 ? getString(strings[1]) : getString(strings[0]);
        final String stringDisplayNatural = strings.length > 2 ? getString(strings[2]) : getString(strings[0]);
        if (displayString.size() <= 80) {
            if (calculated) {
                calculated = false;
                clear(null);
            }
//            int temp = editor;
            displayString.add(editor, stringDisplay);
            displayStringNatural.add(editor, stringDisplayNatural);
            calculate.add(editor, stringCalculate);
            editor++;
            if (closeBracket) {
//                closeBracketMissing.add(Integer.toString(temp));
                displayString.add(editor, ")");
                displayStringNatural.add(editor, ")");
                calculate.add(editor, ")");
            }
        }
        display(null, null, true);
        blink = true;
    }

    public void operandButtonOnClick(View view, boolean closeBracket, String... operand) {
        blink = false;
        String operandDisplay;
        String operandCalculate;
        String operandDisplayNatural;
        if (operand.length > 2) {
            operandDisplay = operand[0];
            operandCalculate = operand[1];
            operandDisplayNatural = operand[2];
        } else if (operand.length > 1) {
            operandDisplay = operand[0];
            operandCalculate = operand[1];
            operandDisplayNatural = operand[0];
        } else {
            operandDisplay = operand[0];
            operandCalculate = operand[0];
            operandDisplayNatural = operand[0];
        }
        if (displayString.size() <= 80) {
            if (calculated) {
                calculated = false;
                clear(null);
                // add ans if it's the start of a new calculation
                displayString.add(editor, "Ans");
                displayStringNatural.add(editor, "Ans");
                calculate.add(editor, "ans");
                editor++;
            }
//            int temp = editor;
            if (operandCalculate.equals("/(")) {
                int count = 0;
                if (editor > 0 && !displayString.get(editor - 1).equals(")")) {
                    for (int i = editor - 1; i >= 0; i--) {
                        if (i == 0 || (calculate.get(i - 1).equals("+") || calculate.get(i - 1).equals("-") || calculate.get(i - 1).equals("*") || calculate.get(i - 1).equals("/(")) && count == 0) {
                            displayString.add(i, "(");
                            displayStringNatural.add(i, "(");
                            calculate.add(i, "(");
                            editor++;
                            break;
                        }
                        if (displayString.get(i).equals(")")) {
                            count++;
                        }
                        if (displayString.get(i).matches("[^(]*\\([^(]*")) {
                            count--;
                        }
                    }
                    displayString.add(editor, ")");
                    displayStringNatural.add(editor, ")");
                    calculate.add(editor, ")");
                    editor++;
                }
            }
            displayString.add(editor, operandDisplay);
            displayStringNatural.add(editor, operandDisplayNatural);
            calculate.add(editor, operandCalculate);
            editor++;
            if (closeBracket) {
                displayString.add(editor, ")");
                displayStringNatural.add(editor, ")");
                calculate.add(editor, ")");
            }
        }
        display(null, null, true);
        blink = true;
    }

    public void clear(TextView display) {
        this.blink = true;
//        lineBreak = false;
        displayString.clear();
        displayStringNatural.clear();
        calculate.clear();
        editor = 0;
        if (display == null) {
            this.result.setText("");
        }
        display(null, null, false);
    }

    public static Double calculate(String input) {
        Double result;
        Expression eval = new Expression(input);
        if (!eval.checkLexSyntax()) {
            return null;
        }
        result = eval.calculate();
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}
