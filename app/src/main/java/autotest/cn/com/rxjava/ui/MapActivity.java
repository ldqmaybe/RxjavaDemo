package autotest.cn.com.rxjava.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import autotest.cn.com.rxjava.R;
import io.reactivex.Observable;

public class MapActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        initView();
        initAdapter();
        intiListener();
    }

    private void initView() {
        recyclerView = findViewById(R.id.rv_create);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        content = findViewById(R.id.content);
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_create, getListItemStr()) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_create, item);
            }
        };
        recyclerView.setAdapter(adapter);
    }
    private List<String> getListItemStr() {
        List<String> listItemStr = new ArrayList<>();
        listItemStr.add("map");
        listItemStr.add("flatMap");
        return listItemStr;
    }

    private void intiListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    map();
                    break;
                case 1:
                    flatMap();
                    break;
                default:
            }
        });
    }

    /**
     * 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable
     */
    private void flatMap() {
        Observable
                .just(12)
                .flatMap(integer -> Observable.just("经过flatMap转换后的数据："+integer))
                .subscribe(s -> content.setText(s));
    }

    /**
     * 可以将转换成另外一种数据类型
     */
    private void map() {
        Observable
                .just(12)
                .map(integer -> "经过map转换后的数据："+integer)
                .subscribe(s -> content.setText(s));
    }

}
