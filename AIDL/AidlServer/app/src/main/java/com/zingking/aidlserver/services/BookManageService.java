package com.zingking.aidlserver.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.zingking.aidlserver.IBookManger;
import com.zingking.aidlserver.bean.Book;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/2/21
 * description：
 */
public class BookManageService extends Service {
    private List<Book> bookList = new ArrayList<>();
    private IBinder iBinder = new IBookManger.Stub() {

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public List<Book> getBook() throws RemoteException {
            return bookList;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


}
