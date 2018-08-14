package autotest.cn.com.rxjava.module;

import android.app.Activity;

import autotest.cn.com.rxjava.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * @author LinDingQiang
 * @time 2018/8/7 16:13
 * @email dingqiang.l@verifone.cn
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity activity() {
        return this.activity;
    }
    @Provides
    String provideStr() {
        return "ghngfd";

    }
}
