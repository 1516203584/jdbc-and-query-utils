package cn.LJW.utils;

import cn.LJW.entity.User;
import cn.LJW.exceptions.userExceptions.ParamsAreNullException;
import cn.LJW.exceptions.utilsExceptions.judgeIsNullUtilsExceptions.ObjectIsNullException;
import cn.LJW.exceptions.utilsExceptions.judgeIsNullUtilsExceptions.StringIsBlankException;

/**
 * JudgeIsNull
 */
public class JudgeIsNullUtils {

    public static final String OBJECT_IS_NULL_MSG="对象没有初始化，请检查";
    public static final String STRING_IS_BLANK_MSG="字符串为空字符串,请检查";
    public static final String PARAM_IS_NULL_MSG="参数为空，请检查";
    
    public static void judgeIsNull(Object... o){
        for (int i = 0; i < o.length; i++) {
            System.out.println(o[i]);
            if(o[i]==null){
                throw new ObjectIsNullException(OBJECT_IS_NULL_MSG+o[i].getClass().getName());
            }
    
            if(o[i] instanceof String){
                if("".equals((String)o[i])){
                    throw new StringIsBlankException(STRING_IS_BLANK_MSG+o[i]); 
                }
            }
        }
    }

    public static void main(String[] args) {
        User u=new User();
        u.setUsername("username");
        u.setPassword("");
        judgeIsNull(u,u.getUsername(),u.getPassword(),"asdasdas",5,888,1.58,true,'a');
    }

}
