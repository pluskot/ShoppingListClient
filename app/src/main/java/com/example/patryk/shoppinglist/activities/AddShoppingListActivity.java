package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.models.UserFriend;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.ShoppingListService;
import com.example.patryk.shoppinglist.services.UserFriendService;
import com.example.patryk.shoppinglist.utils.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShoppingListActivity extends BaseActivity {

    private View mProgressView;
    private View mAddShoppingListFormView;
    private FloatingActionButton mShoppingListButton;
    private EditText mNameView;
    private static boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_add_shopping_list, contentFrameLayout);
        mNameView = findViewById(R.id.name);
        mProgressView = findViewById(R.id.progress);
        mAddShoppingListFormView = findViewById(R.id.add_shopping_list);
        mShoppingListButton = findViewById(R.id.addShoppingList);
        mShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameView.setError(null);
                if (mNameView.getText().equals("")) {
                    mNameView.setError(getString(R.string.error_field_required));
                } else {
                    attemptAddShoppingList();
                }
            }
        });
    }

    private void attemptAddShoppingList() {
        cancel = false;
        mNameView.setError(null);
        if (!cancel) {
            showProgress(true);
            ShoppingListService service =
                    ServiceGenerator.createService(ShoppingListService.class, true, Token.getInstance().getToken());
            Call<ShoppingList> call = service.addShoppingList(mNameView.getText().toString());
            call.enqueue(new Callback<ShoppingList>() {
                @Override
                public void onResponse(Call<ShoppingList> call, Response<ShoppingList> response) {
                    if (response.isSuccessful()) {
                        Log.d("ADD_SHOPPING_LIST", "success - response is " + response.body());
                        AddShoppingListActivity.this.showProgress(false);
                        Intent intent =
                                new Intent(AddShoppingListActivity.this, MyShoppingListsActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("ADD_SHOPPING_LIST", "failure response is " + response.raw().toString());
                        cancel = true;
                        mNameView.setError(getString(R.string.error_generic));
                        AddShoppingListActivity.this.showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<ShoppingList> call, Throwable t) {
                    Log.d("ADD_SHOPPING_LIST", " Error :  " + t.getMessage());
                    cancel = true;
                    mNameView.setError(getString(R.string.error_generic));
                    AddShoppingListActivity.this.showProgress(false);
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddShoppingListFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddShoppingListFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddShoppingListFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddShoppingListFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
