package autotest.cn.com.rxjava.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import autotest.cn.com.rxjava.R;
import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;

public class ComposeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private TextView content;
    private String TAG = "tag";

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
        listItemStr.add("concat");
        listItemStr.add("concatArray");
        listItemStr.add("merge");
        listItemStr.add("mergeArray");
        listItemStr.add("concatArrayDelayError");
        listItemStr.add("zip");
        listItemStr.add("combineLatest");
        listItemStr.add("reduce");
        listItemStr.add("collect");
        listItemStr.add("startWith");
        listItemStr.add("count");
        return listItemStr;
    }

    private void intiListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    concat();
                    break;
                case 1:
                    concatArray();
                    break;
                case 2:
                    merge();
                    break;
                case 3:
                    mergeArray();
                    break;
                case 4:
                    concatArrayDelayError();
                    break;
                case 5:
                    zip();
                    break;
                case 6:
                    combineLatest();
                    break;
                case 7:
                    reduce();
                    break;
                case 8:
                    collect();
                    break;
                case 9:
                    startWith();
                    break;
                case 10:
                    count();
                    break;
                default:
            }
        });
    }

    /**
     * 统计发送事件数量
     */
    private void count() {
        Observable
                .just("a", "b", "c")
                .count()
                .subscribe(aLong -> Log.i(TAG, "发送数据事件数量: " + aLong));
    }

    /**
     * 在事件发送之前追加一些数据，或者追加新的事件,追加顺序：后调用则先追加
     */
    private void startWith() {
        Observable
                .just(6)//初始要发送的事件，发送了数据6
                .startWith(5)//追加数据5
                .startWithArray(3, 4)//追加多个数据3，4
                .startWith(Observable.just(1, 2))//追加事件，改追加的事件发送了数据1，2
                .subscribe(s -> Log.i(TAG, "发送的事件: " + s));
    }

    /**
     * 将发送的数据发到一个数据结构中(如ArrayList)
     */
    private void collect() {
        Observable
                .just("a", "b", "c")
                .collect(ArrayList::new, (BiConsumer<ArrayList<String>, String>) ArrayList::add)
                .subscribe(strings -> Log.i(TAG, "输出结果是: " + strings));
    }

    /**
     * 将要发送的事件聚合成一个新的事件，然后在发送
     */
    private void reduce() {
        Observable
                .just("a", "b", "c")
                .reduce((s, s2) -> {
                    Log.i(TAG, "本次聚合的数据是: " + s + "   " + s2);
                    return s + s2;
                }).subscribe(s -> Log.i(TAG, "最终结果是: " + s));
    }

    /**
     * 将observable1最新（即最后）发送的数据与observable2发送的数据合并
     */
    private void combineLatest() {
        Observable.combineLatest(
                Observable.just(1, 2, 3),
                Observable.just("a", "b", "c"),
                (integer, s) -> {
                    Log.e(TAG, "合并的数据是： " + integer + " " + s);
                    return integer + s;
                })
                .subscribe(s -> Log.i(TAG, "zip: " + s));
    }

    /**
     * 合并多个事件，生成一个新事件，然后发送
     */
    private void zip() {
        Observable.zip(
                Observable.just(1, 2, 3),
                Observable.just("a", "b", "c"),
                (integer, s) -> {
                    Log.e(TAG, "合并的数据是： " + integer + " " + s);
                    return integer + s;
                })
                .subscribe(s -> Log.i(TAG, "zip: " + s));
    }

    /**
     * 使用concat或merge，如果其中的某个事件抛异常，使用concatArrayDelayError可继续发送未发送完成事件，最后才触发Error
     */
    private void concatArrayDelayError() {
        Observable.concatArrayDelayError(
                Observable.just("1"),
                Observable.error(new NullPointerException()),
                Observable.just("2"),
                Observable.just("3"))
                .subscribe(str -> Log.i(TAG, "输出：" + str));

    }

    private void mergeArray() {
        Observable.mergeArray(
                Observable.intervalRange(0, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(2, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(3, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(4, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(5, 5, 1, 1, TimeUnit.SECONDS))
                .subscribe(aLong -> Log.i(TAG, "输出：" + aLong));
    }

    /**
     * 组合多个被观察者一起发送数据，合并后 按时间线并行执行,最多只能发四个
     */
    private void merge() {
        Observable.merge(
                Observable.intervalRange(0, 5, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(2, 5, 1, 1, TimeUnit.SECONDS))
                .subscribe(aLong -> Log.i(TAG, "输出：" + aLong));
    }

    /**
     * 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行,最多可大于四个
     */
    private void concatArray() {
        Observable.concatArray(
                Observable.just("1"),
                Observable.just("2"),
                Observable.just("3"),
                Observable.just("4"),
                Observable.just("5"))
                .subscribe(integer -> Log.i(TAG, "输出：" + integer));
    }

    /**
     * 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行，最多只能发四个
     */
    private void concat() {
        Observable.concat(
                Observable.just("a", "b"),
                Observable.just("c", "d"),
                Observable.just("e"),
                Observable.just("g", "h"))
                .subscribe(str -> Log.i(TAG, "输出：" + str));
    }

}
