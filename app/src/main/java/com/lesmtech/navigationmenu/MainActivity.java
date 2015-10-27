package com.lesmtech.navigationmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.lesmtech.navigationmenu.library.NavigationMenuLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.navigation_menu_layout)
    NavigationMenuLayout navigationMenuLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    // Test
    @Bind(R.id.header_container)
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        View header = LayoutInflater.from(this).inflate(R.layout.header, null);
        View bottom = LayoutInflater.from(this).inflate(R.layout.bottom, null);
        LinearLayout content = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.content, null);

        // might create
        ArrayList<com.lesmtech.navigationmenu.library.MenuItem> item = new ArrayList<>();

        for (int i = 0; i < content.getChildCount(); i++) {
            final View view = content.getChildAt(i);
            item.add(new com.lesmtech.navigationmenu.library.MenuItem(view, new Intent(this, com.lesmtech.navigationmenu.TestActivity.class)));
        }

        navigationMenuLayout.addHeaderLayout(header);
        navigationMenuLayout.addBottomContainer(bottom);
        navigationMenuLayout.addContentLayout(content, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navigationMenuLayout.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @OnClick(R.id.action_search)
//    public void actionSearch() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
//        view.startAnimation(anim);
//    }
}
