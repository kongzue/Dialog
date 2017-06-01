package com.kongzue.dialog.listener;

import android.view.View;

public interface InputDialogCallbackClickListener {
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    void onClick(View v, String inputText);
}