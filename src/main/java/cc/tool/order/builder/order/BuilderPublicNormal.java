package cc.tool.order.builder.order;

import cc.admore.infant.dao.entity.homePage.SystemSetUp;
import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.customer.PublicCustomer;
import cc.admore.infant.model.order.NormalOrder;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.bucket.order.OrderBucketCache;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoPublicNormal;
import org.springframework.util.StringUtils;

/**
 * Created by lxj on 18-4-26
 * <p>
 * 公众号订单构造
 * <p>
 * 常规订单
 */
public class BuilderPublicNormal extends OrderBuilder<NormalOrder> {

    public BuilderPublicNormal(OrderGenerator<NormalOrder, ?, ?> orderGenerator) {

        super(orderGenerator);

    }

    @Override
    public NormalOrder build() {

        super.build();

        OrderBucketCache orderBucketCache = orderGenerator.getOrderBucket();

        PublicCustomer customer = (PublicCustomer) orderBucketCache.findCustomer();

        OrderInfoPublicNormal orderInfo = (OrderInfoPublicNormal) orderGenerator.orderInfo;

        order.setFromMark(FromMark.NORMAL);

        order.setQrCodeKey(customer.getQrCodeKey());

        order.setChannelFromMark(ChannelFromMark.WX_PUBLIC_ENSJ);

        order.setDistributionId(StringUtils.isEmpty(orderInfo.getDistributionId()) ? null : orderInfo.getDistributionId());

        SystemSetUp systemSetUp = orderGenerator.operationManager.homePageRedisHandle.findSystemSetUp(order.getChannelFromMark(), order.getFromMark());

        order.setExpire(systemSetUp.getExpire());

        return order;

    }

}
