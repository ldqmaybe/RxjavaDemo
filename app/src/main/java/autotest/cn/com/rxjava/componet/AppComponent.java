package autotest.cn.com.rxjava.componet;

import javax.inject.Singleton;

import autotest.cn.com.rxjava.module.NetModule;
import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author LinDingQiang
 * @time 2018/8/9 15:30
 * @email dingqiang.l@verifone.cn
 */
@Singleton
@Component(modules = NetModule.class)
public interface AppComponent {
    OkHttpClient getOKHttpClient();
}
