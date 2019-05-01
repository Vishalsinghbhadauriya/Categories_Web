package com.example.vishalsingh.categories_web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vishalsingh.categories_web.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CustomViewHolderTwo>{

    private List<Company> dataList;
    private Context context;
    private boolean isOffline;
    DatabaseHelper myDb;

    public
    CompanyAdapter(Context context, ArrayList<Company> dataList,boolean isOffline){
        this.context = context;
        this.dataList = dataList;
        this.isOffline = isOffline;
    }
    class CustomViewHolderTwo extends RecyclerView.ViewHolder {
        TextView categoryid,company_name,company_id;
        ImageView delete,edit;
        CardView cardView ;

        CustomViewHolderTwo(View itemView) {
            super(itemView);
            categoryid=(TextView)itemView.findViewById(R.id.categoryid);
            company_name=(TextView)itemView.findViewById(R.id.company_name);
            company_id=(TextView)itemView.findViewById(R.id.company_id);
            delete = (ImageView)itemView.findViewById(R.id.delete);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            edit = (ImageView)itemView.findViewById(R.id.edit);
            myDb = new DatabaseHelper(context);

        }
    }
    @NonNull
    @Override
    public CustomViewHolderTwo onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.companyadapter, parent, false);
        return new CompanyAdapter.CustomViewHolderTwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderTwo customViewHolderTwo, final int i) {
        if (isOffline){
            customViewHolderTwo.delete.setVisibility(View.VISIBLE);
            customViewHolderTwo.edit.setVisibility(View.VISIBLE);
        }else {
            customViewHolderTwo.delete.setVisibility(View.GONE);
            customViewHolderTwo.edit.setVisibility(View.GONE);
        }
        customViewHolderTwo.categoryid.setText(dataList.get(i).getCategoryid());
        customViewHolderTwo.company_name.setText(dataList.get(i).getName());
        customViewHolderTwo.company_id.setText(dataList.get(i).getCompanyId());

        customViewHolderTwo.delete.setOnClickListener(new
                                                              View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      Integer deleteRows = myDb.deletecomapnyData(dataList.get(i).companyId);
                                                                      dataList.remove(i);
                                                                      notifyDataSetChanged();
                                                                  }
                                                              });
        customViewHolderTwo.cardView.setOnClickListener(new
                                                                View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        final Intent intent;
                                                                        intent = new Intent(context, DepartmentActivity.class);
                                                                        intent.putExtra("ctid", dataList.get(i).categoryid);
                                                                        intent.putExtra("cmpid", dataList.get(i).companyId);
                                                                        context.startActivity(intent);

                                                                    }
                                                                });
        customViewHolderTwo.edit.setOnClickListener(new
                                                             View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     final Intent intent;
                                                                     intent = new Intent(context,Update_Data.class);
                                                                     intent.putExtra("cmid", dataList.get(i).companyId);
                                                                     intent.putExtra("name", dataList.get(i).name);
                                                                     intent.putExtra("id", dataList.get(i).categoryid);
                                                                     intent.putExtra("tbname","CompanyTable");
                                                                     ((Activity)context).startActivityForResult(intent,1);
                                                                    // ((CompanyActivity)context).finish();
                                                                 }
                                                             });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }




}