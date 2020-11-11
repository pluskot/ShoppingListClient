package com.example.patryk.shoppinglist.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.models.WholeShoppingList;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.ShoppingListService;
import com.example.patryk.shoppinglist.utils.Token;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadQRCodeActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_ID = 12354;
    private ZXingScannerView mScannerView;
    private String[] permissions = {Manifest.permission.CAMERA};
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initComponents();
        } else {
            requestPermissions(permissions, REQUEST_CAMERA_ID);
        }
        initComponents();
    }

    private void initComponents() {
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));// Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("READ_QR", rawResult.getText()); // Prints scan results
        String decodedString = rawResult.getText();
        attemptCreateWholeShoppingList(decodedString);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initComponents();
                } else {
                    Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    void attemptCreateWholeShoppingList(String decodedString) {
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_read_qr_code, contentFrameLayout);
        mProgressView = findViewById(R.id.progress);
        showProgress(true);
        ShoppingListService service =
                ServiceGenerator.createService(ShoppingListService.class, true, Token.getInstance().getToken());
        Gson gson = new Gson();
        WholeShoppingList shoppingList = gson.fromJson(decodedString, WholeShoppingList.class);
        Call<Void> call = service.createWholeShoppingList(shoppingList);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CREATE_WHOLE_LIST", "success - response is " + response.body());
                    ReadQRCodeActivity.this.showProgress(false);
                } else {
                    Log.d("CREATE_WHOLE_LIST", "failure response is " + response.raw().toString());
                    ReadQRCodeActivity.this.showProgress(false);
                }
                startShoppingListsActivity();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CREATE_WHOLE_LIST", " Error :  " + t.getMessage());
                ReadQRCodeActivity.this.showProgress(false);
                startShoppingListsActivity();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
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
        }
    }

    void startShoppingListsActivity() {
        Intent intent = new Intent(this, MyShoppingListsActivity.class);
        startActivity(intent);
    }
}
