package com.aquamorph.frcdrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    Controls uiManager;
    PacketSender sender;
    PhysicalJoystick physicalJoystick;
    Toolbar toolbar;

    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wifi:
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                return true;
            case R.id.tutorial:
                openTutorial();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, Preference.class);
        startActivity(intent);
    }

    public void openTutorial() {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.controls);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.wifi:
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                        return true;
                    case R.id.tutorial:
                        openTutorial();
                        return true;
                    case R.id.action_settings:
                        openSettings();
                        return true;
                    default:
                        return true;
                }
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.main);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiManager = new Controls(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            physicalJoystick = new PhysicalJoystick(this);
        }
        sender = new PacketSender(this, uiManager, physicalJoystick);
        sender.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sender.interrupt();
        sender = null;
    }

}
