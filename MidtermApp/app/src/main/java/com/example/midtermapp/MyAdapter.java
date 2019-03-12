package com.example.midtermapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    ArrayList<DataObj> mData;

    public MyAdapter(ArrayList<DataObj> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inflate_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        DataObj dataObj = mData.get(i);

        //Todo : Set details of items to card Views
        viewHolder.textView1.setText("Track :"+dataObj.track);
        viewHolder.textView2.setText("Album :"+dataObj.album);
        viewHolder.textView3.setText("Artist :"+dataObj.artist);
        viewHolder.textView4.setText("Date :"+new SimpleDateFormat("MM-dd-yyyy").format(dataObj.date));
        viewHolder.dataObj = dataObj;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView1, textView2, textView3, textView4;
        DataObj dataObj;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.ifTextView1);
            textView2 = itemView.findViewById(R.id.ifTextView2);
            textView3 = itemView.findViewById(R.id.ifTextView3);
            textView4 = itemView.findViewById(R.id.ifTextView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context= itemView.getContext();
                    String url = dataObj.trackURL;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }


}
