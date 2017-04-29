package com.jf.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import helper.DatabaseAdapter;

public class ViewActivity extends AppCompatActivity {

    // EditTexts
    private EditText siteNameField, usernameField, passwordField, notesField;

    private DatabaseAdapter dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // getting intent from siteloginsactivity
        final String id = getIntent().getStringExtra(SiteLoginsActivity.EXTRA_ID);

        // strings to hold data from database
        String sitenameData, usernameData, passwordData, notesData;

        // instantiating db
        dbHelper = new DatabaseAdapter(this);
        // getting readable database
        dbHelper.open();

        // Buttons
        Button updateLoginBtn, copyPasswordBtn, deleteLoginBtn;

        // assigning TextViews
        siteNameField = (EditText) findViewById(R.id.update_sitename);
        usernameField = (EditText) findViewById(R.id.update_username);
        passwordField =(EditText) findViewById(R.id.update_password);
        notesField = (EditText) findViewById(R.id.update_notes);

        // assigning buttons
        updateLoginBtn = (Button) findViewById(R.id.btnUpdate);
        copyPasswordBtn = (Button) findViewById(R.id.btnCopyPassword);
        deleteLoginBtn = (Button) findViewById(R.id.btnDeleteLogin);

        // string array to store retrieved data in
        String[] data = dbHelper.getLogin(id);
        //assigning string their relevant data from the array
        sitenameData = data[0];
        usernameData = data[1];
        passwordData = data[2];
        notesData = data[3];

        // set TextViews to display the returned data
        siteNameField.setText(sitenameData);
        usernameField.setText(usernameData);
        passwordField.setText(passwordData);
        notesField.setText(notesData);

        // update login data
        updateLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLogin(id);
            }
        });

        // copy password button. Copies data in password field to clipboard
        copyPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPasswordToClipboard();
            }
        });

        // delete login from database
        deleteLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLogin(id);
            }
        });
    }

    public void updateLogin(String id) {
        String sitename = siteNameField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String notes = notesField.getText().toString().trim();

        long row = dbHelper.updateData(id, sitename, username, password, notes);

        if (row < 0) {
            Toast.makeText(ViewActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ViewActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            goToMainPage();
        }
    }

    public void copyPasswordToClipboard() {

        String password = passwordField.getText().toString().trim();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Password", password);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(ViewActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

    }

    public void deleteLogin(String id) {
        dbHelper.deleteLogin(id);
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        goToMainPage();
    }

    public void goToMainPage() {
        Intent intent = new Intent(ViewActivity.this, SiteLoginsActivity.class);
        startActivity(intent);
    }
}
