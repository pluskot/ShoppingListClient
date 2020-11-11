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
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.UserFriend;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.UserFriendService;
import com.example.patryk.shoppinglist.utils.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends BaseActivity {
    private View mProgressView;
    private View mAddFriendFormView;
    private FloatingActionButton mAddFriendButton;
    private EditText mUsernameView;
    private static boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_add_friend, contentFrameLayout);
        mUsernameView = findViewById(R.id.friend_username);
        mProgressView = findViewById(R.id.add_friend_progress);
        mAddFriendFormView = findViewById(R.id.add_friend);
        mAddFriendButton = findViewById(R.id.addFriendButton);
        mAddFriendButton.setOnClickListener(v -> {
            mUsernameView.setError(null);
            if (mUsernameView.getText().toString().equals("")) {
                mUsernameView.setError(getString(R.string.error_field_required));
            } else {
                attemptAddFriend();
            }
        });
    }

    private void attemptAddFriend() {
        cancel = false;
        mUsernameView.setError(null);
        if (!cancel) {
            /*
             Show a progress spinner, and kick off a background task to
             perform the user login attempt.
            */
            showProgress(true);
            UserFriendService service =
                    ServiceGenerator.createService(UserFriendService.class, true, Token.getInstance().getToken());
            Call<UserFriend> call = service.addFriend(mUsernameView.getText().toString());
            call.enqueue(new Callback<UserFriend>() {
                @Override
                public void onResponse(Call<UserFriend> call, Response<UserFriend> response) {
                    if (response.isSuccessful()) {
                        Log.d("ADD_FRIEND", "success - response is " + response.body());
                        AddFriendActivity.this.showProgress(false);
                        Intent intent = new Intent(AddFriendActivity.this, MyFriendsActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("ADD_FRIEND", "failure response is " + response.raw().toString());
                        cancel = true;
                        mUsernameView.setError(getString(R.string.error_username_invalid));
                        AddFriendActivity.this.showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<UserFriend> call, Throwable t) {
                    Log.d("ADD_FRIEND", " Error :  " + t.getMessage());
                    cancel = true;
                    mUsernameView.setError(getString(R.string.error_username_invalid));
                    AddFriendActivity.this.showProgress(false);
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

        mAddFriendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mAddFriendFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAddFriendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
