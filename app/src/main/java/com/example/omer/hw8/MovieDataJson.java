package com.example.omer.hw8;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Omer on 3/21/16.
 */
public class MovieDataJson {

    static String webAddr = "http://omer.win/movie";
    List<Map<String, ?>> movieList;

    public MovieDataJson() {
        movieList = new ArrayList<Map<String,?>>();
    }


    public void addItem(int position, Map<String, ?> item) throws JSONException {
        final JSONObject json;
        if (item != null) {
            String jsonString = "{ \"id\" : \"" + item.get("id") + "_new" + "\", \"name\": \"" + item.get("name") + "\", \"description\" : \"" + item.get("description") + "\", \"stars\" : \"" + item.get("stars")
                    + "\", \"length\" : \"" + item.get("length") + "\", \"rating\" : \"" + ((Double) item.get("rating")) + "\", \"image\" : \"" + item.get("image") + "\", \"year\" : \"" + item.get("year") + "\", \"director\" : \"" + item.get("director") + "\", \"url\" : \""
                    + item.get("url") + "\"}";
            Log.d("String", jsonString);
            json = new JSONObject(jsonString);
            Log.d("ID JSON", json.get("id") + "");

            //JSONArray array = json.getJSONArray("start");
            Log.d("Parsing", "JSON parsed");
            //else json = null;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String url = "http://omer.win/add";
                    MyUtility.sendHttPostRequest(url, json);
                    Log.d("Parsing", "sent post request");
                }
            };
            new Thread(runnable).start();
            movieList.add(position+1, item);
        }
    }


    public void deleteItem(int position, Map<String, ?> item) throws JSONException {
        final JSONObject json;
        if (item != null) {
            String jsonString = "{ \"id\" : \"" + item.get("id") + "\" }";
            Log.d("String", jsonString);
            json = new JSONObject(jsonString);
            Log.d("ID JSON", json.get("id") + "");

            //JSONArray array = json.getJSONArray("start");
            Log.d("Parsing", "JSON parsed");
            //else json = null;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    String url = "http://omer.win/delete";
                    MyUtility.sendHttPostRequest(url, json);
                    Log.d("Parsing", "sent post request");
                }
            };
            new Thread(runnable).start();
            movieList.add(position+1, item);
        }
    }



    


    public void downloadMovieDataJson(String url) throws JSONException {
        String unparsed = MyUtility.downloadJSONusingHTTPGetRequest(url);
            String nUnparsed = "{start : " + unparsed + "}";
            JSONObject jObject = new JSONObject(nUnparsed);
            JSONArray array = jObject.getJSONArray("start");
            int n = array.length();
            List<String> id = new ArrayList<String>();
            List<String> name = new ArrayList<String>();
            List<String> description = new ArrayList<String>();
            List<String> stars = new ArrayList<String>();
            List<String> length = new ArrayList<String>();
            List<String> image = new ArrayList<String>();
            List<String> year = new ArrayList<String>();
            List<Double> rating = new ArrayList<Double>();
            List<String> director = new ArrayList<String>();
            List<String> urls = new ArrayList<String>();

            for (int i = 0; i < n; i++) {
                id.add(array.getJSONObject(i).getString("id"));
                name.add(array.getJSONObject(i).getString("name"));
                description.add(array.getJSONObject(i).getString("description"));
                stars.add(array.getJSONObject(i).getString("stars"));
                length.add(array.getJSONObject(i).getString("length"));
                //image.add((int) array.getJSONObject(i).getInt("image"));
                year.add(array.getJSONObject(i).getString("year"));
                rating.add(array.getJSONObject(i).getDouble("rating"));
                director.add(array.getJSONObject(i).getString("director"));
                urls.add(array.getJSONObject(i).getString("url"));
                //Log.d("Movie Download", "" + list.get(i));
            }

            for (int i = 0; i < id.size(); i++) {
                movieList.add(i, createMovie(id.get(i), name.get(i), description.get(i), year.get(i), length.get(i), rating.get(i), director.get(i), stars.get(i), urls.get(i)));
            }

    }

    private HashMap createMovie(String id, String name, String description, String year,
                                String length, double rating, String director, String stars, String url) {
        HashMap movie = new HashMap();
        //movie.put("image",image);
        movie.put("id", id);
        movie.put("name", name);
        movie.put("description", description);
        movie.put("year", year);
        movie.put("length",length);
        movie.put("rating",rating);
        movie.put("director",director);
        movie.put("stars",stars);
        movie.put("url",url);
        movie.put("selection",false);
        return movie;
    }
}
