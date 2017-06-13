package manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
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

    private int screenWidth;
    private int screenLength;
    public static float titleFontSize = -1;
    public static float subFontSize = -1;
    public static float selectorTitleSize = -1;
    public static float selectorSubSize = -1;
    public static float dialogFragmentTitle = -1;
    public static float dialogFragmentSub = -1;
    private double titleFontBias = 0.21;
    private double subFontBias = 0.12;
    private double selectorTitleBias = 0.14;
    private double selectorSubBias = 0.10;
    private double dialogFragmentTitleBias = 0.14;
    private double dialogFragmentSubBias = 0.10;

    private SharedPreferences mPrefs;
    DisplayAppearanceCallback mAppearanceCallback;

    public DisplayAppearanceManager(Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setupDisplayAppearance(Display display) {
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenLength = size.y;
        float title = (float) (screenWidth * titleFontBias / 10f);
        float rest = (float) (screenWidth * subFontBias / 10);
        float selectorTitle = (float) (screenWidth * selectorTitleBias / 10);
        float selectorSub = (float) (screenWidth * selectorSubBias / 10);
        float dialogTitle = (float) (screenWidth * dialogFragmentTitleBias / 10);
        float dialogSub = (float) (screenWidth * dialogFragmentSubBias / 10);
        setScreenWidth(screenWidth);
        setScreenLength(screenLength);
        setTitleFontSize(title);
        setSubFontSize(rest);
        setSelectorTitleSize(selectorTitle);
        setSelectorSubSize(selectorSub);
        setDialogFragmentTitle(dialogTitle);
        setDialogFragmentSub(dialogSub);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(SCREEN_WIDTH, screenWidth);
        editor.putInt(SCREEN_LENGTH, screenLength);
        editor.putFloat(FONT_TITLE, title);
        editor.putFloat(FONT_REST, rest);
        editor.putFloat(FONT_SELECTOR_TITLE, selectorTitle);
        editor.putFloat(FONT_SELECTOR_SUB, selectorSub);
        editor.putFloat(FONT_DIALOG_TITLE, dialogTitle);
        editor.putFloat(FONT_DIALOG_SUB, dialogSub);
        editor.apply();
    }

    public void initializeDisplayAppearance() {
        screenWidth = mPrefs.getInt(SCREEN_WIDTH, -1);
        screenLength = mPrefs.getInt(SCREEN_LENGTH, -1);
        titleFontSize = mPrefs.getFloat(FONT_TITLE, -1);
        subFontSize = mPrefs.getFloat(FONT_REST, -1);
        selectorTitleSize = mPrefs.getFloat(FONT_SELECTOR_TITLE, -1);
        selectorSubSize = mPrefs.getFloat(FONT_SELECTOR_SUB, -1);
        dialogFragmentTitle = mPrefs.getFloat(FONT_DIALOG_TITLE, -1);
        dialogFragmentSub = mPrefs.getFloat(FONT_DIALOG_SUB, -1);
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

    public double getDialogFragmentTitle() {
        return dialogFragmentTitle;
    }

    public double getDialogFragmentSub() {
        return dialogFragmentSub;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenLength(int screenLength) {
        this.screenLength = screenLength;
    }

    public void setTitleFontSize(float titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public void setSubFontSize(float subFontSize) {
        this.subFontSize = subFontSize;
    }

    public void setSelectorTitleSize(float selectorTitleSize) {
        this.selectorTitleSize = selectorTitleSize;
    }

    public void setSelectorSubSize(float selectorSubSize) {
        this.selectorSubSize = selectorSubSize;
    }

    public void setDialogFragmentTitle(float dialogFragmentTitle) {
        this.dialogFragmentTitle = dialogFragmentTitle;
    }

    public void setDialogFragmentSub(float dialogFragmentSub) {
        this.dialogFragmentSub = dialogFragmentSub;
    }
}
