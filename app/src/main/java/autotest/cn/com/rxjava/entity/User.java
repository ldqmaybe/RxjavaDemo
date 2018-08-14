package autotest.cn.com.rxjava.entity;

import javax.inject.Inject;

/**
 * @author LinDingQiang
 * @time 2018/8/7 15:42
 * @email dingqiang.l@verifone.cn
 */
public class User {
    private String name;

    @Inject
    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
