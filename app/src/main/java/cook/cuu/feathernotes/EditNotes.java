package cook.cuu.feathernotes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class EditNotes extends AppCompatActivity {

    EditText editText;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        Intent intent = getIntent();
        int position = intent.getIntExtra("noteId", -1);

        editText= (EditText) findViewById(R.id.editText);

        if(position!= -1)
            editText.setText(MainActivity.data.get(position));
        else
            editText.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Life", "onStop Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Life", "onPause Called");
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

//        try {
            Log.i("Life", "onDestroy Called");
            if (TextUtils.isEmpty(editText.getText())) {
                Toast.makeText(this, "No Text Typed", Toast.LENGTH_LONG).show();
                Log.i("Life", "No Text Typed");
            } else {

                dbHelper = new DbHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                SQLiteStatement stmt = db.compileStatement("INSERT INTO notes (noteText,star) values (?,0);");
                stmt.bindString(1, String.valueOf(editText.getText()));
                stmt.execute();

                Toast.makeText(this, "Note Inserted ..", Toast.LENGTH_SHORT).show();
                Log.i("Life","Note Inserted ..");
            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
