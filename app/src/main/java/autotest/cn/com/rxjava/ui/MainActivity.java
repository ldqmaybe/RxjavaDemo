package autotest.cn.com.rxjava.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import autotest.cn.com.rxjava.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAdapter();
        intiListener();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main, getListItemStr()) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv, item);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void intiListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(this, CreateActivity.class);
                    break;
                case 1:
                    intent.setClass(this, MapActivity.class);
                    break;
                case 2:
                    intent.setClass(this, ComposeActivity.class);
                    break;
                case 3:
                    intent.setClass(this, FeaturesActivity.class);
                    break;
                default:
            }
            startActivity(intent);
        });
    }

    private List<String> getListItemStr() {
        List<String> listItemStr = new ArrayList<>();
        listItemStr.add("创建");
        listItemStr.add("变换");
        listItemStr.add("组合");
        listItemStr.add("功能性");
        return listItemStr;
    }
}
