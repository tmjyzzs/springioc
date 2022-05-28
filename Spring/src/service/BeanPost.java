package service;

import Application.BeanPostProcessor;
import annotation.Component;

//bean的后置类
//@Component（组件）使用这个组件让spring发现这个主键
//bean也是spring的一个组件
@Component("beanPost")
public class BeanPost implements BeanPostProcessor {
    //所有的bean在创建前都会执行这个方法
    @Override
    public Object afterPostProcessor() {
        System.out.println("bean的初始化后");

        return null;
    }
}
