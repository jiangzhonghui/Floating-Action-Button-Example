package com.metova.floatingactionbuttonexample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                initFABs();
            }
        }, 1000);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean fab3Slid = false;
    private boolean fab4Slid = false;
    private void initFABs() {

        fab1 = new FloatingActionButton.Builder(this)
                .withButtonColor(getResources().getColor(R.color.app_blue))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 10, 10)
                .withText("right", getResources().getColor(R.color.app_white))
                .withVisibility(false)
                .create();

        fab2 = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp))
                .withButtonColor(getResources().getColor(R.color.app_red))
                .withGravity(Gravity.BOTTOM | Gravity.LEFT)
                .withMargins(10, 0, 0, 10)
                .withVisibility(false)
                .create();

        fab3 = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.mipmap.ic_launcher))
                .withButtonColor(getResources().getColor(R.color.app_blue))
                .withGravity(Gravity.TOP | Gravity.LEFT)
                .withMargins(10, 10, 0, 0)
                .withVisibility(false)
                .create();

        fab4 = new FloatingActionButton.Builder(this)
                .withText("movable", getResources().getColor(android.R.color.black))
                .withButtonColor(getResources().getColor(R.color.app_white))
                .withGravity(Gravity.TOP | Gravity.RIGHT)
                .withMargins(0, 10, 10, 0)
                .withVisibility(false)
                .create();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fab2.isHidden()) {
                    fab2.showFloatingActionButton();
                }
                else {
                    fab2.hideFloatingActionButton();
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fab1.isHidden()) {
                    fab1.showFloatingActionButton();
                }
                else {
                    fab1.hideFloatingActionButton();
                }
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fab3Slid) {
                    fab3.slideToGravity(Gravity.CENTER);
                    fab3Slid = true;
                }
                else {
                    fab3.slideToGravity(Gravity.TOP | Gravity.LEFT);
                    fab3Slid = false;
                }
            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fab4Slid) {
                    fab4.slideToGravity(Gravity.CENTER);
                    fab4Slid = true;
                }
                else {
                    fab4.slideToGravity(Gravity.TOP | Gravity.RIGHT);
                    fab4Slid = false;
                }
            }
        });

        fab1.showFloatingActionButton();
        fab2.showFloatingActionButton();
        fab3.showFloatingActionButton();
        fab4.showFloatingActionButton();
    }
}
