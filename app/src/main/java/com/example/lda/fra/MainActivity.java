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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lda.fra.DB.Words;
import com.example.lda.fra.DB.WordsDBHelper;
import com.example.lda.fra.Fragment.NewsTitleFragment;
import com.example.lda.fra.Word.WordActivity;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper mDbHelper;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final LayoutInflater f = LayoutInflater.from(MainActivity.this);
        final View f1 = f.inflate(R.layout.insert,null);
        final Intent intent = new Intent();

        switch (item.getItemId()){
            case R.id.but1:
                intent.setClass(MainActivity.this,WordActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setView(f1);
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String w;
                        String word = null;
                        String meaning = null;

                        EditText wow = (EditText)f1.findViewById(R.id.txtWord);
                        w = wow.getText().toString();


                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        String sql="select * from words where word = ?";
                        Cursor c=db.rawQuery(sql,new String[]{w});
                        if(c.moveToFirst())
                        {
                            do{
                                word =c.getString(c.getColumnIndex("word"));
                                meaning =c.getString(c.getColumnIndex("meaning"));

                            }while (c.moveToNext());
                        }

                        c.close();
                        //结果放在text view




                        Intent intent2 = new Intent();
                        intent2.putExtra("word",word);
                        intent2.putExtra("meaning",meaning);
                        intent2.setClass(MainActivity.this,t1Activity.class);
                        startActivity(intent2);

                    }
                });
                builder1.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialogInterfance,int i){
                    }
                })
                        .create()
                        .show();
                break;
            default:


        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new WordsDBHelper(MainActivity.this);
    }

    protected void onDestroy(){
        super.onDestroy();
        mDbHelper.close();
    }


}
