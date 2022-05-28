package service;

import Application.InitializingBean;
import annotation.Autowired;
import annotation.Component;
import annotation.Scope;

@Component("dao")
@Scope("prototype")
public class Dao implements InitializingBean {
   @Autowired
    User user;
   public void text(){
       System.out.println(user);
   }
    @Override
    public void initializingBean() {
        System.out.println("初始化");
    }
}
