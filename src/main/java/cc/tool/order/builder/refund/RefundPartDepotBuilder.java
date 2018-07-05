package cc.tool.order.builder.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.follow.reference.FollowGoodsInfo;
import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.generator.refund.RefundGenerator;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-19
 */
public class RefundPartDepotBuilder extends RefundBuilder {

    private DepotOrder depotOrder;

    public RefundPartDepotBuilder(DepotOrder depotOrder, Order order, RefundGenerator refundGenerator) {

        super(order, refundGenerator);

        this.depotOrder = depotOrder;

    }

    @Override
    public FollowOrder build() {

        super.build();

        refundInfo.setStatus(Status.AFTERSALE_AFTER);
        refundInfo.setFollowType(refundGenerator.refundInfo.getFollowType());

        Map<ObjectId, Status> minceStates = new HashMap<ObjectId, Status>();

        minceStates.put(depotOrder.getId(),depotOrder.getStatus());

        refundInfo.setMinceStatus(minceStates);

        List<FollowGoodsInfo> refundGoodses = new ArrayList<FollowGoodsInfo>();

        Map<ObjectId, Long> refundGoodsInfos = refundGenerator.refundInfo.getRefundGoodsInfos();

        for (ObjectId goodsId : refundGoodsInfos.keySet()) {

            FollowGoodsInfo refundGoodsInfo = buildDepotGoodsInfo(depotOrder, goodsId, refundGoodsInfos.get(goodsId));

            refundGoodses.add(refundGoodsInfo);

        }

        refundInfo.setRefundGoodses(refundGoodses);

        return refundInfo;

    }

}
