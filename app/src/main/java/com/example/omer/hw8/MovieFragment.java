package com.example.omer.hw8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private int mParam4;
    private double mParam5;

    private TextView title;
    private TextView description;
    private TextView year;
    private ImageView pictureView;
    private RatingBar ratingView;
    MovieDataJson threadMovieData;


    private ShareActionProvider mShareActionProvider;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieFragment newInstance(String name) {
        MovieFragment fragment = new MovieFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
//        args.putString(ARG_PARAM2, (String) movieItem.get("description"));
//        args.putString(ARG_PARAM3, (String) movieItem.get("year"));
//        args.putInt(ARG_PARAM4, (Integer) movieItem.get("image"));
//        Log.d("New Instance", args.getInt(ARG_PARAM4)+"");
//        args.putDouble(ARG_PARAM5, (double) movieItem.get("rating"));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getInt(ARG_PARAM4);
            mParam5 = getArguments().getDouble(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);


        title = (TextView) rootView.findViewById(R.id.title);
        description = (TextView) rootView.findViewById(R.id.description);
        year = (TextView) rootView.findViewById(R.id.year);
        pictureView = (ImageView) rootView.findViewById(R.id.pictureView);
        ratingView = (RatingBar) rootView.findViewById(R.id.ratingView);


        MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(title, description, year, pictureView, ratingView);
        String url = MovieDataJson.webAddr + "s/id/" + mParam1;
        downloadJson.execute(url);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.move_fragment_menu, menu);
//        MenuItem shareItem = menu.findItem(R.id.share);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//
//        Intent intentShare = new Intent(Intent.ACTION_SEND);
//        intentShare.setType("text/plain");
//
//        intentShare.putExtra(Intent.EXTRA_TEXT, mParam1);
//
//        mShareActionProvider.setShareIntent(intentShare);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private final class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, MovieDataJson> {

//        title = (TextView) rootView.findViewById(R.id.title);
//        description = (TextView) rootView.findViewById(R.id.description);
//        year = (TextView) rootView.findViewById(R.id.year);
//        pictureView = (ImageView) rootView.findViewById(R.id.pictureView);
//        ratingView = (RatingBar) rootView.findViewById(R.id.ratingView);


        //private final WeakReference<MyRecyclerViewAdapter> adapterReference;
        private final WeakReference<TextView> tRef;
        private final WeakReference<TextView> dRef;
        private final WeakReference<ImageView> pRef;
        private final WeakReference<RatingBar> rRef;
        private final WeakReference<TextView> yRef;

        public MyDownloadJsonAsyncTask(TextView titleAdapter, TextView descriptionAdapter, TextView yearAdapter, ImageView pictureAdapter, RatingBar ratingAdapter) {
            //adapterReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
            tRef = new WeakReference<TextView>(titleAdapter);
            dRef = new WeakReference<TextView>(descriptionAdapter);
            yRef = new WeakReference<TextView>(yearAdapter);
            pRef = new WeakReference<ImageView>(pictureAdapter);
            rRef = new WeakReference<RatingBar>(ratingAdapter);
        }

        @Override
        protected MovieDataJson doInBackground(String... urls) {
            threadMovieData = new MovieDataJson();
            for (String url : urls) {
                try {
                    threadMovieData.downloadMovieDataJson(urls[0]);
                    Log.d("Ping *****", ""+threadMovieData.movieList.get(0).get("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return threadMovieData;
        }

        @Override
        protected void onPostExecute(MovieDataJson threadMovie) {
           // movieData.moviesList.clear();
            Log.d("In post", "execute");
            //movieData.moviesList.clear();


            if (tRef != null) {
                //final MyRecyclerViewAdapter adapter = adapterReference.get();
                final TextView titleAdapter = tRef.get();
                if (titleAdapter != null) {
                    final TextView descriptionAdapter = dRef.get();
                    final TextView yearAdapter = yRef.get();
                    final ImageView pictureAdapter = pRef.get();
                    final RatingBar ratingAdapter = rRef.get();
                    descriptionAdapter.setText((String)threadMovie.movieList.get(0).get("description"));
                    yearAdapter.setText((String)threadMovie.movieList.get(0).get("year"));
                    titleAdapter.setText((String)threadMovie.movieList.get(0).get("name"));
                    double progress = Double.parseDouble((((Double) threadMovie.movieList.get(0).get("rating"))+""));
                    ratingAdapter.setProgress((int) progress);

                    MyDownloadImageAsyncTask task = new MyDownloadImageAsyncTask(pictureAdapter);
                    task.execute((String) threadMovie.movieList.get(0).get("url"));


                    Log.d("Notify", "data has changed");
                } else { Log.d("Inner Adapter Ref", "null"); }
            }
        }
    }


    private class MyDownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        public MyDownloadImageAsyncTask(ImageView imv) {
            imageViewReference = new WeakReference<ImageView>(imv);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            bitmap = MyUtility.downloadImageusingHTTPGetRequest(params[0]);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //Log.d("Download", 1+"");
            if (imageViewReference != null && bitmap != null) {
                //Log.d("Download", 2+"");
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    //Log.d("Download", 3+"");
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


}
