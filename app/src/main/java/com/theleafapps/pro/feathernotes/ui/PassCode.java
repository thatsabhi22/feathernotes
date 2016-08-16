package com.theleafapps.pro.feathernotes.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theleafapps.pro.feathernotes.utils.DbHelper;

import com.theleafapps.pro.feathernotes.R;

public class PassCode extends AppCompatActivity {

    //action = 1 -> Forgot PassCode
    //action = 2 -> Change PassCode

    int action = 0;
    DbHelper dbHelper;
    String hintAnswer,hintQuestion,passCode;
    TextView passCodeHead;
    EditText changePassCodeET,cfmChangePassCodeET,currentPassCodeET,
            securityQuestionET,securityAnswerET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        Intent it           =   getIntent();
        action              =   it.getIntExtra("action", -1);
        passCodeHead        =   (TextView) findViewById(R.id.passCodeTextView);
        changePassCodeET    =   (EditText) findViewById(R.id.changePassCode);
        cfmChangePassCodeET =   (EditText) findViewById(R.id.confirmChangePassCode);
        currentPassCodeET   =   (EditText) findViewById(R.id.currentPassCode);
        securityQuestionET  =   (EditText) findViewById(R.id.securityQuestion);
        securityAnswerET    =   (EditText) findViewById(R.id.securityAnswer);

        passCodeHead.setTypeface(SplashScreen.typeface);
        changePassCodeET.setTypeface(SplashScreen.typeface);
        cfmChangePassCodeET.setTypeface(SplashScreen.typeface);
        currentPassCodeET.setTypeface(SplashScreen.typeface);
        securityAnswerET.setTypeface(SplashScreen.typeface);
        securityQuestionET.setTypeface(SplashScreen.typeface);
        getUser();

        if(action == 0){
            securityAnswerET.requestFocus();
            passCodeHead.setText("Forgot Passcode");
            changePassCodeET.setVisibility(View.GONE);
            cfmChangePassCodeET.setVisibility(View.GONE);
            currentPassCodeET.setVisibility(View.GONE);
            securityQuestionET.setNextFocusDownId(R.id.hintAnswer);
            securityAnswerET.setNextFocusDownId(R.id.changePassCodeButton);

        }
        else if(action == 1){
            currentPassCodeET.requestFocus();
            passCodeHead.setText("Change Passcode");
            securityQuestionET.setVisibility(View.GONE);
            securityAnswerET.setVisibility(View.GONE);

        }
    }

    private void getUser() {
        Cursor c = MainActivity.notesDB.rawQuery("SELECT id, passCode, hintQuestion, hintAnswer from users;", null);
        int count = c.getCount();
        if (count > 0) {
//          int userIdIndex         = c.getColumnIndex("id");
            int passCodeIndex       = c.getColumnIndex("passCode");
            int hintQuestionIndex   = c.getColumnIndex("hintQuestion");
            int hintAnswerIndex     = c.getColumnIndex("hintAnswer");

            c.moveToFirst();

            hintQuestion    =   c.getString(hintQuestionIndex);
            hintAnswer      =   c.getString(hintAnswerIndex);
            passCode        =   c.getString(passCodeIndex);

            c.close();
        }
        securityQuestionET.setText(hintQuestion);
    }

    public void changePassCode(View view){

        String currentPassCode,changePassCode,cfmChangePassCode,securityQuestion,securityAnswer;

        if(action == 0){
//            securityQuestion    =   String.valueOf(securityQuestionET.getText());
            securityAnswer      =   String.valueOf(securityAnswerET.getText());

            if(TextUtils.equals(securityAnswer,hintAnswer)){
                Intent intent = new Intent(this,Auth.class);
                intent.putExtra("Msg",passCode);
                startActivity(intent);
            }
            else{
                securityAnswerET.setError("Your Security answer is not correct");
            }
        }
        else if(action == 1){
            currentPassCode     =   String.valueOf(currentPassCodeET.getText());
            changePassCode      =   String.valueOf(changePassCodeET.getText());
            cfmChangePassCode   =   String.valueOf(cfmChangePassCodeET.getText());

            if(!TextUtils.isEmpty(currentPassCode) && currentPassCode.length() == 4){
                if(!TextUtils.isEmpty(changePassCode) && changePassCode.length() == 4){
                    if(!TextUtils.isEmpty(cfmChangePassCode) && cfmChangePassCode.length() == 4){
                        if(TextUtils.equals(currentPassCode,passCode)) {
                            if (TextUtils.equals(changePassCode, cfmChangePassCode)) {

                                Toast.makeText(this,"All set",Toast.LENGTH_LONG).show();
                                updatePassCode(currentPassCode, changePassCode);
                                Intent intent = new Intent(this,Auth.class);
                                startActivity(intent);

                            } else {
                                cfmChangePassCodeET.setError("New and Confirm Passcode Do not Match");
                            }
                        }
                        else{
                            currentPassCodeET.setError("The Current Passcode is not Correct");
                        }
                    }
                    else{
                        cfmChangePassCodeET.setError("Please Re-Enter a 4 digit New Passcode");
                    }
                }else{
                    changePassCodeET.setError("Please Enter a 4 digit New Passcode");
                }
            }
            else{
                currentPassCodeET.setError("Please Enter a 4 digit Current Passcode");
            }
        }
    }

    private void updatePassCode(String currentPassCode, String changePassCode) {
        try {
            dbHelper = new DbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            SQLiteStatement stmt = db.compileStatement("UPDATE users SET passCode = ? WHERE passCode = ?;");
            stmt.bindString(1, changePassCode);
            stmt.bindString(2, currentPassCode);
            stmt.execute();
            Toast.makeText(this,"Passcode Updated...",Toast.LENGTH_LONG).show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
