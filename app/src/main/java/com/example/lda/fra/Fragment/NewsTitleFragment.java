package com.example.lda.fra.Fragment;



import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lda.fra.DB.WordsDBHelper;
import com.example.lda.fra.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;
    public WordsDBHelper mDbHelper;
    private SwipeRefreshLayout t;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag,container,false);
        final RecyclerView newsTitleRecyclerView = (RecyclerView) view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);
        t=(SwipeRefreshLayout) view.findViewById(R.id.swfh);
        t.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsAdapter a=new NewsAdapter(getNews());
                newsTitleRecyclerView.setAdapter(a);
                t.setRefreshing(false);
            }
        });
        return view;
    }

    private List<Words> getNews() {
        List<Words> wordsList = new ArrayList<>();
        mDbHelper = new WordsDBHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.query(
                com.example.lda.fra.DB.Words.Word.TABLE_NAME,
                new String [] {com.example.lda.fra.DB.Words.Word.COLUMN_NAME_WORD, com.example.lda.fra.DB.Words.Word.COLUMN_NAME_MEANING},
                null,
                null,
                null,
                null,
                null
        );
        if(c.moveToFirst())
        {
            do{
                String word =c.getString(c.getColumnIndex("word"));
                String meaning =c.getString(c.getColumnIndex("meaning"));
                Words words = new Words();
            words.setTitle(word );
            words.setContent(meaning);
            wordsList.add(words);
            }while (c.moveToNext());
        }
        c.close();
        while(c.moveToNext())
        {
            Words words = new Words();
            words.setTitle(getString(c.getColumnIndex(com.example.lda.fra.DB.Words.Word.COLUMN_NAME_WORD)));
            words.setContent(getString(c.getColumnIndex(com.example.lda.fra.DB.Words.Word.COLUMN_NAME_MEANING)));
            wordsList.add(words);
        }
        return wordsList;
    }




    private String getRandomLengthContent(String content) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i =0 ; i<length ;i++){
            builder.append(content);
        }
        return builder.toString();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout) !=null){
            isTwoPane = true;
        }else{
            isTwoPane = false;
        }
    }
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<Words> mWordsList;


        class ViewHolder extends RecyclerView.ViewHolder{
            View vs;
            TextView newsTitleText;
            public ViewHolder(View view){
                super(view);
                vs=view;
                newsTitleText = (TextView) view.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<Words> wordsList) {
            mWordsList = wordsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            holder.vs.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Words words = mWordsList.get(holder.getAdapterPosition());
                    showNormalDialog(v.getContext(),words.getTitle());
                   // Toast.makeText(v.getContext(),"delete",Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Words words = mWordsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        NewsContentFragment newsContentFragment =
                                (NewsContentFragment) getFragmentManager()
                                .findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(words.getTitle(),
                        words.getContent());
                    }else{
                        NewsContentActivity.actionStart(getActivity(),
                        words.getTitle(), words.getContent()
                        );
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Words words = mWordsList.get(position);
            holder.newsTitleText.setText(words.getTitle());
        }

        @Override
        public int getItemCount() {
            return mWordsList.size();
        }
    }
    private   void   showNormalDialog(final Context ct, final String title){
        final   AlertDialog.Builder normalDialog = new   AlertDialog.Builder(ct);
        normalDialog.setTitle(  "是否删除"  );
//        normalDialog.setMessage(  "你要点击哪一个按钮呢?"  );
        normalDialog.setPositiveButton(  "确定"  ,
                new   DialogInterface.OnClickListener() {
                    @Override
                    public   void   onClick(DialogInterface dialog,   int   which) {

                        String sql="delete from words where word='"+title+"'";
                        SQLiteDatabase db = mDbHelper.getReadableDatabase();
                        db.execSQL(sql);


                    }
                });
        normalDialog.setNegativeButton(  "取消"  ,
                new   DialogInterface.OnClickListener() {
                    @Override
                    public   void   onClick(DialogInterface dialog,   int   which) {
                    }
                });
        normalDialog.show();
    }
}
