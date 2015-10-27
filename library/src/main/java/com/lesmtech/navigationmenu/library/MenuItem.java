package com.lesmtech.navigationmenu.library;

import android.content.Intent;
import android.view.View;

/**
 * Object to store MenuItem and Intent
 * @author Rindt
 * @version 0.1
 * @since 10/27/15
 */
public class MenuItem {

    private View view;

    private Intent intent;

    public MenuItem(View view, Intent intent) {
        this.view = view;
        this.intent = intent;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
