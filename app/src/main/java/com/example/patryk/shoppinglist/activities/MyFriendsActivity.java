package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.adapters.UserFriendListAdapter;
import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.models.User;
import com.example.patryk.shoppinglist.models.UserFriend;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.UserFriendService;
import com.example.patryk.shoppinglist.utils.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFriendsActivity extends BaseActivity {
    private View mProgressView;
    private View mFriendsFormView;
    private ListView mUserFriendsListView;
    private UserFriendListAdapter adapter;
    private FloatingActionButton mAddFriendButton;
    private static final int RESULT_CODE = 1847;
    private static boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_my_friends, contentFrameLayout);
        mProgressView = findViewById(R.id.my_friends_progress);
        mFriendsFormView = findViewById(R.id.my_friends);
        mUserFriendsListView = findViewById(R.id.myFriendsListView);
        if (getCallingActivity() != null) {
            if (getCallingActivity().getShortClassName().equals(".activities.ShoppingListDetailActivity")) {
                mUserFriendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        User user = adapter.getItem(position);
                        ShoppingList shoppingList = (ShoppingList) getIntent().getSerializableExtra("SHOPPING_LIST");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("SHOPPING_LIST", shoppingList);
                        resultIntent.putExtra("USER_ID", user.getId());
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
            }
        }
        mAddFriendButton = findViewById(R.id.addFriendButton);
        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });
        attemptGetFriends();
    }

    private void attemptGetFriends() {
        cancel = false;

        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            UserFriendService service =
                    ServiceGenerator.createService(UserFriendService.class, true, Token.getInstance().getToken());
            Call<List<User>> call = service.getMyFriends();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        Log.d("GET_MY_FRIENDS", "success - response is " + response.body());
                        UserFriend.currentFriends = response.body();
                        MyFriendsActivity.this.adapter = new UserFriendListAdapter(UserFriend.currentFriends, R.layout.list_view_item, MyFriendsActivity.this);
                        MyFriendsActivity.this.mUserFriendsListView.setAdapter(adapter);
                        MyFriendsActivity.this.showProgress(false);
                    } else {
                        Log.d("GET_MY_FRIENDS", "failure response is " + response.raw().toString());
                        cancel = true;
                        MyFriendsActivity.this.showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.d("GET_MY_FRIENDS", " Error :  " + t.getMessage());
                    cancel = true;
                    MyFriendsActivity.this.showProgress(false);
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

            mFriendsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFriendsFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFriendsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFriendsFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
