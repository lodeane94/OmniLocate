package com.omnivision.dao;

import android.content.Context;
import android.content.ContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by lkelly on 3/16/2017.
 */

public class JSONHelper<T> extends ContextWrapper{
    private String json = null;
    public JSONHelper(Context base,String fileName) {
        super(base);
        try{
            InputStream is = getAssets().open(fileName);//using input stream to open file
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public JSONArray getJSONArray(String jsonHeader) throws JSONException {
        JSONObject JsonObj = new JSONObject(json);
        JSONArray jsonArray = JsonObj.getJSONArray(jsonHeader);
        return jsonArray;
    }

  /*  public T assignJSONToObject(T object){
        ArrayList<T> jList = new ArrayList<T>();
    }*/
}
