package Application;

//bean的后置处理器
//对于所有的bean都会执行后置处理器
//想要个别的类实现。需要进行判断：
public interface BeanPostProcessor {

    public Object afterPostProcessor();
}
