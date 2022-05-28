package service;

import Application.BeanNameAware;
import annotation.Autowired;
import annotation.Component;
import annotation.Scope;

@Component("order")
@Scope("prototype")
public class Order implements BeanNameAware {

    @Autowired
    Dao dao;

    String name;

    public void text(){
        System.out.println("+++++++++++++++++"+dao);

    }

    @Override
    public void Aware(String name ) {
        this.name=name;
    }
}
