package cc.tool.order.generator;

import cc.tool.order.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lxj on 18-4-26
 */
public abstract class AbstractGenerator<T> implements Generator<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Class<?> clazz;

    @SuppressWarnings("unchecked")
    protected void initType() {

        this.clazz = ((Class<?>) ClassUtil.findParameterizedType(getClass()).getActualTypeArguments()[0]);

    }

}
