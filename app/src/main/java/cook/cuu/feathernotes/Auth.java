package cook.cuu.feathernotes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Auth extends AppCompatActivity {

    EditText passCodeBox;
    TextView displayTextView;
    boolean firstTimeFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_auth);

            passCodeBox     =   (EditText) findViewById(R.id.passCode);
            displayTextView =   (TextView) findViewById(R.id.displayText);
            displayTextView.setTypeface(SplashScreen.typeface);

            MainActivity.notesDB = this.openOrCreateDatabase("featherNotesDB", MODE_PRIVATE, null);
            MainActivity.notesDB.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, passCode INTEGER);");

            if(checkIfFirstTime()){
                Toast.makeText(this,"First Time User ....",Toast.LENGTH_LONG).show();
                displayTextView.setText("Create Passcode");


                firstTimeFlag = true;
            }
            else{
                Toast.makeText(this,"Returning User ....",Toast.LENGTH_LONG).show();
                displayTextView.setText("Enter Passcode");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public boolean checkIfFirstTime(){
        Cursor c    =   MainActivity.notesDB.rawQuery("SELECT id,passCode from users;", null);
        int count   =   c.getCount();
        c.close();
        if(count > 0){
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        passCodeBox.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(passCodeBox, 0);
            }
        }, 200);
    }

    public void authenticate(View view){

        if(firstTimeFlag){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else{

        }


    }
}
