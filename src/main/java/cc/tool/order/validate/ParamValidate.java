package cc.tool.order.validate;

import cc.tool.order.exception.InfoArgumentException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by lxj on 18-5-14
 */
public abstract class ParamValidate {

    private static ArrayList<String> keysObject = new ArrayList<String>() {{

        add("order");

        add("charge");

        add("orderId");

        add("payMark");

        add("customerId");

        add("luckCustomer");

    }};

    private static ArrayList<String> keysString = new ArrayList<String>() {{

        add("openId");

        add("shopId");

        add("memberId");

        add("miniOpenId");


    }};

    private static ArrayList<String> keysMap = new ArrayList<String>() {{

        add("wechat");

        add("countInfos");

    }};

    public static void isEmpty(Object[]... objects) {

        for (Object[] object : objects)

            isEmpty((String) object[0], object[1]);

    }

    public static void isEmpty(String key, Object value) {

        if (keysObject.contains(key))

            objectEmpty(key, value);

        else if (keysString.contains(key))

            stringEmpty(key, value);

        else if (keysMap.contains(key))

            mapEmpty(key, value);

        else

            throw new InfoArgumentException("Validate key [" + key + "] not found,please check or configure");

    }

    private static void mapEmpty(String key, Object value) {

        if (CollectionUtils.isEmpty((Map<?, ?>) value))

            exception(key);

    }

    private static void objectEmpty(String key, Object value) {

        if (ObjectUtils.isEmpty(value))

            exception(key);

    }

    private static void stringEmpty(String key, Object value) {

        if (StringUtils.isEmpty(value))

            exception(key);

    }

    private static void exception(String key) {

        throw new InfoArgumentException(key + " must be not null");

    }

}
