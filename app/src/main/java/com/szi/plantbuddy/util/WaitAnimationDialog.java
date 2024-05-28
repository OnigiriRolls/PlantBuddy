package com.szi.plantbuddy.util;

import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.szi.plantbuddy.BaseActivity;
import com.szi.plantbuddy.R;
import com.szi.plantbuddy.ui.IOnDialogDismissListener;

public class WaitAnimationDialog {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int MINIM_LOOPS = 2;
    private AnimatedVectorDrawable anim;
    private boolean shouldContinueAnimation = true;
    private AlertDialog dialog;
    private int loopsCounter = 1;

    public void showLoadingDialog(BaseActivity activity, IOnDialogDismissListener onDismissListener) {
        loopsCounter = 1;
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
                if (shouldContinueAnimation || loopsCounter < MINIM_LOOPS) {
                    loopsCounter++;
                    handler.postDelayed(() -> anim.start(), 500);
                } else {
                    handler.removeCallbacksAndMessages(null);
                    anim.clearAnimationCallbacks();
                    if (onDismissListener != null) {
                        onDismissListener.onDismiss();
                    }
                }
            }
        };
        anim.registerAnimationCallback(animationCallback);
        dialog.show();
        anim.start();
        this.dialog = dialog;
    }

    public void stopAnimationWhenDone() {
        if (dialog != null && dialog.isShowing()) {
            shouldContinueAnimation = false;
        }
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
