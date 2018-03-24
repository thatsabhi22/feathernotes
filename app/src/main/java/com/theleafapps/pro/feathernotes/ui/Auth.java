package com.theleafapps.pro.feathernotes.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theleafapps.pro.feathernotes.R;
import com.theleafapps.pro.feathernotes.dialogs.MessageDialog;
import com.theleafapps.pro.feathernotes.dialogs.PassCodeActionListDialog;
import com.theleafapps.pro.feathernotes.dialogs.SecQListDialog;
import com.theleafapps.pro.feathernotes.utils.Commons;
import com.theleafapps.pro.feathernotes.utils.DbHelper;

public class Auth extends AppCompatActivity {

    public static EditText hintQuestion;
    EditText passCodeBox, hintAnswer;
    TextView displayTextView, aboutButton, creditsButton;
    ;
    DbHelper dbHelper;
    ImageView passCodeHelp;
    boolean firstTimeFlag = false;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        try {
            passCodeHelp = (ImageView) findViewById(R.id.passCodeHelp);
            passCodeBox = (EditText) findViewById(R.id.passCode);
            hintQuestion = (EditText) findViewById(R.id.hintQuestion);
            hintAnswer = (EditText) findViewById(R.id.hintAnswer);
            displayTextView = (TextView) findViewById(R.id.displayText);
            imageButton = (ImageButton) findViewById(R.id.suggest);
            aboutButton = (TextView) findViewById(R.id.about_button);
            creditsButton = (TextView) findViewById(R.id.credits_button);

            passCodeBox.requestFocus(View.LAYOUT_DIRECTION_LTR);

            Intent intent = getIntent();
            String code = intent.getStringExtra("Msg");

            aboutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Auth.this, About.class);
                    startActivity(intent);
                }
            });

            creditsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Auth.this, Credits.class);
                    startActivity(intent);
                }
            });

            if (TextUtils.isEmpty(code)) {

                displayTextView.setTypeface(SplashScreen.typeface);
                displayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));

                hintQuestion.setTypeface(SplashScreen.typeface);
                hintAnswer.setTypeface(SplashScreen.typeface);

                MainActivity.notesDB = this.openOrCreateDatabase("featherNotesDB", MODE_PRIVATE, null);
                MainActivity.notesDB.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, passCode INTEGER, hintQuestion VARCHAR,hintAnswer VARCHAR);");

                if (checkIfFirstTime()) {
//                    Toast.makeText(this,"First Time User ....",Toast.LENGTH_LONG).show();
                    displayTextView.setText("You're First time using this app. Create Your own Passcode");
                    passCodeBox.setNextFocusDownId(R.id.hintQuestion);
                    hintQuestion.setNextFocusDownId(R.id.hintAnswer);
                    passCodeHelp.setVisibility(View.INVISIBLE);
                    firstTimeFlag = true;
                } else {
//                    Toast.makeText(this,"Returning User ....",Toast.LENGTH_LONG).show();
                    returningUserVisibility();
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("Msg", "Your PassCode id : " + code);
                FragmentManager fm = getFragmentManager();
                MessageDialog messageDialog = new MessageDialog();
                messageDialog.setArguments(bundle);
                messageDialog.show(fm, "messageDialog");
                returningUserVisibility();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void returningUserVisibility() {
        displayTextView.setText("Enter Passcode");
        hintQuestion.setVisibility(View.GONE);
        hintAnswer.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);
    }

    public boolean checkIfFirstTime() {
        Cursor c = MainActivity.notesDB.rawQuery("SELECT id,passCode from users;", null);
        int count = c.getCount();
        c.close();
        if (count > 0) {
            return false;
        }
        return true;
    }

    public boolean checkValidUser(String passCode) {
        Cursor c = MainActivity.notesDB.rawQuery("SELECT id,passCode from users where passCode = ?;", new String[]{passCode});
        int count = c.getCount();
        c.close();
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            passCodeBox.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(passCodeBox, 0);
                }
            }, 200);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void authenticate(View view) {
        if (firstTimeFlag) {
            // For first time entry to app
            if (passCodeBox.length() == 4) {
                if (!TextUtils.isEmpty(hintQuestion.getText())) {
                    if (!TextUtils.isEmpty(hintAnswer.getText())) {
                        savePassCode(String.valueOf(passCodeBox.getText()), String.valueOf(hintQuestion.getText()), String.valueOf(hintAnswer.getText()));
                        Toast.makeText(this, "Your Passcode has been created.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        displayTextView.setTextColor(Color.RED);
                        displayTextView.setText("Please Enter Your Security Answer");
                    }
                } else {
                    displayTextView.setTextColor(Color.RED);
                    displayTextView.setText("Please Enter Your Security Question or Click on suggest.");
                }
            } else {
                displayTextView.setTextColor(Color.RED);
                displayTextView.setText("Please Enter 4 Digit Passcode.");
            }
        } else {
            if (passCodeBox.length() == 4) {
                if (checkValidUser(String.valueOf(passCodeBox.getText()))) {
//                    Toast.makeText(this,"Entry Valid",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    passCodeBox.setText("");
                    displayTextView.setTextColor(Color.RED);
                    displayTextView.setText("Please Enter Valid Passcode.");
                }
                //For entry as a returning user to the app
            } else {
                displayTextView.setTextColor(Color.RED);
                displayTextView.setText("Please Enter 4 Digit Passcode.");
            }
        }
    }

    public void displayHintQuestionDialog(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        SecQListDialog ldc = new SecQListDialog();
        ldc.show(fragmentManager, "securityQuestionDialog");
    }

    public void openPassCodeHelpDialog(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        PassCodeActionListDialog passCodeActionListDialog = new PassCodeActionListDialog();
        passCodeActionListDialog.show(fragmentManager, "passCodeHelpDialog");
    }

    private void savePassCode(String passCode, String hintQuestion, String hintAnswer) {

        try {

            dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("INSERT INTO users (passCode,hintQuestion,hintAnswer) values (?,?,?);");
            stmt.bindString(1, passCode);
            stmt.bindString(2, hintQuestion);
            stmt.bindString(3, hintAnswer);
            stmt.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        boolean handleReturn = super.dispatchTouchEvent(ev);

        View view = getCurrentFocus();

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (view instanceof EditText) {
            EditText innerView = (EditText) getCurrentFocus();

            if (ev.getAction() == MotionEvent.ACTION_UP &&
                    !Commons.getLocationOnScreen(innerView).contains(x, y)) {

                InputMethodManager input = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }

        return handleReturn;
    }
}
