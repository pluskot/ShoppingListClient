package com.example.patryk.shoppinglist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.patryk.shoppinglist.R;
import com.example.patryk.shoppinglist.adapters.EntryListAdapter;
import com.example.patryk.shoppinglist.models.Entry;
import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.models.User;
import com.example.patryk.shoppinglist.models.WholeShoppingList;
import com.example.patryk.shoppinglist.services.EntryService;
import com.example.patryk.shoppinglist.services.ServiceGenerator;
import com.example.patryk.shoppinglist.services.ShoppingListService;
import com.example.patryk.shoppinglist.utils.Token;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingListDetailActivity extends BaseActivity {
    private View mProgressView;
    private View mEntriesView;
    private ListView mEntriesListView;
    private EntryListAdapter adapter;
    private FloatingActionButton mAddEntryButton;
    private FloatingActionButton mGenerateQRCode;
    private FloatingActionButton mSendToFriendButton;
    private static boolean cancel = false;
    private ShoppingList shoppingList;
    private static final int RESULT_CODE = 1847;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingList = (ShoppingList) getIntent().getSerializableExtra("SHOPPING_LIST");
        this.setTitle(shoppingList.getName());
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_shopping_list_detail, contentFrameLayout);
        mProgressView = findViewById(R.id.progress);
        mEntriesView = findViewById(R.id.entries);
        mEntriesListView = findViewById(R.id.entriesListView);
        mEntriesListView.setOnItemClickListener((parent, view, position, id) -> {
            if (adapter.getSelectedItems().contains(adapter.getItem(position))) {
                adapter.getSelectedItems().remove(adapter.getItem(position));
            } else {
                adapter.getSelectedItems().add(adapter.getItem(position));
            }
            adapter.notifyDataSetChanged();
        });
        registerForContextMenu(mEntriesListView);
        mAddEntryButton = findViewById(R.id.addEntry);
        mAddEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShoppingListDetailActivity.this, AddEntryActivity.class);
            intent.putExtra("SHOPPING_LIST", shoppingList);
            startActivity(intent);
        });

        mGenerateQRCode = findViewById(R.id.generateQRCode);
        mGenerateQRCode.setOnClickListener(v -> {
            String json = createQRCode();
            Intent intent = new Intent(ShoppingListDetailActivity.this, GenerateQRCodeActivity.class);
            intent.putExtra("QR_JSON", json);
            startActivity(intent);
        });

        mSendToFriendButton = findViewById(R.id.sendToFriend);
        mSendToFriendButton.setOnClickListener(v -> {
            Intent intent = new Intent(ShoppingListDetailActivity.this, MyFriendsActivity.class);
            intent.putExtra("SHOPPING_LIST", shoppingList);
            startActivityForResult(intent, RESULT_CODE);
        });

        if (shoppingList.getId() != -1) {
            attemptGetEntries(shoppingList.getId());
        } else {
            Intent intent = new Intent(this, MyShoppingListsActivity.class);
            startActivity(intent);
        }
    }

    private String createQRCode() {
        showProgress(true);
        WholeShoppingList wholeShoppingList =
                new WholeShoppingList(Token.getInstance().getUser(), shoppingList, Entry.currentEntries);
        Gson gson = new Gson();
        String json = gson.toJson(wholeShoppingList);
        Log.i("JSON", json);
        return json;
    }

    private void attemptGetEntries(int shoppingListId) {
        cancel = false;
        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            EntryService service =
                    ServiceGenerator.createService(EntryService.class, true, Token.getInstance().getToken());
            Call<List<Entry>> call = service.getEntries(shoppingListId);
            call.enqueue(new Callback<List<Entry>>() {
                @Override
                public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                    if (response.isSuccessful()) {
                        Log.d("GET_ENTRIES", "success - response is " + response.body());
                        Entry.currentEntries = response.body();
                        ShoppingListDetailActivity.this.adapter = new EntryListAdapter(Entry.currentEntries,
                                R.layout.entry_list_view_item, ShoppingListDetailActivity.this);
                        ShoppingListDetailActivity.this.mEntriesListView.setAdapter(adapter);
                    } else {
                        Log.d("GET_ENTRIES", "failure response is " + response.raw().toString());
                        cancel = true;
                    }
                    ShoppingListDetailActivity.this.showProgress(false);
                }

                @Override
                public void onFailure(Call<List<Entry>> call, Throwable t) {
                    Log.d("GET_ENTRIES", " Error :  " + t.getMessage());
                    cancel = true;
                    ShoppingListDetailActivity.this.showProgress(false);
                }
            });
        }
    }

    private void attemptDeleteEntry(final int entryId, final int position) {
        cancel = false;
        if (!cancel) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            EntryService service =
                    ServiceGenerator.createService(EntryService.class, true, Token.getInstance().getToken());
            Call<Entry> call = service.deleteEntry(entryId);
            call.enqueue(new Callback<Entry>() {
                @Override
                public void onResponse(Call<Entry> call, Response<Entry> response) {
                    if (response.isSuccessful()) {
                        Log.d("DELETE_ENTRY", "success - response is " + response.body());
                        Entry.currentEntries.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("DELETE_ENTRY", "failure response is " + response.raw().toString());
                        cancel = true;
                    }
                    ShoppingListDetailActivity.this.showProgress(false);
                }

                @Override
                public void onFailure(Call<Entry> call, Throwable t) {
                    Log.d("DELETE_ENTRY", " Error :  " + t.getMessage());
                    cancel = true;
                    ShoppingListDetailActivity.this.showProgress(false);
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

        mEntriesView.setVisibility(show ? View.GONE : View.VISIBLE);
        mEntriesView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mEntriesView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.entriesListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Entry entry = adapter.getItem(info.position);
                if (entry != null) {
                    attemptDeleteEntry(entry.getId(), info.position);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE) { // Please, use a final int instead of hardcoded int value
            if (resultCode == RESULT_OK) {
                userId = data.getExtras().getInt("USER_ID");
                User user = new User(userId);
                WholeShoppingList wholeShoppingList = new WholeShoppingList(user, shoppingList, Entry.currentEntries);
                attemptCreateWholeShoppingList(wholeShoppingList);
            }
        }
    }

    void attemptCreateWholeShoppingList(WholeShoppingList shoppingList) {
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_read_qr_code, contentFrameLayout);
        mProgressView = findViewById(R.id.progress);
        showProgress(true);
        ShoppingListService service =
                ServiceGenerator.createService(ShoppingListService.class, true, Token.getInstance().getToken());
        Call<Void> call = service.createWholeShoppingList(shoppingList);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CREATE_WHOLE_LIST", "success - response is " + response.body());
                    ShoppingListDetailActivity.this.showProgress(false);
                    Toast.makeText(ShoppingListDetailActivity.this, "Udostępniono", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("CREATE_WHOLE_LIST", "failure response is " + response.raw().toString());
                    ShoppingListDetailActivity.this.showProgress(false);
                    Toast.makeText(ShoppingListDetailActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CREATE_WHOLE_LIST", " Error :  " + t.getMessage());
                ShoppingListDetailActivity.this.showProgress(false);
                Toast.makeText(ShoppingListDetailActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
