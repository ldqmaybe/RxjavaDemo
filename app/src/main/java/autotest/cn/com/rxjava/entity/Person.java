package autotest.cn.com.rxjava.entity;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * @author LinDingQiang
 * @time 2018/8/9 15:08
 * @email dingqiang.l@verifone.cn
 */
public class Person {
   private Teacher teacher;
   private Student student;
    private OkHttpClient okHttpClient;
    @Inject
    Person(Teacher teacher, Student student, OkHttpClient okHttpClient) {
        this.teacher = teacher;
        this.student = student;
        this.okHttpClient = okHttpClient;
    }

    public String getDate(){
        return teacher.getName() +" : " +student.getName() +" : "+ okHttpClient + "  3 ";
    }
}
