package autotest.cn.com.rxjava;

import android.app.Application;

import autotest.cn.com.rxjava.componet.AppComponent;
import autotest.cn.com.rxjava.componet.DaggerAppComponent;
import autotest.cn.com.rxjava.module.NetModule;

/**
 * @author LinDingQiang
 * @time 2018/8/9 15:33
 * @email dingqiang.l@verifone.cn
 */
public class MyApp extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent.builder().netModule(new NetModule()).build();
    }
    public AppComponent getAppComponent() {
        return this.appComponent;
    }
}
