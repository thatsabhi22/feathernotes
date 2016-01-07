package cook.cuu.feathernotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //static String[] data = {"Jan","Feb","Maraaaaa aaaaa aaaaa aaaa aaaaa aaaaa aaaaa aaaaa aaa aaaaaa","April","May","June","July","Aug","Sept","Oct"};
    static ArrayList<String> data ;
    ListView listView;
    static SQLiteDatabase notesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            data = new ArrayList<>();
            notesDB = this.openOrCreateDatabase("featherNotesDB", MODE_PRIVATE, null);
            notesDB.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT, noteText VARCHAR, " +
                    "star INTEGER DEFAULT 0,timestamp DATE DEFAULT (datetime('now','localtime')));");

            Cursor c = notesDB.rawQuery("SELECT id,noteText,star from notes ORDER BY timestamp DESC", null);

            int noteIdIndex = c.getColumnIndex("id");
            int noteIndex = c.getColumnIndex("noteText");
            int starIndex = c.getColumnIndex("star");

            if(c.getCount() != 0 && c!=null){
                c.moveToFirst();
                do{
                    Log.i("dabo","noteId ->" + Integer.toString(c.getInt(noteIdIndex)));
                    Log.i("dabo","note ->" + c.getString(noteIndex));
                    Log.i("dabo","star ->" + Integer.toString(c.getInt(starIndex)));
                    data.add(c.getString(noteIndex));
                }while(c.moveToNext());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                 callEditNote(-1);
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.single_row,R.id.noteTextView,data);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);
        arrayAdapter.setNotifyOnChange(true);
    }

    public void callEditNote(int position){
        Intent intent = new Intent(this,EditNotes.class);
        intent.putExtra("noteId", position);
        startActivity(intent);
    }

    public void onToggleStar(View view){
        if(view.getId() == R.id.favorite)
            Log.i("star", "This is star toggle");

        Toast.makeText(this,"You're a star !!",Toast.LENGTH_LONG).show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        callEditNote(position);
    }
}
