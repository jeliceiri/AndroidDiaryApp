package com.jilleliceiri.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
/**
 * PasswordActivity
 *
 * This class uses Shared Preferences to store a password when created. If a password has been
 * created and matches the user entered password when submitted, then it displays the main activity
 * of the application via the use of an intent.
 *
 *
 * @author Jill Eliceiri
 */
public class PasswordActivity extends AppCompatActivity {

    //instance variables
    EditText enteredPasswordTextView;
    TextView passwordResult;
    Boolean isPasswordSet = false;
    String enteredPassword;
    String sharedPrefRetrievedPassword;
    SharedPreferences myPrefs;
    Context context = this;

    /**
     * This method begins the program. It gets references to the views and checks whether the
     * password is set.
     *
     * @param savedInstanceState the saved instance state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        enteredPasswordTextView = (EditText) findViewById(R.id.passwordText);
        passwordResult = (TextView) findViewById(R.id.passwordResult);

        //retrieve password from shared preferences and use "EMPTY" as a default
        myPrefs = getSharedPreferences("diaryPswd", Activity.MODE_PRIVATE);
        sharedPrefRetrievedPassword = myPrefs.getString("password", "EMPTY");

        //check if password is set
        if (!sharedPrefRetrievedPassword.equals("EMPTY")) {
            isPasswordSet = true;
        }
    }

    /**
     * This method is called when the submit password button is selected. It gets the user entered
     * password and checks if equal to the shared preferences password. If they are the same,
     * then the main application activity begins via use of an intent. If the wrong password is
     * entered, an appropriate toast message displays. If a password does not exist, an appropriate
     * message diplays.
     *
     * @param view the view
     */

    public void submitPassword(View view) {

        if (isPasswordSet) {
            enteredPassword = enteredPasswordTextView.getText().toString();
            if (enteredPassword.equals(sharedPrefRetrievedPassword)) {
                //go!
                Intent createNewEntryIntent = new Intent(PasswordActivity.this,
                        MainActivity.class);
                startActivity(createNewEntryIntent);
            }
            //wrong password entered
            else {
                Toast.makeText(context, "Wrong password. Try again. ", Toast.LENGTH_LONG).show();
                enteredPasswordTextView.setText("");
            }
        }
        //password has not been created
        else {
            Toast.makeText(context, "First, create a password", Toast.LENGTH_LONG).show();
            enteredPasswordTextView.setText("");
        }
    }

    /**
     * This method is called when the create password button is selected. It gets the user entered
     * password and uses a Shared Preferences container to save data. An editor object is used to
     * make preference changes. Once the password has been saved, then a toast message displays to
     * the user, telling them to login.
     *
     * @param view the view
     */

    public void createPassword(View view) {

        enteredPassword = enteredPasswordTextView.getText().toString();
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("password", enteredPassword);
        editor.commit();
        isPasswordSet = true;
        sharedPrefRetrievedPassword = enteredPassword;
        enteredPassword = "";
        enteredPasswordTextView.setText("");
        Toast.makeText(context, "Password created. Now login.", Toast.LENGTH_LONG).show();
    }
}
