package cc.tool.order.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lxj on 18-5-22
 */
public class ClassUtil {

    public static ParameterizedType findParameterizedType(Class clazz) {

        ParameterizedType parameterizedType;

        while (true) {

            Type type = clazz.getGenericSuperclass();

            if (type instanceof Class) {

                clazz = (Class) type;

            } else {

                parameterizedType = (ParameterizedType) type;

                break;

            }

        }

        return parameterizedType;

    }

}
