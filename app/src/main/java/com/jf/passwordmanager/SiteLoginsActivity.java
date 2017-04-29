package com.jf.passwordmanager;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import helper.DatabaseAdapter;
import helper.DeleteLoginsDialogFragment;

public class SiteLoginsActivity extends AppCompatActivity
        implements DeleteLoginsDialogFragment.DeleteLoginsListener{

    public static final String EXTRA_ID = "com.jf.passwordmanager._ID";

    private ListView listView;
    private DatabaseAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_logins);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Database handler
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SiteLoginsActivity.this, NewLoginActivity.class);
                startActivity(intent);

            }
        });

        displayListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site_logins, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all_logins) {
            showDeleteDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDeleteDialog() {
        // create an instance of the delete dialog and show it
        DialogFragment dialog = new DeleteLoginsDialogFragment();
        dialog.show(getFragmentManager(), "DeleteDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        dbHelper.deleteAllLogins();
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

        // refreshes the listview
        this.onResume();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }

    private void displayListView() {

        Cursor cursor = dbHelper.getAllLogins();

        // column names to display
        String[] columns = new String[]{
                DatabaseAdapter.KEY_SITE_NAME,
                DatabaseAdapter.KEY_USERNAME,
        };

        // layout to use for list view
        int[] to = new int[] {
                R.id.listItemSiteName,
                R.id.listItemUsername
        };

        dataAdapter = new SimpleCursorAdapter(this,
                R.layout.login_list_item,
                cursor,
                columns,
                to,
                0);

        listView = (ListView) findViewById(R.id.list_view);
        // assigning adapter to listview
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(SiteLoginsActivity.this, ViewActivity.class);
                intent.putExtra(EXTRA_ID, String.valueOf(id));

                startActivity(intent);
            }
        });
    }
}
