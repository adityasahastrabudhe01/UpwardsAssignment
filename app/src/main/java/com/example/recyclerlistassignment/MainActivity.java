package com.example.recyclerlistassignment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue mRequestQueue;
    private DividerItemDecoration mDividerItemDecoration;
    private List<MyData> myData;
    private String url = "https://www.datakick.org/api/items";
    private Button mNext;
    private JSONArray nextJSONArray;
    private static int counter = 0, JsonLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If the user is connected to the Internet
        if (isConnectedToNetwork()) {
            Log.d("Internet", "Yes");
            mNext = findViewById(R.id.buttonNext);
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);

            mRequestQueue = Volley.newRequestQueue(this);

            mLayoutManager = new LinearLayoutManager(this);
            ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager) mLayoutManager).getOrientation());
            mRecyclerView.addItemDecoration(mDividerItemDecoration);
            myData = new ArrayList<>();

            mAdapter = new MyAdapter(getApplicationContext(), myData);
            mRecyclerView.setAdapter(mAdapter);

            getData();

            mNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(counter < JsonLength) {
                        final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.show();
                        for (int i = counter + 1; i < nextJSONArray.length(); i++) {
                            try {
                                JSONObject mJsonObject = nextJSONArray.getJSONObject(i);
                                MyData myData1 = new MyData();
//                        myData1.setmName(mJsonObject.getString("name"));
//                        myData1.setmName(mJsonObject.optString("name"));

                                if (mJsonObject.has("name")) {
                                    myData1.setmName(mJsonObject.optString("name"));
                                } else {
                                    myData1.setmName("NA");
                                }
                                myData1.setmGtin14(mJsonObject.optString("gtin14"));

                                myData.add(myData1);
                            }
                            catch (Exception e) {
                                Log.d("Error", e.toString());
                                mProgressDialog.dismiss();
                            }
                            counter = i;
                        }
                        mAdapter.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Next 50 items are Added to the list. \nScroll Down to see.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "No item is remaining to Add into the List.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(this);
            mAlertDialogBuilder.setTitle("No Internet Connection");
            mAlertDialogBuilder.setMessage("Please make sure that you are connected to Internet");
            mAlertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            mAlertDialogBuilder.show();
        }
    }

    private void getData(){
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                nextJSONArray = response;
                JsonLength = response.length() - 1;
                for (int i = 0; i < 50; i++) {
                    try {
                        JSONObject mJsonObject = response.getJSONObject(i);
                        MyData myData1 = new MyData();
//                        myData1.setmName(mJsonObject.getString("name"));
//                        myData1.setmName(mJsonObject.optString("name"));
                        if (mJsonObject.has("name")){
                            myData1.setmName(mJsonObject.optString("name"));
                        }
                        else{
                            myData1.setmName("NA");
                        }
                        myData1.setmGtin14(mJsonObject.optString("gtin14"));

                        myData.add(myData1);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Error", e.toString());
                        mProgressDialog.dismiss();
                    }

                    counter = i;
                }
                mAdapter.notifyDataSetChanged();
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                mProgressDialog.dismiss();
            }
        });
        mRequestQueue.add(mJsonArrayRequest);
    }

    private boolean isConnectedToNetwork(){
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mActiveNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mActiveNetworkInfo != null && mActiveNetworkInfo.isConnected();
    }
}
