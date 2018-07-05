package cc.tool.order.generator;

import cc.tool.order.exception.CreateException;

/**
 * Created by lxj on 18-4-25
 */
public interface Generator<T> {

    T create() throws CreateException;

}
