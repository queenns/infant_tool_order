package cc.tool.order;

import cc.tool.order.bucket.order.OrderBucketCache;
import cc.tool.order.generator.Generator;

/**
 * Created by lxj on 18-5-3
 */
public interface OrderCreateTool<T, P, G> extends OrderBucketCache<P, G>, Generator<T> {

    /**
     * 创建订单后是否默认存储数据库
     * 开启
     */
    void enableKeep();

    /**
     * 创建订单后是否默认存储数据库
     * 关闭
     */
    void unableKeep();

}
