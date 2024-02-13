package com.szi.plantbuddy;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected void startActivityWithoutFinish(Class<? extends AppCompatActivity> cl) {
        Intent intent = new Intent(this, cl);
        startActivity(intent);
    }

    public void onHome(View view) {
        if (!this.getClass().equals(MainActivity.class))
            startActivityWithoutFinish(MainActivity.class);
    }

    public void onPlantsCollection(View view) {
        if (!this.getClass().equals(PlantsCollectionActivity.class))
            startActivityWithoutFinish(PlantsCollectionActivity.class);
    }
}
