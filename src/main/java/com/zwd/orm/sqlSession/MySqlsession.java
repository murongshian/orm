package com.zwd.orm.sqlSession;

import java.lang.reflect.Proxy;

public class MySqlsession {

    private Excutor excutor = new MyExcutor();

    private MyConfiguration myConfiguration = new MyConfiguration();

    /**
     * 第二个T很好理解，表示返回值类型；
     * 而第一个<T>的作用是声明这个方法是个泛型方法。
     */
    public <T> T selectOne(String statement,Object parameter){
        return excutor.query(statement, parameter);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clas){
        //动态代理调用
        return (T)Proxy.newProxyInstance(clas.getClassLoader(),new Class[]{clas},
                new MyMapperProxy(myConfiguration,this));
    }
}