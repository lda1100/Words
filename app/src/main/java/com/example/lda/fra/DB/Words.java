package com.example.lda.fra.DB;

import android.provider.BaseColumns;

public class Words {
    public Words(String string, String line){
    }
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME="words";
        public static final String COLUMN_NAME_WORD = "word";
        public static final String COLUMN_NAME_MEANING = "meaning";
    }
}
