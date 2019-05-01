package com.example.vishalsingh.categories_web;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

public class DepartmentActivity extends AppCompatActivity {
   private RecyclerView recyclerView;
   private DepartmentAdapter departmentAdapter;
   private ArrayList<Department> arrayList;
   private TextView network;
   private ProgressBar progressBar;
   private Button tryagain;
   private String json,ctid,cmpid;
   private DatabaseHelper myDb;
    boolean isOffline,iscreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        network = (TextView)findViewById(R.id.network);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        tryagain = (Button)findViewById(R.id.btn_try);
        myDb = new DatabaseHelper(DepartmentActivity.this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        actionBar.setDisplayShowHomeEnabled(true);

        iscreate=true;
        if (getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            ctid = getIntent().getStringExtra("ctid");
            cmpid= getIntent().getStringExtra("cmpid");
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewThree);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);
        network.setVisibility(View.GONE);
        tryagain.setVisibility(View.GONE);

        arrayList = new ArrayList<Department>();

        if ((isNetworkAvailable())){
            isOffline = false;
            onDataList();

        }else {
          //  Toast.makeText(DepartmentActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            isOffline = true;
            viewDepartmentData();

            progressBar.setVisibility(View.GONE);
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
                viewDepartmentData();
            }
        }
    }
   /* @Override
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
                viewDepartmentData();
            }
        }
    } */

   private void viewDepartmentData() {
        Cursor resthree = myDb.getDepartmentDataCTIDandCPID(ctid,cmpid);
        while (resthree.getCount() == 0) {

            network.setVisibility(View.VISIBLE);
            tryagain.setVisibility(View.VISIBLE);
            // Toast.makeText(MainActivity.this,"Data Not Found",Toast.LENGTH_LONG).show();
            return;
        }
        arrayList.clear();
        while (resthree.moveToNext()) {
            String categoryid = resthree.getString(resthree.getColumnIndex("category_id"));
            String department_id = resthree.getString(resthree.getColumnIndex("department_id"));
            String departmentName =resthree.getString(resthree.getColumnIndex("departmentName"));
            String company_id = resthree.getString(resthree.getColumnIndex("company_id"));
            String companyName = resthree.getString(resthree.getColumnIndex("companyName"));
            String title =resthree.getString(resthree.getColumnIndex("title"));
            String mobile = resthree.getString(resthree.getColumnIndex("mobile"));
            String landline = resthree.getString(resthree.getColumnIndex("land_line"));
            String colorCode = resthree.getString(resthree.getColumnIndex("colorCode"));
            String description = resthree.getString(resthree.getColumnIndex("description"));
            String openingTime = resthree.getString(resthree.getColumnIndex("openingTime"));
            String address1 = resthree.getString(resthree.getColumnIndex("address1"));
            String address2 = resthree.getString(resthree.getColumnIndex("address2"));
            String address3 = resthree.getString(resthree.getColumnIndex("address3"));
            String address4 = resthree.getString(resthree.getColumnIndex("address4"));
            String address5 = resthree.getString(resthree.getColumnIndex("address5"));
            String latitude =resthree.getString(resthree.getColumnIndex("latitude"));
            String longitude = resthree.getString(resthree.getColumnIndex("longitude"));
            String emailAddress = resthree.getString(resthree.getColumnIndex("emailAddress"));
            String companyLink = resthree.getString(resthree.getColumnIndex("companyLink"));

            Department department = new Department();

            department.setCategoryId(categoryid);
            department.setDepartmentId(department_id);
            department.setDepartmentName(departmentName);
            department.setCompanyId(company_id);
            department.setCompanyName(companyName);
            department.setTitle(title);
            department.setMobile(mobile);
            department.setLandLine(landline);
            department.setColorCode(colorCode);
            department.setDescription(description);
            department.setOpeningTime(openingTime);
            department.setAddress1(address1);
            department.setAddress2(address2);
            department.setAddress3(address3);
            department.setAddress4(address4);
            department.setAddress5(address5);
            department.setLatitude(latitude);
            department.setLongitude(longitude);
            department.setEmailAddress(emailAddress);
            department.setCompanyLink(companyLink);


            arrayList.add(department);
            departmentAdapter    = new DepartmentAdapter(DepartmentActivity.this, arrayList,isOffline);
            recyclerView.setAdapter(departmentAdapter);

        }

    }

    private void onDataList() {
        Dis dis = new Dis();
        dis.execute();
    }

private class Dis extends AsyncTask{

    @Override
    protected Object doInBackground(Object[] objects) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        if (getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            ctid = getIntent().getStringExtra("ctid");
            cmpid= getIntent().getStringExtra("cmpid");
        }

        try {
            url = new URL("http://kallapp.madword-media.co.uk/department.php?ctid=" + ctid + "&cmpid=" + cmpid);
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

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i=0; i < jsonArray.length();i++) {

                Department department = new Department();

                JSONObject object = jsonArray.getJSONObject(i);

                String categoryid = object.getString("category_id");
                String department_id = object.getString("department_id");
                String departmentName = object.getString("departmentName");
                String company_id = object.getString("company_id");
                String companyName = object.getString("companyName");
                String title = object.getString("title");
                String mobile = object.getString("mobile");
                String landline = object.getString("land-line");
                String colorCode = object.getString("colorCode");
                String description = object.getString("description");
                String openingTime = object.getString("openingTime");
                String address1 = object.getString("address1");
                String address2 = object.getString("address2");
                String address3 = object.getString("address3");
                String address4 = object.getString("address4");
                String address5 = object.getString("address5");
                String latitude = object.getString("latitude");
                String longitude = object.getString("longitude");
                String emailAddress = object.getString("emailAddress");
                String companyLink = object.getString("companyLink");


                department.setCategoryId(categoryid);
                department.setDepartmentId(department_id);
                department.setDepartmentName(departmentName);
                department.setCompanyId(company_id);
                department.setCompanyName(companyName);
                department.setTitle(title);
                department.setMobile(mobile);
                department.setLandLine(landline);
                department.setColorCode(colorCode);
                department.setDescription(description);
                department.setOpeningTime(openingTime);
                department.setAddress1(address1);
                department.setAddress2(address2);
                department.setAddress3(address3);
                department.setAddress4(address4);
                department.setAddress5(address5);
                department.setLatitude(latitude);
                department.setLongitude(longitude);
                department.setEmailAddress(emailAddress);
                department.setCompanyLink(companyLink);


                arrayList.add(department);
                boolean isInserted = myDb.insertDepartmentData(categoryid,department_id,departmentName,company_id,companyName,
                        title,mobile,landline,colorCode,description,openingTime,address1,address2,address3,
                        address4,address5,latitude,longitude,emailAddress,companyLink);
                departmentAdapter    = new DepartmentAdapter(DepartmentActivity.this, arrayList,isOffline);
                recyclerView.setAdapter(departmentAdapter);

            }
            progressBar.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(o);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this,CompanyActivity.class);
        intent.putExtra("id",ctid);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);

    }
}
