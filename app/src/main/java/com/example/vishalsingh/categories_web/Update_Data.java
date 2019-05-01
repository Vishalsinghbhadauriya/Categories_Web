package com.example.vishalsingh.categories_web;

import android.app.ActionBar;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vishalsingh.categories_web.Database.DatabaseHelper;

public class Update_Data extends AppCompatActivity {
    private EditText name;
    private TextView titleId;
    private Button update;
    private String uid , uname ,tableName,id,did;
    DatabaseHelper myDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__data);
        titleId = (TextView)findViewById(R.id.titleText);
        name = (EditText)findViewById(R.id.upname);
        update = (Button)findViewById(R.id.Upadte);
        myDb = new DatabaseHelper(this);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        actionBar.setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras()!=null) {
            Intent intent = getIntent();
            uid = getIntent().getStringExtra("cmid");
            did = getIntent().getStringExtra("dpid");
            uname = getIntent().getStringExtra("name");
            tableName = getIntent().getStringExtra("tbname");
            id = getIntent().getStringExtra("id");
        }

        name.setText(uname);
        name.setSelection(name.getText().length());
        if (TextUtils.isEmpty(uid)){
            titleId.setText(id);
        } else if (TextUtils.isEmpty(did)){
            titleId.setText(uid);
        }else {
            titleId.setText(did);
        }

        update.setOnClickListener(new
                                          View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if (tableName.equals("CategoriesTable"))
                                                  {
                                                      if(name.getText().toString().isEmpty()) {
                                                          Toast.makeText(Update_Data.this,"Please Enter The Name",Toast.LENGTH_LONG).show();
                                                      } else {
                                                          myDb.upadateData(id,name.getText().toString());
                                                            setResult(1);
                                                            finish();
                                                      }

                                                  }else if (tableName.equals("CompanyTable"))
                                                  {
                                                      if(name.getText().toString().isEmpty()) {
                                                          Toast.makeText(Update_Data.this,"Please Enter The Name",Toast.LENGTH_LONG).show();
                                                      } else {
                                                          myDb.upadateCompanyData(uid,name.getText().toString());
                                                          setResult(1);
                                                          finish();
                                                      }
                                                  }else if (tableName.equals("DepartmentTable"))
                                                  {
                                                      if(name.getText().toString().isEmpty()) {
                                                          Toast.makeText(Update_Data.this,"Please Enter The Name",Toast.LENGTH_LONG).show();
                                                      } else {
                                                          myDb.upadateDepartmentData(did,name.getText().toString());
                                                          setResult(1);
                                                          finish();
                                                      }
                                                  }
                                                   }

                                          });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        if (tableName.equals("CategoriesTable")){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if (tableName.equals("CompanyTable")){
            Intent intent = new Intent(this,CompanyActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
            //setResult(1,intent);
            finish();
        }else if (tableName.equals("DepartmentTable"))
        {
            Intent intent = new Intent(this,DepartmentActivity.class);
            intent.putExtra("ctid",id);
            intent.putExtra("cmpid",uid);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
