package pillar_technology.vendingmachine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import fragment.VendingMachineFragment;
import manager.DisplayAppearanceManager;
import model.Item;
import model.Items;

public class VendingMachineActivity extends AppCompatActivity {
    public static final String TAG = "VendingMachineActivity";
    public static final String TAG_ITEMS_DATA = "VendingMachineActivityItemsData";
    public static final String TAG_SHARED_PREFERENCE = "VendingMachineActivitySharedPreference";

    SharedPreferences mPreferences;
    Items mItems;
    Toolbar mToolbar;
    VendingMachineFragment mVendingMachineFragment;
    FragmentManager mFragmentManager;
    CheckBox mItemReload;
    CheckBox mCreditReload;
    TextView mMachineSpecTitle;
    TextView mMachineSpecSubTitle;
    TextView mFactoryResetTitle;
    TextView mFactoryResetSub;

    float selectorTitleSize;
    float selectorSubSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_machine);
        initialScreenAppearance();
        initialFontSize();
        mPreferences = this.getSharedPreferences(TAG_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        if (savedInstanceState != null) {
            mItems = savedInstanceState.getParcelable(TAG_ITEMS_DATA);
        } else {
            mItems = getPreferenceData();
        }
        if (mItems == null) {
            mItems = initialVendingMachine();
        }
        mFragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setSubtitle(R.string.subtitle_action_bar);
        setSupportActionBar(mToolbar);
        createVendingMachineFragment();
    }

    //Amount 30, Credit $50
    public static Items initialVendingMachine() {
        Items items = new Items();
        Item cola = new Item(R.drawable.cola, "Cola", 30, 100, 3000, 0);
        Item chips = new Item(R.drawable.chips, "Chips", 30, 50, 3000, 0);
        Item candy = new Item(R.drawable.candy, "Candy", 30, 65, 3000, 0);
        items.addItem(cola);
        items.addItem(chips);
        items.addItem(candy);
        items.addMachineCredit(5000);
        return items;
    }

    public static Items reloadVendingMachine(Items items, int amount, int credit) {
        if (items == null) {
            return initialVendingMachine();
        } else {
            for (Item item : items.getItems()) {
                item.setAmount(item.getAmount() + amount);
            }
            items.addMachineCredit(credit);
            return items;
        }
    }

    @Override
    protected void onDestroy() {
        savePreferenceData();
        super.onDestroy();
    }

    private void savePreferenceData() {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String json = gson.toJson(mItems);
        editor.putString(TAG_ITEMS_DATA, json);
        editor.apply();
    }

    private Items getPreferenceData() {
        String json = mPreferences.getString(TAG_ITEMS_DATA, "");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        return gson.fromJson(json, new TypeToken<Items>() {
        }.getType());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bar_machine_spec) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.selector_machine_specs_layout,
                    null);
            builder.setView(view);
            mMachineSpecTitle = (TextView) view.findViewById(R.id.machine_specs_title);
            mMachineSpecTitle.setTextSize(selectorTitleSize);
            mItemReload = (CheckBox) view.findViewById(R.id.item_reload);
            mItemReload.setTextSize(selectorTitleSize);
            mCreditReload = (CheckBox) view.findViewById(R.id.credit_reload);
            mCreditReload.setTextSize(selectorTitleSize);
            Log.d(TAG, mCreditReload.getTextSize() + " ");
            mMachineSpecSubTitle = (TextView) view.findViewById(R.id.machine_specs_subtitle);
            mMachineSpecSubTitle.setTextSize(selectorSubSize);
            Log.d(TAG, mMachineSpecSubTitle.getTextSize() + " ");
            mMachineSpecSubTitle.setText(getString(R.string.machine_specs_subtitle, mItems.getItemAmountSummary(),
                    mItems.getMachineCredit() / 100f));
            builder.setCancelable(true);
            builder.setPositiveButton(getString(R.string.alert_btn_reload), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String snakeBarString = "";
                    if (!mItemReload.isChecked() && !mCreditReload.isChecked()) {
                        snakeBarString = getString(R.string.machine_specs_no_selection_made);
                    } else {
                        if (mItemReload.isChecked() && mCreditReload.isChecked()) {
                            snakeBarString = getString(R.string.machine_specs_item_credit_loaded, 30, 5000/ 100f);
                            mItems = VendingMachineActivity.reloadVendingMachine(mItems, 30, 5000);
                        } else if (mItemReload.isChecked()) {
                            mItems = VendingMachineActivity.reloadVendingMachine(mItems, 30, 0);
                            snakeBarString = getString(R.string.machine_specs_item_loaded, 30);
                        } else if (mCreditReload.isChecked()) {
                            mItems = VendingMachineActivity.reloadVendingMachine(mItems, 0, 5000);
                            snakeBarString = getString(R.string.machine_specs_credit_loaded, 5000/100f);
                        }
                        mVendingMachineFragment = (VendingMachineFragment) mFragmentManager.findFragmentById(R.id
                                .vending_machine_fragment_container);
                        mVendingMachineFragment.generateUpdateViewPager(mVendingMachineFragment.getView());
                    }
                    if (mVendingMachineFragment.getView() != null) {
                        Snackbar.make(mVendingMachineFragment.getView(), snakeBarString, Snackbar.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, mItems.getMachineCredit() + "");
                }
            });
            builder.setNegativeButton(getString(R.string.alert_btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        if (id == R.id.action_bar_selector_factory_reset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.selector_factory_reset_layout,
                    null);
            builder.setView(view);
            mFactoryResetTitle = (TextView) view.findViewById(R.id.factory_reset_title);
            mFactoryResetTitle.setTextSize(selectorTitleSize);
            mFactoryResetSub = (TextView) view.findViewById(R.id.factory_reset_subtitle);
            mFactoryResetSub.setTextSize(selectorSubSize);
            builder.setCancelable(true);
            builder.setPositiveButton(getString(R.string.alert_btn_factory_reset), new DialogInterface
                    .OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mItems = initialVendingMachine();
                    createVendingMachineFragment();
                }
            });
            builder.setNegativeButton(getString(R.string.alert_btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createVendingMachineFragment() {
        mVendingMachineFragment = (VendingMachineFragment) mFragmentManager.findFragmentById(R.id
                .vending_machine_fragment_container);
        if (mVendingMachineFragment != null) {
            mFragmentManager.beginTransaction().remove(mVendingMachineFragment).commit();
        }
        mVendingMachineFragment = VendingMachineFragment.newInstance(mItems);
        mFragmentManager.beginTransaction().add(R.id.vending_machine_fragment_container, mVendingMachineFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG_ITEMS_DATA, mItems);
    }

    private void initialScreenAppearance() {
        DisplayAppearanceManager displayManager = new DisplayAppearanceManager(this);
        displayManager.initializeDisplayAppearance();
        if (displayManager.getTitleFontSize() == -1) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            displayManager.setupDisplayAppearance(display);
        }
    }

    private void initialFontSize() {
        selectorTitleSize = DisplayAppearanceManager.selectorTitleSize;
        selectorSubSize = DisplayAppearanceManager.selectorSubSize;
    }
}
