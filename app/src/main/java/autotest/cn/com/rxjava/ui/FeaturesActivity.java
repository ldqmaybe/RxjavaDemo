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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeaturesActivity extends AppCompatActivity {
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
        listItemStr.add("scheduler");
        listItemStr.add("delay");
        listItemStr.add("do");
        listItemStr.add("onErrorReturn");
        listItemStr.add("retry");
        listItemStr.add("repeat");
        return listItemStr;
    }

    private void intiListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position) {
                case 0:
                    scheduler();
                    break;
                case 1:
                    delay();
                    break;
                case 2:
                    doXXX();
                    break;
                case 3:
                    onErrorReturn();
                    break;
                case 4:
                    retry();
                    break;
                case 5:
                    repeat();
                    break;
                default:
            }
        });
    }

    /**
     * 无条件地、重复发送 被观察者事件a
     */
    private void repeat() {
        Observable
                .just("事件源")
                .repeat(3)
                .subscribe(s -> Log.i(TAG, "接收数据: " + s));
    }

    /**
     * 重试，即当出现错误时，让被观察者（Observable）重新发射数据
     */
    private void retry() {
        Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext("数据源");
                    emitter.onError(new Throwable("error"));
                })
                .retry(3)//重试三次，其他重载方法不在次演示
                .subscribe(s -> Log.i(TAG, "接收到的数据: " + s));
    }

    /**
     * 遇到错误时，发送1个特殊事件 & 正常终止
     */
    private void onErrorReturn() {
        Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext("开始发送数据");
                    emitter.onError(new Throwable("error"));
                })
                .onErrorReturn(throwable -> {
                    Log.i(TAG, "onErrorReturn 发生错误了,错误信息: " + throwable.getMessage());
                    return "onErrorReturn";
                })
                .subscribe(s -> Log.i(TAG, "接收数据: " + s));
    }

    private void doXXX() {
        Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext("a");
                    emitter.onNext("b");
                    emitter.onNext("c");
                    emitter.onError(new Throwable("error"));
                })
                .doAfterNext(s -> Log.i(TAG, "doAfterNext 执行Next事件之后调用: " + s))//执行Next事件之后调用
                .doAfterTerminate(() -> Log.i(TAG, "doAfterTerminate 发送事件完毕后调用，无论正常发送完毕 / 异常终止 "))//发送事件完毕后调用，无论正常发送完毕 / 异常终止
                .doFinally(() -> Log.i(TAG, "doFinally 最后执行"))
                .doOnError(throwable -> Log.i(TAG, "doOnError: " + throwable.getMessage()))
                .subscribe(s -> Log.i(TAG, "subscribe 接收数据: " + s));
    }

    /**
     * 被观察者延迟一段时间再发送事件
     */
    private void delay() {
        Observable.just("被观察者延迟一段时间再发送事件")
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> content.setText(s));
    }

    /**
     * 线程切换
     */
    private void scheduler() {
        Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    Log.i(TAG, "发送数据所在的线程: " + Thread.currentThread().getName());
                    emitter.onNext("线程调度");
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())//在io线程产生事件，发送数据
                .observeOn(Schedulers.io())//切换到io线程接收、响应
                .subscribeOn(AndroidSchedulers.mainThread())//第二次指定无效
                .observeOn(AndroidSchedulers.mainThread())//在UI线程中接收、响应事件
                .subscribe(s -> Log.i(TAG, "接收数据所在的线程: " + Thread.currentThread().getName()));
    }

}
