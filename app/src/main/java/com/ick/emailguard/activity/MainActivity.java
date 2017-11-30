package com.ick.emailguard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ick.emailguard.R;
import com.ick.emailguard.fragment.InboxFragment;
import com.ick.emailguard.fragment.SentFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_inbox);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());

        // Handle navigation view item clicks here.
//        int id = item.getItemId();

//        if (id == R.id.nav_inbox) {
//        } else if (id == R.id.nav_sent) {
//            Toast.makeText(getApplicationContext(), "Menu Belum Berfungsi", Toast.LENGTH_LONG).show();
//
//        } else if (id == R.id.nav_drafts) {
//            Toast.makeText(getApplicationContext(), "Menu Belum Berfungsi", Toast.LENGTH_LONG).show();
//
//        } else if (id == R.id.nav_logout) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Do you want to logout?");
//            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                }
//            });
//            builder.create().show();
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_inbox:
                fragment = new InboxFragment();
                break;
            case R.id.nav_sent:
                fragment = new SentFragment();
                break;
            case R.id.nav_logout:
//                fragment = new Menu3();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
