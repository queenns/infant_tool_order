package cc.tool.order.util;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lxj on 18-5-21
 */
public class OrderConfigureUtil extends PropertyPlaceholderConfigurer {

    private static Map<String, String> clientPropertyConfigurerMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory configurableListableBeanFactory, Properties properties) {

        super.processProperties(configurableListableBeanFactory, properties);

        clientPropertyConfigurerMap = new HashMap<String, String>();

        for (Object key : properties.keySet()) {

            String mapKey = key.toString();

            String mapVal = properties.getProperty(mapKey);

            clientPropertyConfigurerMap.put(mapKey, mapVal);

        }

    }

    public static String getValue(String key) {

        return clientPropertyConfigurerMap.get(key);

    }

}
