package com.theleafapps.pro.feathernotes.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.theleafapps.pro.feathernotes.ui.MainActivity;
import com.theleafapps.pro.feathernotes.utils.DbHelper;

import java.util.ArrayList;

import com.theleafapps.pro.feathernotes.R;

/**
 * Created by aviator on 22/01/16.
 */
public class TweakedArrayAdapter extends ArrayAdapter<String> implements View.OnClickListener {

    DbHelper dbHelper;
    ImageView imageView;
    TextView textView;
    Context context = getContext();
    ArrayList<String> values;
    ArrayList<Integer> stars;
    int position;

    public TweakedArrayAdapter(Context context,ArrayList<String> values, ArrayList<Integer> stars){
        super(context,-1,values);
        this.values  =  values;
        this.stars   =  stars;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_row, parent, false);
        textView = (TextView) rowView.findViewById(R.id.noteTextView);
        imageView = (ImageView) rowView.findViewById(R.id.favorite);
        imageView.setOnClickListener(this);

        textView.setText(values.get(position));
        // change the icon for Windows and iPhone
        String s = values.get(position);

        if (stars.get(position)== 1) {
            imageView.setImageResource(R.drawable.solid_red_star);
        } else {
            imageView.setImageResource(R.drawable.hollow_star_30_30);
        }

        return rowView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite:

                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);
                View child = listView.getChildAt(position);
                ImageView img = (ImageView) child.findViewById(R.id.favorite);
//                Toast.makeText(getContext()," position ->" + position,Toast.LENGTH_LONG).show();
                if(MainActivity.starList.get(position) == 1){
                    img.setImageResource(R.drawable.hollow_star_30_30);
                    MainActivity.starList.set(position, 0);
                    updateStar(MainActivity.noteIdList.get(position),0);
                }
                else{
                    img.setImageResource(R.drawable.solid_red_star);
                    MainActivity.starList.set(position, 1);
                    updateStar(MainActivity.noteIdList.get(position), 1);
                }
//                Toast.makeText(getContext(),"Done star",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    public void updateStar(int id,int set) {
        dbHelper = new DbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("UPDATE notes SET star = ? where id = ?;");
        stmt.bindString(1, String.valueOf(set));
        stmt.bindString(2, String.valueOf(id));
        stmt.execute();
    }
}
