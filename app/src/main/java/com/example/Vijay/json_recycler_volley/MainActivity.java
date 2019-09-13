package com.example.Vijay.json_recycler_volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String URLstring = "https://demonuts.com/Demonuts/JsonTest/Tennis/json_parsing.php";
    private static ProgressDialog mProgressDialog;
    ArrayList<DataModel> dataModelArrayList;
    private RvAdapter rvAdapter;
    private RecyclerView recyclerView;
    public static Context context;
    public static AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        mActivity = this;

        recyclerView = findViewById(R.id.recycler);

//        fetchingJSON();

        Map<String, String> params = new HashMap<String, String>();

        params.put("email_id", "");
        showSimpleProgressDialog(MainActivity.this, "Loading...","Fetching Json",false);

        VolleyCall volleyCall = new VolleyCall();
        volleyCall.sendRequest(context, URLstring, mActivity, null, "getData", params,"");
    }

    private void fetchingJSON() {

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {
                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                dataModelArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    DataModel playerModel = new DataModel();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    playerModel.setName(dataobj.getString("name"));
                                    playerModel.setCountry(dataobj.getString("country"));
                                    playerModel.setCity(dataobj.getString("city"));
                                    playerModel.setImgURL(dataobj.getString("imgURL"));

                                    dataModelArrayList.add(playerModel);
                                }
                                setupRecycler();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);

    }

    public void DataResponse(JSONObject obj){

        try {
            removeSimpleProgressDialog();

            if(obj.optString("status").equals("true")){

                dataModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    DataModel playerModel = new DataModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    playerModel.setName(dataobj.getString("name"));
                    playerModel.setCountry(dataobj.getString("country"));
                    playerModel.setCity(dataobj.getString("city"));
                    playerModel.setImgURL(dataobj.getString("imgURL"));

                    dataModelArrayList.add(playerModel);
                }
                setupRecycler();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupRecycler(){

        rvAdapter = new RvAdapter(this,dataModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
