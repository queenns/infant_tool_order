package cc.tool.order.builder.order;

import cc.admore.infant.dao.entity.homePage.SystemSetUp;
import cc.admore.infant.model.customer.PublicCustomer;
import cc.admore.infant.model.order.PanicOrder;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.bucket.order.OrderBucketCache;
import cc.tool.order.generator.order.OrderGenerator;

/**
 * Created by lxj on 18-5-7
 * <p>
 * 公众号订单构造
 * <p>
 * 抢购订单
 */
public class BuilderPublicPanic extends OrderBuilder<PanicOrder> {

    public BuilderPublicPanic(OrderGenerator<PanicOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public PanicOrder build() {

        super.build();

        OrderBucketCache orderBucketCache = orderGenerator.getOrderBucket();

        PublicCustomer customer = (PublicCustomer) orderBucketCache.findCustomer();

        order.setFromMark(FromMark.PANICBUYING);

        order.setQrCodeKey(customer.getQrCodeKey());

        order.setChannelFromMark(ChannelFromMark.WX_PUBLIC_ENSJ);

        SystemSetUp systemSetUp = orderGenerator.operationManager.homePageRedisHandle.findSystemSetUp(order.getChannelFromMark(), order.getFromMark());

        order.setExpire(systemSetUp.getExpire());

        return order;

    }

}
