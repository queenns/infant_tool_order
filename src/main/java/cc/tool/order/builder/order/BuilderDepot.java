package cc.tool.order.builder.order;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.bucket.order.DepotBucketCache;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoDepot;

/**
 * Created by lxj on 18-5-11
 * <p>
 * 拆分订单构造
 */
public abstract class BuilderDepot extends OrderBuilder<DepotOrder> {

    public BuilderDepot(OrderGenerator<DepotOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public DepotOrder build() {

        super.build();

        OrderInfoDepot orderInfo = (OrderInfoDepot) orderGenerator.orderInfo;

        DepotBucketCache depotBucketCache = (DepotBucketCache) orderGenerator.getOrderBucket();

        Order masterOrder = orderInfo.getOrder();

        order.setHoldMarker(true);

        order.setMasterId(masterOrder.getId());

        order.setShopId(orderInfo.getShopId());

        order.setStatus(masterOrder.getStatus());

        order.setRemark(masterOrder.getRemark());

        order.setWelfare(masterOrder.getWelfare());

        order.setChannelFromMark(masterOrder.getChannelFromMark());

        order.setShopName(depotBucketCache.findUser().getShopName());

        return order;

    }

}
