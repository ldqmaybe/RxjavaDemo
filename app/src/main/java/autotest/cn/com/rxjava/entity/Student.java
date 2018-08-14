package autotest.cn.com.rxjava.entity;

import javax.inject.Inject;

/**
 * @author LinDingQiang
 * @time 2018/8/9 15:05
 * @email dingqiang.l@verifone.cn
 */
public class Student {
    private String name;
    @Inject
    public Student() {

    }

    public String getName() {
        return "Student";
    }

    public void setName(String name) {
        this.name = name;
    }
}
