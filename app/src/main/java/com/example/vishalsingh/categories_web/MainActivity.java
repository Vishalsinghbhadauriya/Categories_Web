package com.example.vishalsingh.categories_web;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishalsingh.categories_web.Database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoriesAdapter categoriesAdapter;
    ArrayList<Categories> arrayList;
    String json;
    TextView network;
    ProgressBar progressBar;
    Button tryagain;
    DatabaseHelper myDb;
    URL url = null;
    HttpURLConnection httpURLConnection = null;
    InputStream inputStream = null;
    boolean isOffline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        network = (TextView)findViewById(R.id.network);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        tryagain = (Button)findViewById(R.id.btn_try);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myDb = new DatabaseHelper(this);
        network = (TextView)findViewById(R.id.network);
        network.setVisibility(View.GONE);
        tryagain.setVisibility(View.GONE);
        arrayList = new ArrayList<>();

        if ((isNetworkAvailable())){
            isOffline=false;
            onDataList();
            }else {
            isOffline=true;
            viewData();
            progressBar.setVisibility(View.GONE);

           // Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
        }

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    isOffline=false;
                    onDataList();
                    progressBar.setVisibility(View.VISIBLE);
                    network.setVisibility(View.GONE);
                    tryagain.setVisibility(View.GONE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    network.setVisibility(View.VISIBLE);
                    tryagain.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() { super.onResume(); }

    private void onDataList() {
        Dis dis = new Dis();
        dis.execute();
    }

    private void viewData(){
        Cursor res = myDb.getAllDAta();
        while (res.getCount() == 0) {

             network.setVisibility(View.VISIBLE);
             tryagain.setVisibility(View.VISIBLE);

           // Toast.makeText(MainActivity.this,"Data Not Found",Toast.LENGTH_LONG).show();
            return;
        }
         arrayList.clear();
        while (res.moveToNext()) {
            String  id = res.getString(res.getColumnIndex("id"));
            String name = res.getString(res.getColumnIndex("name"));
            String image_icon = res.getString(res.getColumnIndex("icon"));
            Categories catego = new Categories();
            catego.setId(id);
            catego.setName(name);
            catego.setIcon(image_icon);
            arrayList.add(catego);
             }
             initAdapter(arrayList,isOffline);
        }

    private void initAdapter(ArrayList<Categories> arrayList, boolean isOffline) {

        categoriesAdapter = new CategoriesAdapter(MainActivity.this,arrayList,isOffline);
        recyclerView.setAdapter(categoriesAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            if ((isNetworkAvailable())){
                isOffline=false;
                onDataList();
            }else {
                isOffline=true;
                viewData();
                // Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
            }
        return isAvailable;
      }

    private class Dis extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                url = new URL("http://kallapp.madword-media.co.uk/categories.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line+"\n");
                }
                inputStream.close();
                json = stringBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {

                arrayList.clear();
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonArray = jsonObject.getJSONArray("categories");
                for (int i=0; i < jsonArray.length();i++){

                    Categories catego = new Categories();

                    JSONObject object = jsonArray.getJSONObject(i);

                   String  id = object.getString("id");
                   String name = object.getString("name");
                   String image_icon = object.getString("icon");
                   catego.setId(id);
                   catego.setName(name);
                   catego.setIcon(image_icon);
                   arrayList.add(catego);
                    boolean isInserted = myDb.insertData(id,name,image_icon);
                }

                initAdapter(arrayList,isOffline);
//                categoriesAdapter = new CategoriesAdapter(MainActivity.this,arrayList,isOffline);
//                recyclerView.setAdapter(categoriesAdapter);
               /* recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this
                        , new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(MainActivity.this, arrayList.get(position).getId() + "",Toast.LENGTH_SHORT).show();
                        final Intent intent;
                        intent = new Intent(MainActivity.this,CompanyActivity.class);
                        intent.putExtra("id",arrayList.get(position).getId());
                        startActivity(intent);
                    }
                }));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            // progressDoalog.dismiss();
            super.onPostExecute(o);
        }
    }
}
