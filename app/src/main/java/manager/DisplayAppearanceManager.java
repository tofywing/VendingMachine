package manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import callback.DisplayAppearanceCallback;

/**
 * Created by Yee on 4/24/16.
 */
public class DisplayAppearanceManager {
    private static final String TAG = "Manager";
    private static final String PREFERENCE = "sharedPreference";
    private static final String SCREEN_WIDTH = "screenWidth";
    private static final String SCREEN_LENGTH = "screenLength";
    private static final String FONT_TITLE = "VendingMachineFragmentTitleFont";
    private static final String FONT_REST = "VendingMachineFragmentRestFont";
    private static final String FONT_SELECTOR_TITLE = "VendingMachineFragmentSelectorTitleFont";
    private static final String FONT_SELECTOR_SUB = "VendingMachineFragmentSelectorSubFont";
    private static final String FONT_DIALOG_TITLE = "InsertCoinDialogTitle";
    private static final String FONT_DIALOG_SUB = "InsertCoinDialogSub";
    private static final String COMPARE_RATIO = "CompareRatio";
    public static final float REFERENCE_RATIO = 0.602f;

    private int screenWidth;
    private int screenLength;

    public static float titleFontSize = -1;
    public static float subFontSize = -1;
    public static float selectorTitleSize = -1;
    public static float selectorSubSize = -1;
    public static float dialogFragmentTitle = -1;
    public static float dialogFragmentSub = -1;
    public static float compareRatio = -1;
    private float titleFontBias = 0.21f;
    private float subFontBias = 0.12f;
    private float selectorTitleBias = 0.14f;
    private float selectorSubBias = 0.12f;
    private float dialogFragmentTitleBias = 0.14f;
    private float dialogFragmentSubBias = 0.11f;

    private SharedPreferences mPrefs;
    DisplayAppearanceCallback mAppearanceCallback;

    public DisplayAppearanceManager(Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setupDisplayAppearance(Display display) {
        Log.d(TAG, "Appearance setup");
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenLength = size.y;
        float sizeRatio = screenWidth / (float) screenLength;
        float compareRatio = REFERENCE_RATIO / sizeRatio;
        float compareScreenWidth = screenWidth * compareRatio;
        float compareScreenLength = screenLength * compareRatio;
        Log.d(TAG, "compare ratio: " + compareRatio + "\n");
        float title = compareScreenWidth * titleFontBias / 10;
        float rest = compareScreenWidth * subFontBias / 10;
        float selectorTitle = compareScreenWidth * selectorTitleBias / 10;
        float selectorSub = compareScreenWidth * selectorSubBias / 10;
        float dialogTitle = compareScreenWidth * dialogFragmentTitleBias / 10;
        float dialogSub = compareScreenWidth * dialogFragmentSubBias / 10;
        setScreenWidth(screenWidth);
        setScreenLength(screenLength);
        setTitleFontSize(title);
        setSubFontSize(rest);
        setSelectorTitleSize(selectorTitle);
        setSelectorSubSize(selectorSub);
        setDialogFragmentTitle(dialogTitle);
        setDialogFragmentSub(dialogSub);
        setCompareRatio(compareRatio);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(SCREEN_WIDTH, screenWidth);
        editor.putInt(SCREEN_LENGTH, screenLength);
        editor.putFloat(FONT_TITLE, title);
        editor.putFloat(FONT_REST, rest);
        editor.putFloat(FONT_SELECTOR_TITLE, selectorTitle);
        editor.putFloat(FONT_SELECTOR_SUB, selectorSub);
        editor.putFloat(FONT_DIALOG_TITLE, dialogTitle);
        editor.putFloat(FONT_DIALOG_SUB, dialogSub);
        editor.putFloat(COMPARE_RATIO,compareRatio);
        editor.apply();
        Log.d(TAG, "Screen width: " + screenWidth + "\n"
                + "Screen length: " + screenLength + "\n"
                + "Title font: " + titleFontSize + "\n"
                + "Sub font: " + subFontSize + "\n"
                + "Selector title: " + selectorTitleSize + "\n"
                + "Selector sub: " + selectorSubSize + "\n"
                + "Dialog title: " + dialogFragmentTitle + "\n"
                + "Dialog sub: " + dialogFragmentSub + "\n" + compareRatio
        );
    }

    public void initializeDisplayAppearance() {
        Log.d(TAG, "Appearance initialize");
        screenWidth = mPrefs.getInt(SCREEN_WIDTH, -1);
        screenLength = mPrefs.getInt(SCREEN_LENGTH, -1);
        titleFontSize = mPrefs.getFloat(FONT_TITLE, -1);
        subFontSize = mPrefs.getFloat(FONT_REST, -1);
        selectorTitleSize = mPrefs.getFloat(FONT_SELECTOR_TITLE, -1);
        selectorSubSize = mPrefs.getFloat(FONT_SELECTOR_SUB, -1);
        dialogFragmentTitle = mPrefs.getFloat(FONT_DIALOG_TITLE, -1);
        dialogFragmentSub = mPrefs.getFloat(FONT_DIALOG_SUB, -1);
        compareRatio = mPrefs.getFloat(COMPARE_RATIO, - 1);
        Log.d(TAG, "Screen width: " + screenWidth + "\n"
                + "Screen length: " + screenLength + "\n"
                + "Title font: " + titleFontSize + "\n"
                + "Sub font: " + subFontSize + "\n"
                + "Selector title: " + selectorTitleSize + "\n"
                + "Selector sub: " + selectorSubSize + "\n"
                + "Dialog title: " + dialogFragmentTitle + "\n"
                + "Dialog sub: " + dialogFragmentSub + "\n" + compareRatio
        );
    }

    public void onAppearanceReady() {
        mAppearanceCallback.onAppearanceReady(titleFontSize, subFontSize, selectorTitleSize, selectorSubSize,
                dialogFragmentTitle, dialogFragmentSub);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenLength() {
        return screenLength;
    }

    public float getTitleFontSize() {
        return titleFontSize;
    }

    public float getSubFontSize() {
        return subFontSize;
    }

    public float getSelectorTitleSize() {
        return selectorTitleSize;
    }

    public float getSelectorSubSize() {
        return selectorSubSize;
    }

    public float getDialogFragmentTitle() {
        return dialogFragmentTitle;
    }

    public float getDialogFragmentSub() {
        return dialogFragmentSub;
    }

    public static float getCompareRatio() {
        return compareRatio;
    }

    private void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    private void setScreenLength(int screenLength) {
        this.screenLength = screenLength;
    }

    private void setTitleFontSize(float titleFontSize) {
        DisplayAppearanceManager.titleFontSize = titleFontSize;
    }

    private void setSubFontSize(float subFontSize) {
        DisplayAppearanceManager.subFontSize = subFontSize;
    }

    private void setSelectorTitleSize(float selectorTitleSize) {
        DisplayAppearanceManager.selectorTitleSize = selectorTitleSize;
    }

    private void setSelectorSubSize(float selectorSubSize) {
        DisplayAppearanceManager.selectorSubSize = selectorSubSize;
    }

    private void setDialogFragmentTitle(float dialogFragmentTitle) {
        DisplayAppearanceManager.dialogFragmentTitle = dialogFragmentTitle;
    }

    private void setDialogFragmentSub(float dialogFragmentSub) {
        DisplayAppearanceManager.dialogFragmentSub = dialogFragmentSub;
    }

    public static void setCompareRatio(float compareRatio) {
        DisplayAppearanceManager.compareRatio = compareRatio;
    }
}
