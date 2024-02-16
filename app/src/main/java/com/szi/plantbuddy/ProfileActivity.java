package com.szi.plantbuddy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options) {
            startActivityWithoutFinish(OptionsActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}