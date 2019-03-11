package com.zingking.aidlclient.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zingking.aidlclient.R;
import com.zingking.aidlserver.bean.Book;

import java.util.List;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/3/8
 * description：
 */
public class RvBookAdapter extends RecyclerView.Adapter<RvBookAdapter.BookViewHolder> {
    private static final String TAG = "RvBookAdapter";
    private List<Book> bookList;
    private View.OnClickListener listener;

    public RvBookAdapter(List<Book> data) {
        this.bookList = data;
    }

    public void update(List<Book> list){
        bookList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_book, parent,false);
        return new BookViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvName.setText(book.getName());
        String text = book.getId() + "";
        holder.tvId.setText(text);
        String price = book.getPrice() + "";
        holder.tvPrice.setText(price);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvId;
        TextView tvPrice;

        public BookViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvId = itemView.findViewById(R.id.tv_id);
            tvPrice = itemView.findViewById(R.id.tv_price);

        }
    }
}
