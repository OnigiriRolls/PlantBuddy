package com.szi.plantbuddy;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
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

    public void onProfile(View view) {
        if (!this.getClass().equals(ProfileActivity.class))
            startActivityWithoutFinish(ProfileActivity.class);
    }
}
