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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vishalsingh.categories_web.Database.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    private Context context;
    private ArrayList<Categories> dataList;
    private boolean isOffline;
    DatabaseHelper myDb;
    CardView cardView;

    public CategoriesAdapter(Context context, ArrayList<Categories> dataList, boolean isOffline) {

        this.context = context;
        this.dataList = dataList;
        this.isOffline = isOffline;
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        TextView txtid, txtname;
        ImageView image_icon, delete, edit;


        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = (TextView) itemView.findViewById(R.id.id);
            txtname = (TextView) itemView.findViewById(R.id.name);
            image_icon = (ImageView) itemView.findViewById(R.id.image_icon);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);
            myDb = new DatabaseHelper(context);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.categoriesadapter, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder categoriesViewHolder, final int i) {

        Categories categories = new Categories();
        categories = dataList.get(i);
        if (isOffline) {
            categoriesViewHolder.delete.setVisibility(View.VISIBLE);
            categoriesViewHolder.edit.setVisibility(View.VISIBLE);


        } else {
            categoriesViewHolder.delete.setVisibility(View.GONE);
            categoriesViewHolder.edit.setVisibility(View.GONE);
        }
        categoriesViewHolder.txtid.setText(categories.getId());
        categoriesViewHolder.txtname.setText(categories.getName());

        Picasso.with(context).load(categories.getIcon()).into(categoriesViewHolder.image_icon);
        categoriesViewHolder.delete.setOnClickListener(new
                                                               View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                        myDb.deleteData(dataList.get(i).getId());
                                                                       dataList.remove(i);

                                                                       notifyDataSetChanged();
                                                                   }
                                                               });
        categoriesViewHolder.edit.setOnClickListener(new
                                                             View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     final Intent intent;
                                                                     intent = new Intent(context, Update_Data.class);
                                                                     intent.putExtra("id", dataList.get(i).getId());
                                                                     intent.putExtra("name", dataList.get(i).getName());
                                                                     intent.putExtra("tbname", "CategoriesTable");
                                                                     ((Activity)context).startActivityForResult(intent,1);

                                                                 }
                                                             });
        cardView.setOnClickListener(new
                                            View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final Intent intent;
                                                    intent = new Intent(context, CompanyActivity.class);
                                                    intent.putExtra("id", dataList.get(i).getId());
                                                    context.startActivity(intent);
                                                }
                                            });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
