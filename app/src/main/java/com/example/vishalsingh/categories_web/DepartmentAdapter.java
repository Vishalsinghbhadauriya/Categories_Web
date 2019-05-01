package com.example.vishalsingh.categories_web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vishalsingh.categories_web.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.CustomViewHolderThree> {

    private List<Department> dataList;
    private Context context;
    DatabaseHelper myDb;
    private boolean isOffline;
    public DepartmentAdapter(Context context, ArrayList<Department> dataList,boolean isOffline) {
        this.context = context;
        this.dataList = dataList;
        this.isOffline = isOffline;
    }
    class CustomViewHolderThree extends RecyclerView.ViewHolder {
        TextView categoryid,department_id,departmentName,company_id,companyName,title,mobile;
        TextView landline,colorCode,description,openingTime,address1,address2,address3,address4,address5;
        TextView latitude,longitude,emailAddress,companyLink;
        ImageView delete,edit;
        public CustomViewHolderThree(@NonNull View itemView) {
            super(itemView);
            categoryid=(TextView)itemView.findViewById(R.id.categoryid);
            department_id=(TextView)itemView.findViewById(R.id.department_id);
            departmentName=(TextView)itemView.findViewById(R.id.departmentName);
            company_id =(TextView)itemView.findViewById(R.id.company_id);
            companyName =(TextView)itemView.findViewById(R.id.companyName);
            title=(TextView)itemView.findViewById(R.id.title);
            mobile =(TextView)itemView.findViewById(R.id.mobile);
            landline =(TextView)itemView.findViewById(R.id.landline);
            colorCode=(TextView)itemView.findViewById(R.id.colorCode);
            description=(TextView)itemView.findViewById(R.id.description);
            openingTime=(TextView)itemView.findViewById(R.id.openingTime);
            address1=(TextView)itemView.findViewById(R.id.address1);
            address2=(TextView)itemView.findViewById(R.id.address2);
            address3 =(TextView)itemView.findViewById(R.id.address3);
            address4  =(TextView)itemView.findViewById(R.id.address4);
            address5 =(TextView)itemView.findViewById(R.id.address5);
            latitude  =(TextView)itemView.findViewById(R.id.latitude);
            longitude =(TextView)itemView.findViewById(R.id.longitude);
            emailAddress =(TextView)itemView.findViewById(R.id.emailAddress);
            companyLink =(TextView)itemView.findViewById(R.id.companyLink);
            myDb = new DatabaseHelper(context);
            delete = (ImageView)itemView.findViewById(R.id.delete);
            edit = (ImageView)itemView.findViewById(R.id.edit);


        }
    }
    @NonNull
    @Override
    public CustomViewHolderThree onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.departmentadapter, parent, false);
        return new DepartmentAdapter.CustomViewHolderThree(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderThree customViewHolderThree, final int i) {
        if (isOffline){
            customViewHolderThree.delete.setVisibility(View.VISIBLE);
            customViewHolderThree.edit.setVisibility(View.VISIBLE);
        }else {
            customViewHolderThree.delete.setVisibility(View.GONE);
            customViewHolderThree.edit.setVisibility(View.GONE);
        }
        customViewHolderThree.categoryid.setText(dataList.get(i).getCategoryId());
        customViewHolderThree.department_id.setText(dataList.get(i).getDepartmentId());
        customViewHolderThree.departmentName.setText(dataList.get(i).getDepartmentName());
        customViewHolderThree.company_id.setText(dataList.get(i).getCompanyId());
        customViewHolderThree.companyName.setText(dataList.get(i).getCompanyName());
        customViewHolderThree.title.setText(dataList.get(i).getTitle());
        customViewHolderThree.mobile.setText(dataList.get(i).getMobile());
        customViewHolderThree.landline.setText(dataList.get(i).getLandLine());
        customViewHolderThree.colorCode.setText(dataList.get(i).getColorCode());
        customViewHolderThree.description.setText(dataList.get(i).getDescription());
        customViewHolderThree.openingTime.setText(dataList.get(i).getOpeningTime());
        customViewHolderThree.address1.setText(dataList.get(i).getAddress1());
        customViewHolderThree.address2.setText(dataList.get(i).getAddress2());
        customViewHolderThree.address3.setText(dataList.get(i).getAddress3());
        customViewHolderThree.address4.setText(dataList.get(i).getAddress4());
        customViewHolderThree.address5.setText(dataList.get(i).getAddress5());
        customViewHolderThree.latitude.setText(dataList.get(i).getLatitude());
        customViewHolderThree.longitude.setText(dataList.get(i).getLongitude());
        customViewHolderThree.emailAddress.setText(dataList.get(i).getEmailAddress());
        customViewHolderThree.companyLink.setText(dataList.get(i).getCompanyLink());

        customViewHolderThree.delete.setOnClickListener(new
                                                                View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        Integer deleteRows = myDb.deleteDepartmentData(dataList.get(i).getCompanyId());
                                                                        dataList.remove(i);
                                                                        notifyDataSetChanged();
                                                                    }
                                                                });
      customViewHolderThree.edit.setOnClickListener(new
                                        View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Intent intent;
                                                intent = new Intent(context,Update_Data.class);
                                                intent.putExtra("id", dataList.get(i).getCategoryId());
                                                intent.putExtra("cmid", dataList.get(i).getCompanyId());
                                                intent.putExtra("dpid",dataList.get(i).getDepartmentId());
                                                intent.putExtra("name", dataList.get(i).getDepartmentName());
                                                intent.putExtra("tbname","DepartmentTable");
                                                ((Activity)context).startActivityForResult(intent,1);

                                            }
                                        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
