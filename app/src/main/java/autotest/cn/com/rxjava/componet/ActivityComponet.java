package autotest.cn.com.rxjava.componet;

import android.app.Activity;

import autotest.cn.com.rxjava.ui.MainActivity;
import autotest.cn.com.rxjava.PerActivity;
import autotest.cn.com.rxjava.module.ActivityModule;
import dagger.Component;

/**
 * @author LinDingQiang
 * @time 2018/8/7 15:55
 * @email dingqiang.l@verifone.cn
 */
@PerActivity
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponet {
    void inject(MainActivity mainActivity);
    String str();
    Activity activity();
}