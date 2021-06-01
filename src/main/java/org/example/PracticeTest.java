package org.example;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.example.component.Resource;
import org.example.component.SqlSession;
import org.example.component.SqlSessionFactory;
import org.example.component.SqlSessionFactoryBuilder;
import org.example.dao.UserDao;
import org.example.pojo.User;
import org.example.service.ProxyTestService;
import org.example.service.TestService;
import org.example.service.impl.TestServiceImpl;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class PracticeTest {





    @org.junit.Test
    public void proxyTest() {
        TestService testService = new TestServiceImpl();
        ProxyTestService proxy = new ProxyTestService(testService);
        TestService instance = proxy.getInstance();
        instance.say();
        instance.hello();
    }


    @Test
    public void reflectionTest() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        User paramUser = new User();
        paramUser.setId(1);
        paramUser.setName("123");
        testParam(paramUser);

        List<User> paramUserList = new ArrayList<>();
        paramUserList.add(paramUser);

        testParam(paramUserList);




        Class<?> clazz = Class.forName("org.example.pojo.User");
        Object o = clazz.newInstance();
        Field name = o.getClass().getDeclaredField("name");
        name.setAccessible(true);


        Object name2 = name.get(paramUser);

//        name.set(o, "张三");
        User user = (User) o;
        String name1 = user.getName();
        System.out.println(name1);
    }


    public void testParam(Object... params) {

        for (int i = 0; i < params.length; i++) {

            Object param = params[i];






        }


    }


    /**
     * 使用jdbc的方式操作数据库
     * 他的弊端有哪些呢
     * 1.硬编码问题，数据库的链接信息都编写在了代码之中
     * 2.结果集的处理很麻烦，不能和pojo对应起来
     * 3.频繁创建数据库链接和销毁都很浪费系统资源
     * @throws ClassNotFoundException
     */
    @Test
    public void DBTest() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://192.168.100.222:3306/team";
        String user = "root";
        String password = "mysqlHolder";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            String sql = "select * from team.hp_user";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                System.out.println("name is "+name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    @Test
    public void testMyORM() throws SQLException, PropertyVetoException, DocumentException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException {
        InputStream inputStream = Resource.getInputStream("sqlMap.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = build.openSession();
        User paramUser = new User();
        paramUser.setId(1);
        paramUser.setName("123");
//        User users = sqlSession.selectOne("user.findAll", paramUser);
//        System.out.println(users);
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> users = mapper.selectAll(paramUser);
        for (User user : users) {
            System.out.println(user);
        }
    }
}
