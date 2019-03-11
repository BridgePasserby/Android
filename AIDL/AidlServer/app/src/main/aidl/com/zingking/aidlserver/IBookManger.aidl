// IBookManger.aidl

/**
 *  接口 aidl 文件
 * */
package com.zingking.aidlserver;
import com.zingking.aidlserver.bean.Book;

// Declare any non-default types here with import statements

interface IBookManger {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     * void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

    void addBook(in Book book);

    List<Book> getBook();
}
