package cc.tool.order.generator.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.builder.refund.RefundBuilder;
import cc.tool.order.builder.refund.RefundDeliveryBuilder;
import cc.tool.order.exception.CreateException;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-6-15
 */
public class RefundOrderDelivery extends RefundOrder {

    public RefundOrderDelivery(RefundInfo refundInfo, OperationManager operationManager) {
        super(refundInfo, operationManager);
    }

    @Override
    public RefundBuilder createRefundBuilder() {

        return new RefundDeliveryBuilder(order, this);

    }

    @Override
    protected Boolean validate() {

        if (!Status.SIGNFOR.equals(order.getStatus())) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.REFUND_FAIL_STATUS.toString());

            return false;

        }

        return true;

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateOrderRefundInfo(FollowOrder followOrder) {

        Map<String, Object> updates = new HashMap<String, Object>();

        updates.put("followId", followOrder.getId());
        updates.put("status", Status.AFTERSALE_AFTER);

        operationManager.masterOrderDao.update(order.getId(), updates);

        try {

            Field field = order.getClass().getDeclaredField("depotOrderIds");

            field.setAccessible(true);

            List<ObjectId> depotOrderIds = (List<ObjectId>) field.get(order);

            if (ObjectUtils.isEmpty(depotOrderIds)) return;

            operationManager.minceOrderDao.update(depotOrderIds, "status", Status.AFTERSALE_AFTER);

        } catch (NoSuchFieldException e) {

            logger.info("refund update status not depot order ids orderId[{}] order type[{}]", order.getId(), order.getClass().getName());

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.error("refund update status error orderId[{}]", order.getId());

        }

    }

}
