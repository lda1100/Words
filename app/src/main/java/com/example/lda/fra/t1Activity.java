package com.example.lda.fra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lda.fra.Word.WordActivity;

public class t1Activity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final LayoutInflater f = LayoutInflater.from(t1Activity.this);
        final View f1 = f.inflate(R.layout.insert,null);
        final Intent intent = new Intent();

        switch (item.getItemId()){
            case R.id.but8:
                intent.setClass(t1Activity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.third,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t1);
        Intent intent = getIntent();
        TextView wo = (TextView)findViewById(R.id.t1);
        TextView me = (TextView)findViewById(R.id.t2);

        wo.setText(intent.getStringExtra("word"));
        me.setText(intent.getStringExtra("meaning"));
    }
}
