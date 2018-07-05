package cc.tool.order.builder.refund;

import cc.admore.infant.model.follow.reference.FollowGoodsInfo;
import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.generator.refund.RefundGenerator;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 */
public abstract class RefundOrderBuilder extends RefundBuilder {

    public RefundOrderBuilder(Order order, RefundGenerator refundGenerator) {
        super(order, refundGenerator);
    }

    @Override
    protected Long findAmount() {

        return order.getAmount();

    }

    protected void buildDepotInfos() {

        List<DepotOrder> depotOrders = refundGenerator.operationManager.minceOrderDao.findDepotOrders(order.getId());

        if (CollectionUtils.isEmpty(depotOrders)) return;

        Map<ObjectId, Status> minceStates = new HashMap<ObjectId, Status>();

        List<FollowGoodsInfo> refundGoodses = new ArrayList<FollowGoodsInfo>();

        for (DepotOrder depotOrder : depotOrders) {

            for (ObjectId goodsId : depotOrder.getCountInfos().keySet()) {

                Long count = depotOrder.getCountInfos().get(goodsId);

                FollowGoodsInfo refundGoodsInfo = buildDepotGoodsInfo(depotOrder, goodsId, count);

                refundGoodses.add(refundGoodsInfo);

            }

            minceStates.put(depotOrder.getId(), depotOrder.getStatus());

        }

        refundInfo.setMinceStatus(minceStates);

        refundInfo.setRefundGoodses(refundGoodses);

    }

    protected void buildGoodsInfos() {

        List<FollowGoodsInfo> refundGoodses = new ArrayList<FollowGoodsInfo>();

        for (ObjectId goodsId : order.getCountInfos().keySet()) {

            Long count = order.getCountInfos().get(goodsId);

            FollowGoodsInfo refundGoods = buildGoodsInfo(goodsId, count);

            refundGoodses.add(refundGoods);

        }

        refundInfo.setRefundGoodses(refundGoodses);

    }

}
