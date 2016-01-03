package cook.cuu.feathernotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        Intent intent = getIntent();
        int position = intent.getIntExtra("noteId", -1);

        EditText editText = (EditText) findViewById(R.id.editText);

        if(position!= -1)
            editText.setText(MainActivity.data[position]);
        else
            editText.setText("");
    }
}
