package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.Entry;
import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.models.Unit;
import com.example.patryk.shoppinglist.services.EntryService;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.utils.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEntryActivity extends BaseActivity {
    private View mProgressView;
    private View mProductForm;
    private View mQuantityForm;
    private FloatingActionButton mAddEntryButton;
    private EditText mProductName;
    private EditText mQuantity;
    private Spinner mUnit;
    private static boolean cancel = false;
    private Entry entry;
    private ShoppingList shoppingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingList = (ShoppingList) getIntent().getSerializableExtra("SHOPPING_LIST");
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_add_entry, contentFrameLayout);
        mProgressView = findViewById(R.id.progress);
        mProductName = findViewById(R.id.name);
        mProductForm = findViewById(R.id.productForm);
        mQuantity = findViewById(R.id.quantity);
        mQuantityForm = findViewById(R.id.quantityForm);
        mUnit = findViewById(R.id.unit);
        mUnit.setAdapter(new ArrayAdapter<Unit>(this, R.layout.spinner_item, Unit.values()));
        mAddEntryButton = findViewById(R.id.addEntry);
        mAddEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValid()) {
                    attemptAddEntry(entry);
                }
            }
        });
    }

    private boolean isFormValid(){
        String product = mProductName.getText().toString();
        Unit unit = (Unit) mUnit.getSelectedItem();
        String quantityString = mQuantity.getText().toString();
        if(!(product.isEmpty() || unit == null || quantityString.isEmpty())){
            int quantity = Integer.parseInt(quantityString);
            entry = new Entry(product, unit, quantity);
            return true;
        }
        return false;
    }

    private void attemptAddEntry(Entry entry) {
        cancel = false;
        mProductName.setError(null);
        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            EntryService service =
                    ServiceGenerator.createService(EntryService.class, true, Token.getInstance().getToken());
            Call<Entry> call = service.addEntry(entry, shoppingList.getId());
            call.enqueue(new Callback<Entry>() {
                @Override
                public void onResponse(Call<Entry> call, Response<Entry> response) {
                    if (response.isSuccessful()) {
                        Log.d("ADD_ENTRY", "success - response is " + response.body());
                        AddEntryActivity.this.showProgress(false);
                        Intent intent = new Intent(AddEntryActivity.this, ShoppingListDetailActivity.class);
                        intent.putExtra("SHOPPING_LIST", shoppingList);
                        startActivity(intent);
                    } else {
                        Log.d("ADD_ENTRY", "failure response is " + response.raw().toString());
                        cancel = true;
                        mProductName.setError(getString(R.string.error_generic));
                        AddEntryActivity.this.showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<Entry> call, Throwable t) {
                    Log.d("ADD_ENTRY", " Error :  " + t.getMessage());
                    cancel = true;
                    mProductName.setError(getString(R.string.error_generic));
                    AddEntryActivity.this.showProgress(false);
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

            mProductForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mQuantityForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mProductForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProductForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mQuantityForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mQuantityForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mProductForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mQuantityForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
