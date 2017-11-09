package com.example.lda.fra.Word;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.UserDictionary;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.lda.fra.DB.Words;
import com.example.lda.fra.DB.WordsDBHelper;
import com.example.lda.fra.MainActivity;
import com.example.lda.fra.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordActivity extends AppCompatActivity {
    EditText edit ;
    Button button ;
    TextView responseText;
    private WordsDBHelper mDbHelper;
    String Word;
    String Meaning;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDbHelper = new WordsDBHelper(WordActivity.this);
        Intent intent = new Intent();
        final LayoutInflater f = LayoutInflater.from(WordActivity.this);
        final View f1 = f.inflate(R.layout.insert,null);

        switch (item.getItemId())

        {
            case R.id.but2:
                intent.setClass(WordActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                AlertDialog.Builder builder = new AlertDialog.Builder(WordActivity.this);
                builder.setMessage("是否添加单词"+" "+edit.getText()+" "+responseText.getText())
                .setTitle("添加单词");
                Word = ((EditText) findViewById(R.id.edit)).getText().toString();
                Meaning = ((TextView) findViewById(R.id.responseText)).getText().toString();
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("word",Word);
                        values.put("meaning",Meaning);
                        db.insert("words",null,values);
                        values.clear();
                    }
                });
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialogInterfance,int i){
                    }
                })
                        .create()
                        .show();
                break;
//            case R.id.search:
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(WordActivity.this);
//                builder1.setView(f1);
//                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String w;
//                        String word = null;
//                        String meaning = null;
//
//                        EditText wow = (EditText)findViewById(R.id.txtWord);
//                        w = wow.getText().toString();
//
//
//                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//                        String sql="select * from words where word = ?";
//                        Cursor c=db.rawQuery(sql,new String[]{w});
//                        if(c.moveToFirst())
//                        {
//                            do{
//                                word =c.getString(c.getColumnIndex("word"));
//                                meaning =c.getString(c.getColumnIndex("meaning"));
//
//                            }while (c.moveToNext());
//                        }
//
//                        c.close();
//                        //结果放在text view
//                        TextView wo = (TextView)findViewById(R.id.t1);
//                        TextView me = (TextView)findViewById(R.id.t2);
//                        wo.setText(word);
//                        me.setText(meaning);
//
//                    }
//                });
//                builder1.setNegativeButton("取消",new DialogInterface.OnClickListener() {
//                    public void onClick (DialogInterface dialogInterfance,int i){
//                    }
//                })
//                        .create()
//                        .show();
//                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second,menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        mDbHelper = new WordsDBHelper(WordActivity.this);
        edit = (EditText)findViewById(R.id.edit);
        button = (Button)findViewById(R.id.button);
        responseText = (TextView)findViewById(R.id.responseText);


        WebView browser=(WebView)findViewById(R.id.Toweb);
        browser.loadUrl("http://wap.chinadaily.com.cn/");
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {      view.loadUrl(url);
                return true;
            }
        });






            button.setOnClickListener(new View.OnClickListener()

        {
        @Override
        public void onClick(View view) {
            sendRequestWithOkHttp();
        }
    });
}

public boolean onKeyDown;


    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://fanyi.youdao.com/openapi.do?keyfrom=youdao111&key=60638690&type=data&doctype=xml&version=1.1&q="+edit.getText())
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseXMLWithPull(responseData);
                    showRespone(parseXMLWithPull(responseData));
//                    showRespone(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showRespone(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                responseText.setText(response);

            }
        });
    }

    private String parseXMLWithPull(String xmlData) {

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String paragraph = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:{
                        if("paragraph".equals(nodeName)){

                            paragraph= xmlPullParser.nextText();

                        }
                        break;

                    }
                    case XmlPullParser.END_TAG:{
                        if("translation".equals(nodeName)) {

                            return paragraph;

                        }
                        break;
                    }
                    default:
                        break;


                }

                eventType = xmlPullParser.next();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;


    }


}
