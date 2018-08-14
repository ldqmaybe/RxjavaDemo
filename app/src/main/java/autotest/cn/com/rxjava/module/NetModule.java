package autotest.cn.com.rxjava.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author LinDingQiang
 * @time 2018/8/9 15:31
 * @email dingqiang.l@verifone.cn
 */
@Module
public class NetModule {
    @Singleton
    @Provides
    OkHttpClient provideOKHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
