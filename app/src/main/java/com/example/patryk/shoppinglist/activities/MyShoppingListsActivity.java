package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.adapters.ShoppingListListAdapter;
import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.ShoppingListService;
import com.example.patryk.shoppinglist.utils.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShoppingListsActivity extends BaseActivity {
    private View mProgressView;
    private View mShoppingListsView;
    private ListView mShoppingListsListView;
    private ShoppingListListAdapter adapter;
    private FloatingActionButton mAddShoppingListButton;
    private static boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_my_shopping_lists, contentFrameLayout);
        mProgressView = findViewById(R.id.progress);
        mShoppingListsView = findViewById(R.id.shoppingLists);
        mShoppingListsListView = findViewById(R.id.shoppingListsListView);
        mShoppingListsListView.setOnItemClickListener((parent, view, position, id) -> {
            Object o = mShoppingListsListView.getItemAtPosition(position);
            ShoppingList list = (ShoppingList) o;
            if(list != null) {
                Intent intent = new Intent(MyShoppingListsActivity.this, ShoppingListDetailActivity.class);
                intent.putExtra("SHOPPING_LIST", list);
                startActivity(intent);
            }
        });


        mAddShoppingListButton = findViewById(R.id.addShoppingList);
        mAddShoppingListButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyShoppingListsActivity.this, AddShoppingListActivity.class);
            startActivity(intent);
        });
        attemptGetShoppingLists();
    }


    private void attemptGetShoppingLists() {
        cancel = false;
        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            ShoppingListService service =
                    ServiceGenerator.createService(ShoppingListService.class, true, Token.getInstance().getToken());
            Call<List<ShoppingList>> call = service.getMyShoppingLists();
            call.enqueue(new Callback<List<ShoppingList>>() {
                @Override
                public void onResponse(Call<List<ShoppingList>> call, Response<List<ShoppingList>> response) {
                    if (response.isSuccessful()) {
                        Log.d("GET_SHOPPING_LISTS", "success - response is " + response.body());
                        ShoppingList.currentShoppingLists = response.body();
                        MyShoppingListsActivity.this.adapter = new ShoppingListListAdapter(ShoppingList.currentShoppingLists,
                                R.layout.list_view_item, MyShoppingListsActivity.this);
                        MyShoppingListsActivity.this.mShoppingListsListView.setAdapter(adapter);
                        MyShoppingListsActivity.this.showProgress(false);
                    } else {
                        Log.d("GET_SHOPPING_LISTS", "failure response is " + response.raw().toString());
                        cancel = true;
                        MyShoppingListsActivity.this.showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<List<ShoppingList>> call, Throwable t) {
                    Log.d("GET_SHOPPING_LISTS", " Error :  " + t.getMessage());
                    cancel = true;
                    MyShoppingListsActivity.this.showProgress(false);
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mShoppingListsView.setVisibility(show ? View.GONE : View.VISIBLE);
        mShoppingListsView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShoppingListsView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
