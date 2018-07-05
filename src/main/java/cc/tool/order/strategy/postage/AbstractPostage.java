package cc.tool.order.strategy.postage;

import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.strategy.Strategy;

/**
 * Created by lxj on 18-5-2
 */
public abstract class AbstractPostage implements Strategy {

    abstract Long calculate(OrderBucket orderBucket);

}
