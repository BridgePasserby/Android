package com.zingking.aidlclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.zingking.aidlclient.adapters.RvBookAdapter;
import com.zingking.aidlclient.manager.BookServiceManager;
import com.zingking.aidlserver.bean.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView rvBook;
    private Button btnAdd;
    private List<Book> bookList = new ArrayList<>();
    private RvBookAdapter rvBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BookServiceManager.getInstance().init(this);
//        bookList = BookServiceManager.getInstance().getBookList();
        initView();
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        rvBookAdapter = new RvBookAdapter(bookList);
        rvBook.setAdapter(rvBookAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBook.setLayoutManager(layoutManager);
        rvBook.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void setListener() {
        btnAdd.setOnClickListener(this);
        rvBookAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvId = v.findViewById(R.id.tv_id);
                TextView tvName = v.findViewById(R.id.tv_name);
                TextView tvPrice = v.findViewById(R.id.tv_price);
                String s = tvId.getText() + " - " + tvName.getText() + " - " + tvPrice.getText();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        rvBook = (RecyclerView) findViewById(R.id.rv_book);
        btnAdd = (Button) findViewById(R.id.btn_add);
    }

    @Override
    public void onClick(View v) {
        Random random = new Random(System.nanoTime());
        switch (v.getId()) {
            case R.id.btn_add:
                Book book = new Book();
                book.setId(System.currentTimeMillis());
                book.setName("战争与和平");
                book.setPrice(random.nextInt(10) * 10);
                book.setReadFinish(false);
                BookServiceManager.getInstance().addBook(book);
                bookList = BookServiceManager.getInstance().getBookList();
                rvBookAdapter.update(bookList);
                break;
            default:
                break;
        }
    }
}
