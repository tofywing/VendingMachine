package pillar_technology.vendingmachine;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import adapter.ItemSlideAdapter;
import fragment.ItemSummaryFragment;
import fragment.VendingMachineFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_machine);
        mPreferences = this.getSharedPreferences(TAG_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        mItems = getPreferenceData();
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
        Item cola = new Item(R.drawable.cola, "Cola", 30, 1.00, 30, 0);
        Item chips = new Item(R.drawable.chips, "Chips", 30, 0.50, 30, 0);
        Item candy = new Item(R.drawable.candy, "Candy", 30, 0.65, 30, 0);
        items.addItem(cola);
        items.addItem(chips);
        items.addItem(candy);
        items.addMachineCredit(50d);
        return items;
    }

    public static Items reloadVendingMachine(Items items) {
        if (items == null) {
            return initialVendingMachine();
        } else {
            for (Item item : items.getItems()) {
                item.setAmount(item.getAmount() + 30);
            }
            items.addMachineCredit(50d);
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
        if (id == R.id.action_bar_selector_reload) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.selector_machine_specs_layout,
                    null);
            builder.setView(view);
            mItemReload = (CheckBox) view.findViewById(R.id.item_reload);
            mCreditReload = (CheckBox) view.findViewById(R.id.credit_reload);
            builder.setCancelable(true);
            builder.setPositiveButton(getString(R.string.alert_btn_reload), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String snakeBarString = "";
                    if (!mItemReload.isChecked() && !mCreditReload.isChecked()) {
                        snakeBarString = getString(R.string.machine_specs_no_selection_made);
                    } else {
                        if (mItemReload.isChecked() && mCreditReload.isChecked()) {
                            snakeBarString = getString(R.string.machine_specs_item_credit_loaded, 30, 50d);
                        } else if (mItemReload.isChecked()) {
                            snakeBarString = getString(R.string.machine_specs_item_loaded, 30);
                        } else if (mCreditReload.isChecked()) {
                            snakeBarString = getString(R.string.machine_specs_credit_loaded, 50d);
                        }
                        mItems = VendingMachineActivity.reloadVendingMachine(mItems);
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
            builder.setCancelable(true);
            builder.setView(R.layout.selector_factory_reset_layout);
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
}
