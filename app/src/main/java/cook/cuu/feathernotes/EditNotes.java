package cook.cuu.feathernotes;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditNotes extends AppCompatActivity {


    EditText editText;
    DbHelper dbHelper;
    int position;
    String current      =   null;
    boolean inserted    =   false;
    boolean edited      =   false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_notes);

        Intent intent   =   getIntent();
        position        =   intent.getIntExtra("noteId", -1);
        editText        =   (EditText) findViewById(R.id.editText);
        editText.setTypeface(SplashScreen.typeface);
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editText, 0);
            }
        },200);

        if(position!= -1) {
            editText.setText(MainActivity.data.get(position));
            current = (MainActivity.data.get(position));
        }
        else{
            editText.setText("");
            current     =   "";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Life", "onStop Called");
        decideEditInsert();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Life", "onPause Called");
        decideEditInsert();
    }

    private void decideEditInsert(){
        if(position == -1 && !inserted)
            insertNote();
        else if(!TextUtils.equals(current,editText.getText()) && !edited && !inserted)
            editNote();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Life", "onRestart Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Life", "onResume Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("back", "OnBackPressed Called");
        switchToHome();
    }

    public void switchToHome(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switchToHome();
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.saveNote:
                decideEditInsert();
                switchToHome();
                break;
        }
        return true;
    }


    private void insertNote() {
        try {
            if (TextUtils.isEmpty(editText.getText())) {
                Toast.makeText(this, "No Text Typed", Toast.LENGTH_LONG).show();
                Log.i("Life", "No Text Typed");
            } else {

                dbHelper = new DbHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                SQLiteStatement stmt = db.compileStatement("INSERT INTO notes (noteText,star) values (?,0);");
                stmt.bindString(1, String.valueOf(editText.getText()));
                stmt.execute();

                inserted = true;
                Toast.makeText(this, "Note Added ..", Toast.LENGTH_SHORT).show();
                Log.i("Life","Note Inserted ..");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void editNote() {

        try {
            if (TextUtils.isEmpty(editText.getText())) {
                Toast.makeText(this, "No Text Typed", Toast.LENGTH_LONG).show();
                Log.i("Life", "No Text Typed");
            } else {

                dbHelper = new DbHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                SQLiteStatement stmt = db.compileStatement("UPDATE notes SET noteText = ?, timestamp =  datetime('now','localtime') where id = ?;");
                stmt.bindString(1, String.valueOf(editText.getText()));
                stmt.bindString(2, String.valueOf(MainActivity.noteIdList.get(position)));
                stmt.execute();

                edited = true;
                Toast.makeText(this, "Note Edited ..", Toast.LENGTH_SHORT).show();
                Log.i("Life","Note Edited ..");

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_notes, menu);
        return true;
    }
}
