import Application.applicationContext;
import annotation.ComponentScan;
import service.Dao;
import service.Order;
import service.User;



@ComponentScan("service")
public class Text {
    public static void main(String[] args) {
        //完全利用jdk创建spring容器
        //
        applicationContext applicationContext = new applicationContext(Text.class);
        User user = (User)applicationContext.getBean("user");
        Object user1 = applicationContext.getBean("user");
        Object user2 = applicationContext.getBean("user");
        Order order1 = (Order)applicationContext.getBean("order");
        Object order2 = applicationContext.getBean("order");
        Object order3 = applicationContext.getBean("order");
        Dao dao = (Dao) applicationContext.getBean("dao");
       /* System.out.println(user);
        System.out.println(user1);
        System.out.println(user2);
        System.out.println(order1);
        System.out.println(order2);
        System.out.println(order3);*/
        /*user.text();
        order1.text();
        dao.text();*/
       // applicationContext.getBean("01");
    }
}
