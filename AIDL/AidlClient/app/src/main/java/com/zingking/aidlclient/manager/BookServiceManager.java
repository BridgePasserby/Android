package com.zingking.aidlclient.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.zingking.aidlclient.App;
import com.zingking.aidlserver.IBookManger;
import com.zingking.aidlserver.bean.Book;

import java.util.List;

/**
 * Copyright (c) 2019, Z.kai All rights reserved.
 * author：Z.kai
 * date：2019/2/21
 * description：远程服务管理类，封装服务连接
 */
public class BookServiceManager {
    private IBookManger iBookManger;
    private boolean binded;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManger = IBookManger.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookManger = null;
        }
    };

    private BookServiceManager() {

    }

    public static BookServiceManager getInstance() {
        return Holder.BOOK_SERVICE_MANAGER;
    }

    public void init(Context context) {
        connectRemoteService();
    }

    private void connectRemoteService() {
        if (binded) {
            try {
                App.getAppContext().unbindService(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("com.zingking.aidlserver.BOOK_SERVICE");
        intent.setPackage("com.zingking.aidlserver");
        binded = App.getAppContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        App.getAppContext().unbindService(connection);
    }

    public void addBook(Book book) {
        try {
            iBookManger.addBook(book);
        } catch (RemoteException e) {
            e.printStackTrace();
            connectRemoteService();
        }
    }

    public List<Book> getBookList() {
        List<Book> book = null;
        try {
            book = iBookManger.getBook();
        } catch (RemoteException e) {
            e.printStackTrace();
            connectRemoteService();
        }
        return book;

    }

    static class Holder {
        static final BookServiceManager BOOK_SERVICE_MANAGER = new BookServiceManager();
    }


}
