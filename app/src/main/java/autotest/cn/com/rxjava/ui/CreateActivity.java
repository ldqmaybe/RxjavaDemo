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
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CreateActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseQuickAdapter adapter;
    private TextView content;
    private StringBuffer sb;
    private long initialDelay = 2;
    private long period = 1;
    private TimeUnit unit = TimeUnit.SECONDS;
    private long start = 3;
    private long count = 5;

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
        listItemStr.add("create");
        listItemStr.add("just");
        listItemStr.add("fromArray");
        listItemStr.add("fromIterable");
        listItemStr.add("timer");
        listItemStr.add("interval");
        listItemStr.add("intervalRange");
        listItemStr.add("range");
        return listItemStr;
    }

    private void intiListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    create();
                    break;
                case 1:
                    just();
                    break;
                case 2:
                    fromArray();
                    break;
                case 3:
                    fromIterable();
                    break;
                case 4:
                    timer();
                    break;
                case 5:
                    interval(initialDelay, period, unit);
                    break;
                case 6:
                    intervalRange(start, count, initialDelay, period, unit);
                    break;
                case 7:
                    range(3, 5);
                    break;
                default:
            }
        });
    }

    /**
     * range定时发送事件(响应的有rangeLong)
     *
     * @param start 发送事件起始序号
     * @param count 发送事件数量
     */
    private void range(int start, int count) {
        Observable
                .range(start, count)
                .subscribe(integer -> {
                    runOnUiThread(() -> content.setText("从" + start + "开始发送" + count + "个数,接收数据：" + integer));
                });
    }
    /**
     * intervalRange定时发送事件
     *
     * @param start        发送事件起始序号
     * @param count        发送事件数量
     * @param initialDelay 延迟时间
     * @param period       间隔时间
     * @param unit         单位
     */
    private void intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
        Observable
                .intervalRange(start, count, initialDelay, period, unit)
                .subscribe(aLong -> {
                    runOnUiThread(() -> content.setText("从" + start + "开始发送" + count + "个数,延迟时间为" + initialDelay + ",间隔时间" + period + ",数据接收：" + aLong));
                });
    }

    /**
     * interval定时发送事件
     *
     * @param initialDelay 延迟时间
     * @param period       间隔时间
     * @param unit         单位
     */
    private void interval(long initialDelay, long period, TimeUnit unit) {
        Observable
                .interval(initialDelay, period, unit)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong >= 3 && null != disposable) {
                            disposable.dispose();
                        }
                        runOnUiThread(() -> content.setText("延迟2秒执行,每一秒更新：" + aLong));
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() {  }
                });
    }

    /**
     * timer操作符可以延迟执行一段逻辑
     */
    private void timer() {
        Observable
                .timer(5, TimeUnit.SECONDS)
                .subscribe(aLong -> runOnUiThread(() -> content.setText("延迟5秒执行")));
    }

    /**
     * 快速创建fromIterable
     * 发送事件的特点：直接发送 传入的集合List数据
     */
    private void fromIterable() {
        sb = new StringBuffer();
        Observable
                .fromIterable(getListItemStr())
                .subscribe(s -> {
                    sb.append(s).append(" ");
                    content.setText(sb.toString().trim());
                });
    }

    /**
     * 快速创建fromArray
     * 发送事件的特点：直接发送 传入的数组数据
     */
    private void fromArray() {
        sb = new StringBuffer();
        String[] strArr = {"create", "just", "fromArray"};
        Observable.fromArray(strArr)
                .subscribe(str -> {
                    sb.append(str).append(" ");
                    Log.i("tag", sb.toString().trim());
                    content.setText(sb.toString().trim());
                });
    }

    /**
     * 快速创建just
     * 发送事件的特点：直接发送传入的事件
     */
    private void just() {
        Observable.just("这是使用just操作符发送的数据")
                .subscribe(s -> {
                    content.setText(s);
                });
    }

    /**
     * 基本创建
     */
    private void create() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("这是使用create操作符发送的数据");
            emitter.onComplete();
        }).subscribe(s -> content.setText(s));
    }

}
