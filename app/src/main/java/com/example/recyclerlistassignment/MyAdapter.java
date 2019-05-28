package com.example.recyclerlistassignment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<MyData> mList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTvName, mTvGtin14;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvName = itemView.findViewById(R.id.textViewName);
            mTvGtin14 = itemView.findViewById(R.id.textViewGtin14);
        }
    }

    public MyAdapter(Context mContext, List<MyData> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_item, viewGroup, false);
        return new MyAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder myViewHolder, int i) {
        MyData myData = mList.get(i);
        myViewHolder.mTvName.setText(myData.getmName());
        myViewHolder.mTvGtin14.setText(myData.getmGtin14());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
