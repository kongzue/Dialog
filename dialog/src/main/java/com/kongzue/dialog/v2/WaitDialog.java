package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.util.BaseDialog;

import static com.kongzue.dialog.v2.DialogSettings.THEME_DARK;
import static com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT;
import static com.kongzue.dialog.v2.DialogSettings.tip_text_size;
import static com.kongzue.dialog.v2.DialogSettings.tip_theme;

public class WaitDialog extends BaseDialog {

    private AlertDialog alertDialog;
    private static WaitDialog waitDialog;
    private boolean isCanCancel = false;

    private Context context;
    private String tip;

    private WaitDialog() {
    }

    public static WaitDialog show(Context context, String tip) {
        synchronized (WaitDialog.class) {
            if (waitDialog == null) waitDialog = new WaitDialog();
            waitDialog.context = context;
            waitDialog.tip = tip;
            waitDialog.showDialog();
            waitDialog.log("显示等待对话框 -> " + tip);
            return waitDialog;
        }
    }

    private RelativeLayout boxInfo;
    private RelativeLayout bkg;
    private TextView txtInfo;

    private void showDialog() {
        AlertDialog.Builder builder;
        int bkgResId;
        switch (tip_theme) {
            case THEME_LIGHT:
                builder = new AlertDialog.Builder(context, R.style.lightMode);
                bkgResId = R.drawable.rect_light;
                break;
            default:
                builder = new AlertDialog.Builder(context, R.style.darkMode);
                bkgResId = R.drawable.rect_dark;
                break;
        }
        builder.setCancelable(isCanCancel);

        alertDialog = builder.create();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_progressbar);

        boxInfo = (RelativeLayout) window.findViewById(R.id.box_info);
        bkg = (RelativeLayout) window.findViewById(R.id.bkg);
        txtInfo = (TextView) window.findViewById(R.id.txt_info);

        bkg.setBackgroundResource(bkgResId);

        if (!tip.isEmpty()) {
            boxInfo.setVisibility(View.VISIBLE);
            txtInfo.setText(tip);
        } else {
            boxInfo.setVisibility(View.GONE);
        }
        if (tip_text_size > 0) {
            txtInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tip_text_size);
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialogLifeCycleListener != null) dialogLifeCycleListener.onDismiss();
            }
        });
        alertDialog.show();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onShow(alertDialog);
    }

    public WaitDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }

    public static void dismiss() {
        synchronized (WaitDialog.class) {
            if (waitDialog != null) {
                if (waitDialog.alertDialog != null) waitDialog.alertDialog.dismiss();
            }
        }
    }
}
