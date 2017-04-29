package com.jf.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import com.jf.passwordmanager.R;

import java.util.Random;

import helper.DatabaseAdapter;

public class NewLoginActivity extends Activity  {

    private EditText siteNameField;
    private EditText userField;
    private EditText passField;
    private EditText notesField;
    private DatabaseAdapter dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        // Instantiating Views
        siteNameField = (EditText) findViewById(R.id.enterSiteName);
        userField = (EditText) findViewById(R.id.enterUsername);
        passField = (EditText) findViewById(R.id.enterPassword);
        notesField = (EditText) findViewById(R.id.enterNotes);

        // SQLite database handler
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();

        // random password generator
        Button passwordBtn = (Button) findViewById(R.id.btnPassword);
        passwordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Generating random password and putting it into the password field.
                genRandPass();
            }
        });

        // save info to db
        Button saveBtn = (Button) findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                String siteName = siteNameField.getText().toString().trim();
                String username = userField.getText().toString().trim();
                String password = passField.getText().toString().trim();
                String notes = notesField.getText().toString().trim();

                long id = dbHelper.insertData(siteName, username, password, notes);
                if (id < 0) {
                    Toast.makeText(NewLoginActivity.this, "Unsuccessful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(NewLoginActivity.this, "Added login", Toast.LENGTH_LONG).show();
                }

                // go to main page
                Intent intent = new Intent(NewLoginActivity.this, SiteLoginsActivity.class);
                startActivity(intent);
            }
        });

    }

    /***
     * Checks if an EditText view is empty
     * @param et EditText object to check
     * @return true if empty. false otherwise
     */
    public boolean isEmpty(EditText et) {
        if (et.getText().toString().trim().matches(""))
            return true;
        else
            return false;
    }

    /**
     * Generates a random string of 12 characters in length
     */
    public void genRandPass() {
        EditText passField = (EditText) findViewById(R.id.enterPassword);
        String generatedPass = "";
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int i = 0; // Iterator for while loop
        int charsLen = charSet.length();

        while (i < 11) {
            i++;
            Random rand = new Random();
            int randPos = rand.nextInt(charsLen); // A random position in the charset
            generatedPass += charSet.charAt(randPos);
        }

        passField.setText(generatedPass);
    }

}
