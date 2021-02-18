package com.ajitmaurya.basicsetup;

import android.content.Context;

import io.paperdb.Paper;

public class PaperDB {

    public static void init(Context context){
        Paper.init(context);
    }

    public static <T> T read(String repoName){
       return Paper.book().read(repoName);
    }


    public static void write(Object value,String repoName){
        Paper.book().write(repoName, value);
    }

    public static void delete(String repoName){
        Paper.book().delete(repoName);
    }

    public static void destroys(){
        Paper.book().destroy();
    }
}
