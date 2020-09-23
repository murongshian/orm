package com.zwd.orm.sqlSession;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zwd.orm.configuration.Function;
import com.zwd.orm.configuration.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 读取与解析配置信息，并返回处理后的Environment
 */
public class MyConfiguration {
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();//系统类加载器

    /**
     * 读取xml信息并处理
     */
    public  Connection build(String resource){
        try {
            InputStream stream = loader.getResourceAsStream(resource);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            return evalDataSource(root);
        } catch (Exception e) {
            throw new RuntimeException("error occured while evaling xml " + resource);
        }
    }

    private  Connection evalDataSource(Element node) throws ClassNotFoundException {
        if (!node.getName().equals("database")) {
            throw new RuntimeException("root should be <database>");
        }
        String driverClassName = null;
        String url = null;
        String username = null;
        String password = null;
        //获取属性节点
        for (Object item : node.elements("property")) {
            Element i = (Element) item;
            String value = getValue(i);
            String name = i.attributeValue("name");
//            if (name == null || value == null) {
//                throw new RuntimeException("[database]: <property> should contain name and value");
//            }
            //赋值
            switch (name) {
                case "url" :
                    url = value;
                    break;
                case "username" :
                    username = value;
                    break;
                case "password" :
                    password = value;
                    break;
                case "driverClassName" :
                    driverClassName = value;
                    break;
                default :
                    throw new RuntimeException("[database]: <property> unknown name");
            }
        }
        Class.forName(driverClassName);
        Connection connection = null;
        try {
            //建立数据库链接
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //获取property属性的值,如果有value值,则读取 没有设置value,则读取内容
    private  String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    @SuppressWarnings("rawtypes")
    public MapperBean readMapper(String path){
        MapperBean mapper = new MapperBean();
        try{
            //以流的形式加载资源路径下的文件
            InputStream stream = loader.getResourceAsStream(path);
            //SAXReader就是一个管道，用一个流的方式，把xml文件读出来
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            //把mapper节点的nameSpace值存为接口名
            mapper.setInterfaceName(root.attributeValue("nameSpace").trim());
            List<Function> list = new ArrayList<>(); //用来存储函数的List
            for(Iterator rootIter = root.elementIterator();rootIter.hasNext();) {//遍历根节点下所有子节点
                Function fun = new Function();    //用来存储一个函数的信息
                Element e = (Element) rootIter.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                fun.setSqltype(sqltype);
                fun.setFuncName(funcName);
                Object newInstance=null;
                try {
                    newInstance = Class.forName(resultType).newInstance();//？？？？？？？？？？？？
                    // || 和 | 都是表示“或”，区别是 || 只要满足第一个条件，后面的条件就不再判断，而|要对所有的条件进行判断
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                fun.setResultType(newInstance);
                fun.setSql(sql);
                list.add(fun);
            }
            mapper.setList(list);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return mapper;
    }
}