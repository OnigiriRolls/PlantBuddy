package com.szi.plantbuddy.util;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.szi.plantbuddy.BaseActivity;
import com.szi.plantbuddy.R;

public class WaitAnimationDialog {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static boolean shouldContinueAnimation = true;
    private static AnimatedVectorDrawable anim;

    public static AlertDialog showLoadingDialog(BaseActivity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        ImageView flowerAnim = dialogView.findViewById(R.id.flowerAnim);
        anim = (AnimatedVectorDrawable) flowerAnim.getDrawable();
        Animatable2.AnimationCallback animationCallback = new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                if (shouldContinueAnimation) {
                    handler.postDelayed(() -> anim.start(), 500);
                }
            }
        };
        anim.registerAnimationCallback(animationCallback);

        anim.start();

        return dialog;
    }

    public static void hideLoadingDialog(AlertDialog dialog) {
        Log.d("debug", "in hide");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("debug", "after thread sleep");
        if (dialog != null && dialog.isShowing()) {
            shouldContinueAnimation = false;
            handler.removeCallbacksAndMessages(null);
            anim.clearAnimationCallbacks();
            dialog.dismiss();
        }
    }
}
