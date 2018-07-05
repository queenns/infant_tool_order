package cc.tool.order.builder;

import cc.tool.order.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lxj on 18-5-21
 */
public abstract class AbstractBuilder<T> implements Builder<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Class<?> clazz;

    @SuppressWarnings("unchecked")
    protected void initType() {

        this.clazz = ((Class<?>) ClassUtil.findParameterizedType(getClass()).getActualTypeArguments()[0]);

    }
    
}
