package cc.tool.order.builder.order;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.bucket.order.OrderBucketCache;
import cc.tool.order.builder.AbstractBuilder;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfo;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 订单构造
 */
public abstract class OrderBuilder<T extends Order> extends AbstractBuilder<T> {

    protected T order;

    OrderGenerator<T, ?, ?> orderGenerator;

    OrderBuilder(OrderGenerator<T, ?, ?> orderGenerator) {

        this.orderGenerator = orderGenerator;

        initType();

        try {

            this.order = (T) this.clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public T build() {

        OrderInfo orderInfo = orderGenerator.orderInfo;

        OrderBucketCache orderBucketCache = orderGenerator.getOrderBucket();

        order.setStatus(Status.NONPAYMENT);

        order.setOpenId(orderInfo.getOpenId());

        order.setDress(orderBucketCache.findDress());

        order.setAmount(orderBucketCache.findAmount());

        order.setCountInfos(orderInfo.getCountInfos());

        order.setPostage(orderBucketCache.findPostage());

        order.setCustomer(orderBucketCache.findCustomer());

        order.setGoodsNos(orderBucketCache.findGoodsNos());

        order.setActualAmount(orderBucketCache.findActualAmount());

        order.setUnionId(orderBucketCache.findCustomer().getUnionId());

        order.setOrderGoodsInfos(orderBucketCache.findOrderGoodsInfos());

        return order;

    }

}
