package cn.LJW.utils;

import cn.LJW.entity.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author 86198
 */
public class QueryUtils {

    public static Object fieldFull(Class c, ResultSet resultSet) throws SQLException {

        //判断一共多少条数据，如果为零直接返回
        if(calculateResultSetCount(resultSet)==0){
            return null;
        }
        //返回给用户的对象
        Object o;
        try {
            //反射创建返回给用户的对象
            o = c.newInstance();
            //获取该类所有属性
            Field[] declaredFields = c.getDeclaredFields();
            //获取所有方法
            Method[] methods = c.getDeclaredMethods();
            //储存所有set方法的名称和参数类型的map集合
            Map<String, Class<?>> methodMap = new HashMap<>(methods.length);
            //填充储存set方法的名称和参数类型的map
            for (Method method : methods) {
                if (method.getName().startsWith("set")) {
                    methodMap.put(method.getName(), method.getParameterTypes()[0]);
                }
            }
            //储存结果集中数据的map
            Map<String, Object> objects = new HashMap<>(declaredFields.length);
            //把结果集中的数据封装进map
            while (resultSet.next()) {
                for (Field declaredField : declaredFields) {
                    objects.put(declaredField.getName(), resultSet.getObject(declaredField.getName()));
                }
            }
            //反射执行set把objectsMap中的结果集数据封装进实体类
            for (Map.Entry<String, Class<?>> setMethod : methodMap.entrySet()) {
                //遍历储存set方法的map，拿出每一个set方法的Method对象
                Method method = c.getMethod(setMethod.getKey(), setMethod.getValue());
                //处理set方法的名称，把set去掉获取当前要set的属性名
                String set = method.getName().replace("set", "");
                //set方法set后的属性名是大写的转换为小写就成了属性名
                String set1 = set.replace(set.charAt(0), set.toLowerCase(Locale.ROOT).charAt(0));
                //通过动态执行set方法把结果集封装到对象中（通过封装结果集map中的key也就是属性名，获取属性值作为参数执行方法）
                method.invoke(o, objects.get(set1));
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("封装对象失败！！！");
        }
        //返回对象
        return o;
    }

    public static Integer calculateResultSetCount(ResultSet resultSet) throws SQLException {
        // 将光标移动到最后一行
        resultSet.last();
        // 得到当前行号，即结果集记录数
        int rowCount = resultSet.getRow();
        resultSet.beforeFirst();
        return rowCount;
    }
}
