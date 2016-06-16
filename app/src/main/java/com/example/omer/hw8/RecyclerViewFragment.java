package com.example.omer.hw8;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {} interface
 * to handle interaction events.
 * Use the {@link RecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private MovieData movieData;
    private OnFragmentInteractionListener mListener; //= (OnFragmentInteractionListener) getContext();
    private MyRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;

    private TextView title;
    private TextView description;
    private TextView year;
    private ImageView pictureView;
    private RatingBar ratingView;

    private Button clearAll;
    private Button selectAll;
    private Button delete;
    MovieDataJson threadMovieData;


    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment RecyclerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView;



        movieData = new MovieData();
        movieData.moviesList.clear();
//        movieData.moviesList = threadMovieData.movieList; // Insert movies here

        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        // Inflate the layout for this fragment

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardlist);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), movieData.getMoviesList());

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
        String url = MovieDataJson.webAddr;
        downloadJson.execute(url);


        mRecyclerViewAdapter.SetOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Movie Name", movieData.getItem(position).get("name") + "");
                HashMap movie = movieData.getItem(position);
                mListener.onFragmentInteraction((String)movie.get("id"));
            }

            @Override
            public void onItemLongClick(View view, int position) {
              //  getActivity().startActionMode(new ActionBarCallBack(position, movieData, mRecyclerViewAdapter));

            }

            @Override
            public void onOverflowMenuClick(View view, int position) {
                PopupMenu popup = new PopupMenu(getActivity(), view);
                final int p = position;
                Log.d("Menu Opened", "Now");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.delete:
                                //movieData.delete(p);
//                                threadMovieData.addItem(p, movieData.getItem(p));
                                Log.d("Clicked", "Delete");
                                try {
                                    threadMovieData.deleteItem(p, movieData.getItem(p));
                                    movieData.delete(p);
                                    mRecyclerViewAdapter.notifyItemRemoved(p);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                return true;
                            case R.id.duplicate:
                                //movieData.add(p);
                                try {
                                    threadMovieData.addItem(p, movieData.getItem(p));
                                    movieData.add(p);
                                    mRecyclerViewAdapter.notifyItemInserted(p+1);
//                                    MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
//                                    String url = MovieDataJson.webAddr;
//                                    downloadJson.execute(url);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //mRecyclerViewAdapter.notifyItemInserted(p);
                                return true;
                        }
                        return true;
                    }
                });

                MenuInflater mInflater = popup.getMenuInflater();
                mInflater.inflate(R.menu.card_menu, popup.getMenu());
                popup.show();
            }
        });




        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search != null) {
            Log.d("Search", "is not null");
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    MyDownloadJsonAsyncTask downloadJson = new MyDownloadJsonAsyncTask(mRecyclerViewAdapter);
                    String url = MovieDataJson.webAddr + "s/rating/"+ query;
                    downloadJson.execute(url);
                    Log.d("website", url);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String movie);
    }



    private final class MyDownloadJsonAsyncTask extends AsyncTask<String, Void, MovieDataJson> {
        private final WeakReference<MyRecyclerViewAdapter> adapterReference;

        public MyDownloadJsonAsyncTask(MyRecyclerViewAdapter adapter) {
            adapterReference = new WeakReference<MyRecyclerViewAdapter>(adapter);
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

            Log.d("In post", "execute");
            movieData.moviesList.clear();
            for (int i = 0; i < threadMovieData.movieList.size(); i++) {

                movieData.moviesList.add(threadMovieData.movieList.get(i));
            }

            if (adapterReference != null) {
                final MyRecyclerViewAdapter adapter = adapterReference.get();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    Log.d("Notify", "data has changed");
                } else { Log.d("Inner Adapter Ref", "null"); }
            }
        }
    }







}
