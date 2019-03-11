package com.zingking.aidlserver.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/2/21
 * description：
 */
public class Book implements Parcelable {
    private long id;
    private String name;
    private int price;
    private boolean isReadFinish;

    public Book() {
    }

    public Book(Parcel in) {
        id = in.readLong();
        name = in.readString();
        price = in.readInt();
        isReadFinish = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isReadFinish() {
        return isReadFinish;
    }

    public void setReadFinish(boolean readFinish) {
        isReadFinish = readFinish;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeByte((byte) (isReadFinish ? 1 : 0));
    }
}
