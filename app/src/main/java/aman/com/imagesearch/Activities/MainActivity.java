package aman.com.imagesearch.Activities;

import android.app.ActivityOptions;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.List;

import aman.com.imagesearch.Adapter.RecyclerViewAdapter;
import aman.com.imagesearch.Database.Database;
import aman.com.imagesearch.Model.ImageItem;
import aman.com.imagesearch.R;
import aman.com.imagesearch.Utils.UrlManager;
import aman.com.imagesearch.widget.SquareImageView;


/**
 * Created by Aman on 2/8/18.
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnImageClickListener {

    private final static String TAG = "MainActivity";
     final static String IMAGEITEMLIST ="ImageItem";
    final static String POSTION ="ListItemPosition";
    final static String SEARCHQUERY="SearchQuery";

    /*<------Widgets------>*/
    Toolbar toolbar;
    EditText et_search;
    RecyclerView recyclerView;
    ProgressBar progressBar;


    RecyclerViewAdapter adapter;
    GridLayoutManager layoutManager;


    private int COLUMN_COUNT = 2;
    private ArrayList<ImageItem> mList;


    /* variables for pagination*/


    private boolean isScrolling = false;
    private int pastVisibleItem, visibleItemCount, totalItemCount, previousTotal = 0;
    private int totalPageCount = 2, currentPageCount = 1;


    /*Database*/
    Database mDatabase;


    private String searchQuery = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get the url with search query and page no.
        et_search = (EditText) toolbar.findViewById(R.id.search);

        //do search action from keypad
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }


                return false;
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //setup recyclerview
        initRecyclerView();

        //init and set the adapter
        mList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(this, mList, this);
        recyclerView.setAdapter(adapter);

        //setup database
        mDatabase = Room.databaseBuilder(this, Database.class, "database")
                .allowMainThreadQueries()
                .build();





    }

    private void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


         /*
        set up scrolling behaviour of recyclerview for pagination
        */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();

                if (isScrolling && (visibleItemCount + pastVisibleItem) == totalItemCount && isNetworkConnected()) {
                    isScrolling = false;
                    loadImagesfromServer(searchQuery, ++currentPageCount);
                }

            }
        });
    }


   /*
   *this method will load the image from the inter using flickr api
   * clear the offline data of the search query
   * add the new the data to database
   * Volley is being used to get the data
   * */
    private void loadImagesfromServer(final String query, final int pageNo) {

        Log.d(TAG, "loding images: query=" + query + " pageno" + pageNo);




        String url = UrlManager.getInstance().getItemUrl(query, pageNo);

        //show the progress bar
        progressBar.setVisibility(View.VISIBLE);


        //fetch the data using volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.d(TAG, "response " + response);

                            //parse the jsonObject
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject photosJsonObject = jsonObject.getJSONObject("photos");



                            //get all the images jsonObject

                            JSONArray jsonArray = photosJsonObject.getJSONArray("photo");

                            //put into the list
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                ImageItem imageItem = gson.fromJson(json.toString(), ImageItem.class);
                                imageItem.setSearchKey(query);

                                Log.d(TAG, "parsing json" + imageItem.toString());
                                mList.add(imageItem);
                                mDatabase.myDao().addImageItem(imageItem);
                            }


                            //notify the adapter and load the images in the recyclerview
                            adapter.notifyDataSetChanged();

                            //hide the progress bar
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //on Error Occurred
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Loading data failed", Toast.LENGTH_SHORT).show();

                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
//
//        }else {
//            Toast.makeText(this, "No more post available", Toast.LENGTH_SHORT).show();
//        }
    }




    //perform action on image click
    @Override
    public void onImageClick(SquareImageView imageView, int position) {
        Log.d(TAG, "image clicked" + mList.get(position));

        Intent intent= new Intent(this,ViewImageActivity.class);
       // intent.putExtra(IMAGEITEMLIST,mList);

        intent.putExtra(POSTION,position);
        intent.putExtra(SEARCHQUERY,searchQuery);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                MainActivity.this,
                imageView,
                getString(R.string.transition_name));
        startActivity(intent, transitionActivityOptions.toBundle());

    }


    //inflate option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    //perfrom action on menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.menu_main_search:
                Log.d(TAG, "option selected: " + item.getTitle());
                search();
                break;

            case R.id.menu_main_col2:
                Log.d(TAG, "option selected: " + item.getTitle());
                setGridViewColumn(2);
                break;

            case R.id.menu_main_col3:
                Log.d(TAG, "option selected: " + item.getTitle());
                setGridViewColumn(3);
                break;

            case R.id.menu_main_col4:
                Log.d(TAG, "option selected: " + item.getTitle());
                setGridViewColumn(4);
                break;
        }

        return true;
    }


    /*
    * this method will get the data from edit text
    * check the internet avalability
    * if online it will load the images from online
    * if offline it will load the data from the database */


    private void search() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //request the focus on edit text if it is black
        if (et_search.getText().toString().equals("")) {
            et_search.requestFocus();
            imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);


        } else {

            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            //set page count to 1
            currentPageCount = 1;
            totalPageCount = 2;

            //clear list
            mList.clear();

            // search query method);
            searchQuery = et_search.getText().toString().toLowerCase();

            Log.d(TAG, "loading images for query=" + searchQuery);


            if (isNetworkConnected()) {

                //clear the data from the database for  query
                mDatabase.myDao().deleteImageItem(searchQuery);
                loadImagesfromServer(searchQuery, currentPageCount);
            } else {

                loadImagesOffline(searchQuery, currentPageCount);
            }
        }
    }


    /*
    * this method will be called in offline mode
    * it'll load the data from the SQLite database
    *
    * */
    private void loadImagesOffline(String searchQuery, int currentPageCount) {

        Log.d(TAG,"loading image offline for"+ searchQuery);

        Toast.makeText(this, "Searching Offline", Toast.LENGTH_SHORT).show();


        //get the list from database for the searched query
        List<ImageItem> list = mDatabase.myDao().getImageItem(searchQuery);

        if (list.size()==0){
            Toast.makeText(this, "No offline result found", Toast.LENGTH_SHORT).show();
        }

        mList.clear();
        mList.addAll(list);
        adapter.notifyDataSetChanged();


    }


    //change the gridview coumn
    private void setGridViewColumn(int noOfColumn) {

        Log.d(TAG, "set column " + noOfColumn);

        layoutManager.setSpanCount(noOfColumn);  // change the span count in grid view

        recyclerView.setLayoutManager(layoutManager); //set the layout manager again

        adapter.notifyDataSetChanged();  //notify the adapter

    }


    /*To check internet is available or not*/

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }

}
