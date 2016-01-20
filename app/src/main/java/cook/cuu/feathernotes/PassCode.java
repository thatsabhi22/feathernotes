package cook.cuu.feathernotes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class PassCode extends AppCompatActivity {

    //action = 1 -> Forgot PassCode
    //action = 2 -> Change PassCode

    int action = 0;
    String hintAnswer,hintQuestion;
    TextView passCodeHead;
    EditText changePassCodeET,cfmChangePassCodeET,currentPassCodeET,securityQuestionET,securityAnswerET;

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

        if(action == 0){
            passCodeHead.setText("Forgot Passcode");
            changePassCodeET.setVisibility(View.GONE);
            cfmChangePassCodeET.setVisibility(View.GONE);
            currentPassCodeET.setVisibility(View.GONE);
            securityQuestionET.setNextFocusDownId(R.id.hintAnswer);
            securityAnswerET.setNextFocusDownId(R.id.changePassCodeButton);


            Cursor c = MainActivity.notesDB.rawQuery("SELECT id,hintQuestion,hintAnswer from users;", null);
            int count = c.getCount();
//            c.close();
            if (count > 0) {
//              int userIdIndex         = c.getColumnIndex("id");
                int hintQuestionIndex   = c.getColumnIndex("hintQuestion");
                int hintAnswerIndex     = c.getColumnIndex("hintAnswer");

                c.moveToFirst();
                c.getString(hintQuestionIndex);
                hintQuestion    =   c.getString(hintQuestionIndex);
                hintAnswer      =   c.getString(hintAnswerIndex);

            }
//            securityQuestion    =   String.valueOf(securityQuestionET.getText());
//            securityAnswer      =   String.valueOf(securityAnswerET.getText());
            securityQuestionET.setText(hintQuestion);
        }
        else if(action == 1){
            passCodeHead.setText("Change Passcode");
            securityQuestionET.setVisibility(View.GONE);
            securityAnswerET.setVisibility(View.GONE);
        }


    }

    public void changePassCode(View view){

        String currentPassCode,changePassCode,cfmChangePassCode,securityQuestion,securityAnswer;

        if(action == 0){
        }
        else if(action == 1){
            currentPassCode     =   String.valueOf(currentPassCodeET.getText());
            changePassCode      =   String.valueOf(changePassCodeET.getText());
            cfmChangePassCode   =   String.valueOf(cfmChangePassCodeET.getText());
        }




    }
}
