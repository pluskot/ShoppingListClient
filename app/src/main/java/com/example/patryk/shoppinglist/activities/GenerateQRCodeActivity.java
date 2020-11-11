package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.patryk.shoppinglist.R;
import com.google.common.collect.ImmutableMap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.lang.ref.WeakReference;

public class GenerateQRCodeActivity extends BaseActivity {
    private ImageView mQrCodeView;
    private String generatedJson;
    private View mProgressView;
    private View mQRCodeForm;

    public ImageView getQrCodeView() {
        return mQrCodeView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatedJson = getIntent().getStringExtra("QR_JSON");
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_generated_qr_code, contentFrameLayout);
        mQrCodeView = findViewById(R.id.qrCodeView);
        mProgressView = findViewById(R.id.progress);
        mQRCodeForm = findViewById(R.id.qrCodeForm);
        showQRCode();
    }

    private void showQRCode() {
        showProgress(true);
        new QRCodeGenerator(this).execute(generatedJson);
    }

    private static class QRCodeGenerator extends AsyncTask<String, Void, Bitmap> {
        public final static int WIDTH = 600;
        public final static int HEIGHT = 600;
        private final WeakReference<GenerateQRCodeActivity> activityReference;

        public QRCodeGenerator(Context context) {
            activityReference = new WeakReference(context);
        }

        protected Bitmap doInBackground(String... urls) {
            String Value = urls[0];
            com.google.zxing.Writer writer = new QRCodeWriter();
            Bitmap bitmap = null;
            BitMatrix bitMatrix;
            try {
                bitMatrix = writer.encode(Value, com.google.zxing.BarcodeFormat.QR_CODE, WIDTH, HEIGHT,
                        ImmutableMap.of(EncodeHintType.MARGIN, 1));
                bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK
                                : Color.WHITE);
                    }
                }
            } catch (WriterException e) {
                GenerateQRCodeActivity activity = activityReference.get();
                activity.showProgress(false);
                e.printStackTrace();
                Toast.makeText(activity, "Error occurred", Toast.LENGTH_SHORT).show();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            GenerateQRCodeActivity activity = activityReference.get();
            activity.getQrCodeView().setScaleType(ImageView.ScaleType.FIT_CENTER);
            activity.getQrCodeView().setImageBitmap(result);
            activity.showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mQRCodeForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mQRCodeForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mQRCodeForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
