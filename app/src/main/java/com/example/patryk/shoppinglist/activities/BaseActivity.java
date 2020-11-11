package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.UserFriend;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.UserFriendService;
import com.example.patryk.shoppinglist.utils.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateNavBarTitle() {
        if (Token.getInstance() != null) {
            ((TextView) findViewById(R.id.navBarTitle)).setText(Token.getInstance().getUser().getUsername());
            ((TextView) findViewById(R.id.navBarSubtitle)).setText(Token.getInstance().getUser().getEmail());
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        updateNavBarTitle();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home_screen) {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_shopping_lists) {
            Intent intent = new Intent(this, MyShoppingListsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_import) {
            Intent intent = new Intent(this, ReadQRCodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {
            Intent view = new Intent(this, MyFriendsActivity.class);
            startActivity(view);
        } else if (id == R.id.nav_sync) {
            Toast.makeText(this, "Not implemented yet", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_log_in_out) {
            Intent loginView = new Intent(this, LoginActivity.class);
            Token.clearInstance();
            startActivity(loginView);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
