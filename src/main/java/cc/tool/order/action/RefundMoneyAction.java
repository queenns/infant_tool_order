package cc.tool.order.action;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.follow.enums.FollowStatus;
import cc.admore.infant.model.follow.reference.FollowGoodsInfo;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.NumberUtil;
import com.google.code.morphia.query.Query;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 */
public class RefundMoneyAction implements Action<Double> {

    private ObjectId orderId;

    private Map<ObjectId, Long> refundGoodsInfos;

    private OperationManager operationManager;

    public RefundMoneyAction(ObjectId orderId, Map<ObjectId, Long> refundGoodsInfos, OperationManager operationManager) {

        this.orderId = orderId;

        this.refundGoodsInfos = refundGoodsInfos;

        this.operationManager = operationManager;

    }

    @Override
    public Double action() {

        Order order = operationManager.masterOrderDao.find(orderId);

        Long refundGoodsMoney = findRefundGoodsMoney(order);

        Boolean overallMarker = overallMarker(orderId, order.getCountInfos());

        return NumberUtil.format(overallMarker ? (NumberUtil.add(refundGoodsMoney, order.getPostage())) : refundGoodsMoney);

    }

    private Long findRefundGoodsMoney(Order order) {

        Map<ObjectId, OrderGoodsInfo> orderGoodsInfos = order.getOrderGoodsInfos();

        Long goodsMoney = 0L;

        for (ObjectId goodsId : refundGoodsInfos.keySet()) {

            Long count = refundGoodsInfos.get(goodsId);

            goodsMoney = goodsMoney + NumberUtil.multiply(orderGoodsInfos.get(goodsId).getPrice(), count);

        }

        return goodsMoney;

    }

    /**
     * 判断商品是否全部退货
     *
     * @param orderId           orderId
     * @param overallGoodsInfos 购买商品个数信息
     * @return {@link Boolean}
     */
    private Boolean overallMarker(ObjectId orderId, Map<ObjectId, Long> overallGoodsInfos) {

        findRefundGoodsInfos(orderId);

        return compare(overallGoodsInfos);

    }

    /**
     * 查询退商品个数
     * 已退个数+执行退个数
     *
     * @param orderId orderId
     */
    private void findRefundGoodsInfos(ObjectId orderId) {

        Query<FollowOrder> query = operationManager.followOrderDao.createQuery();

        query.field("masterId").equal(orderId);
        query.field("followStatus").equal(FollowStatus.REFUNDS);

        List<FollowOrder> followOrders = query.asList();

        if (CollectionUtils.isEmpty(followOrders)) return;

        for (FollowOrder followOrder : followOrders) {

            mergeRefundGoodsInfos(followOrder.getRefundGoodses());

        }

    }

    /**
     * 合并退商品个数
     *
     * @param refundGoodses 退商品信息
     */
    private void mergeRefundGoodsInfos(List<FollowGoodsInfo> refundGoodses) {

        for (FollowGoodsInfo followGoodsInfo : refundGoodses) {

            Long count = followGoodsInfo.getRefundCount();

            ObjectId goodsId = followGoodsInfo.getGoodsId();

            refundGoodsInfos.put(goodsId, ObjectUtils.isEmpty(refundGoodsInfos.get(goodsId)) ? count : (count + refundGoodsInfos.get(goodsId)));

        }

    }

    /**
     * 判断是否全部退货
     * <p>
     * 1.退款商品个数信息不含购买商品则是部分退
     * 2.退款商品个数小于购买商品个数则是部分退
     * 3.所有商品满足上述条件则全部退
     *
     * @param overallGoodsInfos 购买商品个数信息
     * @return {@link Boolean}
     */
    private Boolean compare(Map<ObjectId, Long> overallGoodsInfos) {

        for (ObjectId goodsId : overallGoodsInfos.keySet()) {

            Long refundCount = this.refundGoodsInfos.get(goodsId);

            if (ObjectUtils.isEmpty(refundCount)) return false;

            if (refundCount < overallGoodsInfos.get(goodsId)) return false;

        }

        return true;

    }

}
