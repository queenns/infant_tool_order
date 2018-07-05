package cc.tool.order.builder.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payment.Payment;
import cc.tool.order.builder.AbstractBuilder;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-21
 * <p>
 * 支付构造器
 */
public abstract class PaymentBuilder<P extends Payment> extends AbstractBuilder<MakeInfo> {

    protected P payment;

    protected Order order;

    protected PaymentInfo paymentInfo;

    protected OperationManager operationManager;

    MakeInfo makeInfo = new MakeInfo();

    public PaymentBuilder(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {

        this.order = order;

        this.paymentInfo = paymentInfo;

        this.operationManager = operationManager;

        initType();

        try {

            this.payment = (P) this.clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

        }

    }

    protected abstract P payment();

    @Override
    public MakeInfo build() {

        Map<String, Object> updates = new HashMap<String, Object>();

        P payment = payment();

        updates.put("payment", payment);
        updates.put("remark", paymentInfo.getRemark());
        updates.put("welfare", paymentInfo.getWelfare());

        operationManager.masterOrderDao.update(order.getId(), updates);

        try {

            Field field = order.getClass().getDeclaredField("depotMarker");

            field.setAccessible(true);

            Boolean depotMarker = (Boolean) field.get(order);

            logger.info("payment depot update success orderId[{}] depotMarker[{}] updates[{}] order type[{}]", order.getId(), depotMarker, updates, order.getClass().getName());

            if (depotMarker)

                operationManager.minceOrderDao.update(order.getId(), updates);

        } catch (NoSuchFieldException e) {

            logger.info("payment depot update ignore orderId[{}] updates[{}] order type[{}]", order.getId(), updates, order.getClass().getName());

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.error("payment depot update error orderId[{}] updates[{}] order type[{}]", order.getId(), updates, order.getClass().getName());

        }

        return makeInfo;

    }

}
