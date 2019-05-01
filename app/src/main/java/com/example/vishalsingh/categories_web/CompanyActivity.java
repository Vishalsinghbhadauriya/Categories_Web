package com.example.vishalsingh.categories_web;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

public class CompanyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CompanyAdapter companyAdapter;
    ArrayList<Company> arrayList;
    String json;
    String id;
    TextView network;
    ProgressBar progressBar;
    Button tryagain;
    DatabaseHelper myDb;
    boolean isOffline,iscreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        network = (TextView)findViewById(R.id.network);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        tryagain = (Button)findViewById(R.id.btn_try);
        progressBar.setVisibility(View.VISIBLE);
        iscreate=true;
        if (getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            id = getIntent().getStringExtra("id");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewTwo);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myDb = new DatabaseHelper(CompanyActivity.this);
        network.setVisibility(View.GONE);
        tryagain.setVisibility(View.GONE);


        //  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

       // StrictMode.setThreadPolicy(policy);
        arrayList = new ArrayList<Company>();
        if ((isNetworkAvailable())){
            isOffline = false;
            onDataList();
        }else {
            isOffline = true;
            viewCompanyData();
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(CompanyActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            // network.setVisibility(View.VISIBLE);
             //tryagain.setVisibility(View.VISIBLE);

        }

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    isOffline = false;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {

            if ((isNetworkAvailable())) {
                isOffline = false;
                onDataList();
            } else {
                isOffline = true;

                viewCompanyData();
            }
        }
    }

    /*  @Override
        protected void onResume() {
            super.onResume();
            if (iscreate) {
                iscreate = false;
            } else {

                if ((isNetworkAvailable())) {
                    isOffline = false;
                    onDataList();
                } else {
                    isOffline = true;
                    viewCompanyData();
                }
            }
        }
    */
    private void viewCompanyData() {
        Cursor restwo = myDb.getCompanyDataId(id);
        while (restwo.getCount() == 0) {

            network.setVisibility(View.VISIBLE);
            tryagain.setVisibility(View.VISIBLE);
            // Toast.makeText(MainActivity.this,"Data Not Found",Toast.LENGTH_LONG).show();
            return;
        }
        arrayList.clear();
        while (restwo.moveToNext()) {
            String id = restwo.getString(restwo.getColumnIndex("categoryid"));
            String name = restwo.getString(restwo.getColumnIndex("name"));
            String company_id = restwo.getString(restwo.getColumnIndex("company_id"));
            Company company = new Company();
            company.setCategoryid(id);
            company.setName(name);
            company.setCompanyId(company_id);
            arrayList.add(company);
        }
        companyAdapter = new CompanyAdapter(CompanyActivity.this, arrayList,isOffline);
        recyclerView.setAdapter(companyAdapter);
      /*  recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CompanyActivity.this
                , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(MainActivity.this, arrayList.get(position).getId() + "",Toast.LENGTH_SHORT).show();
                final Intent intent;
                intent = new Intent(CompanyActivity.this, DepartmentActivity.class);
                intent.putExtra("ctid", arrayList.get(position).categoryid);
                intent.putExtra("cmpid", arrayList.get(position).companyId);
                startActivity(intent);
            }
        }));  */
    }

    private void onDataList() {
       Dis dis = new Dis();
        dis.execute();
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

    private class Dis extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            if (getIntent().getExtras()!=null) {
                Intent intent = getIntent();
                id = getIntent().getStringExtra("id");
            }
            try {
                url = new URL("http://kallapp.madword-media.co.uk/company.php?category_id=" + id);
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
            arrayList.clear();
            try {
                JSONObject jsonObject = new JSONObject(json);

                JSONArray jsonArray = jsonObject.getJSONArray("companies");
                for (int i=0; i < jsonArray.length();i++) {

                    Company company = new Company();

                    JSONObject object = jsonArray.getJSONObject(i);

                    String id = object.getString("categoryid");
                    String name = object.getString("name");
                    String company_id = object.getString("company_id");

                    company.setCategoryid(id);
                    company.setName(name);
                    company.setCompanyId(company_id);
                    arrayList.add(company);
                    boolean isInserted = myDb.insertCompanyData(id,name,company_id);

                    }

                companyAdapter    = new CompanyAdapter(CompanyActivity.this, arrayList,isOffline);
                recyclerView.setAdapter(companyAdapter);
            /*    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CompanyActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final Intent intent;
                                intent = new Intent(CompanyActivity.this,DepartmentActivity.class);
                                intent.putExtra("ctid",arrayList.get(position).categoryid);
                                intent.putExtra("cmpid",arrayList.get(position).companyId);
                                startActivity(intent);
                            }
                        })); */
                progressBar.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(o);

        }


    }

}
