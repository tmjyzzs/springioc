package Application;


import annotation.Autowired;
import annotation.Component;
import annotation.ComponentScan;
import annotation.Scope;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class applicationContext {
     //
    Class aClass;
    //单例池
    ConcurrentHashMap<String,Object> singleton = new ConcurrentHashMap<>();
    ConcurrentHashMap<String,BeanDefinition> beanDefinitionConcurrentHashMap = new ConcurrentHashMap<>();
    //使用一个list来存放beanPostProcessor的实现类
    List<BeanPostProcessor> postProcessorArrayList = new ArrayList<BeanPostProcessor>();

    public applicationContext(Class aClass) {
        //扫描完成，并且设置了BeanDefinition的值
        Scan(aClass);
        //把单例的bean放入单例池中
        //把遍历扫描的类那些事单例的
        Iterator<Map.Entry<String, BeanDefinition>> iterator = beanDefinitionConcurrentHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, BeanDefinition> next = iterator.next();
            if(next.getValue().getScope()=="singleton"){
                //创建对象放入
                Object singletonBean = creatBean(next.getValue());
                singleton.put(next.getKey(),singletonBean);
            }else {
                //如果不是单例的bean创建好就可以了
               // Object o = creatBean(next.getValue());
            }
        }

    }
    public Object creatBean( String beanName){
        BeanDefinition beanDefinition = beanDefinitionConcurrentHashMap.get(beanName);
        try {
            Object bean = beanDefinition.getaClass().newInstance();
            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
    //重载creatBean()
    public Object creatBean(BeanDefinition beanDefinition)  {
        //根据BeanDefinition进行创建对象
        Class aClass = beanDefinition.getaClass();
        Object object =null;
        try {
             object = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        //@Autowired自动注入的实现
        //获取属性上的注解
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field: declaredFields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                //获取对象
                Object bean = getBean(field.getName());

                //set给那个对象赋值
                try {
                    field.setAccessible(true);
                    field.set(object, bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

        //实现类的初始化
        if(object instanceof InitializingBean){
            ((InitializingBean) object).initializingBean();
        }
        //BeanName的回调
        if(object instanceof BeanNameAware){

        }
        //类的后置处理器
        //从list中遍历取出bean的后置处理器对象(可以创建多个bean的后置处理器)
        for (BeanPostProcessor list:postProcessorArrayList) {
            //可以执行AOP
            list.afterPostProcessor();
        }
        return object;

    }


        private void Scan(Class aClass) {
        this.aClass = aClass;
        //解析配置类来创建Bean对象
        ComponentScan ComponentScan = (annotation.ComponentScan) aClass.getDeclaredAnnotation(ComponentScan.class);
        //读取注解中的信息
        String path = ComponentScan.value();//path  service
        //类加载器
        ClassLoader classLoader = ComponentScan.getClass().getClassLoader();
        String resource = classLoader.getResource(path).getPath();  //resource    /D:/IdeaVincent/untitled4/out/production/Spring/service
        File file = new File(resource);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File f :files) {
                String name = f.getName();
                String relPath = path+"."+name.substring(0, name.indexOf(".class"));

                try {
                    Class<?> aClass1 = classLoader.loadClass(relPath);
                    //通过类的信息来获取注解信息
                    Component annotation = aClass1.getAnnotation(Component.class);
                    if(annotation !=null){
                        String beanName = annotation.value();
                        //把注解的信息存放在BeanDefinition中
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setaClass(aClass1);
                        //如果有注解
                        if(aClass1.isAnnotationPresent(Scope.class)){
                            //获取Scope的值
                            String value = aClass1.getAnnotation(Scope.class).value();
                            beanDefinition.setScope(value);

                        }else {
                            //如果没有值的话设置成为单例模式
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitionConcurrentHashMap.put(beanName,beanDefinition);
                        //存放到BeanDefinitionMap中
                    }
                    //判断这个class对象是否是BeanPostProcessor类的衍生子类
                    if(BeanPostProcessor.class.isAssignableFrom(aClass1)){
                        //创建一个Bean后置处理器的类保存在List中
                        BeanPostProcessor beanPost = (BeanPostProcessor) aClass1.newInstance();

                        postProcessorArrayList.add(beanPost);
                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Object getBean(String beanName){
        //根据BeanDefinition来创建对象
        BeanDefinition beanDefinition = beanDefinitionConcurrentHashMap.get(beanName);
        //是原型则创建新的对象返回
        if("prototype".equals(beanDefinition.getScope())){
            Object prototypeBean = creatBean(beanDefinition);
            return prototypeBean;
        }else if("singleton".equals(beanDefinition.getScope())){
            //单例则返回单例bean
            Object  singletonBean= singleton.get(beanName);
            return singletonBean;
        }else {

            throw new RuntimeException();


        }

    }
}
