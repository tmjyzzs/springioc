package service;

import annotation.Autowired;
import annotation.Component;

@Component("user")
public class User {

     @Autowired
    Order order;
    @Autowired
    Dao dao;
    public void text(){
        System.out.println("+++++++++++++++++"+order);
        System.out.println(dao);

    }
}
