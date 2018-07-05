package cc.tool.order.builder.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentFree;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-5-21
 */
public class BuilderPaymentFree extends PaymentBuilder<PaymentFree> {

    public BuilderPaymentFree(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {
        super(order, paymentInfo, operationManager);
    }

    @Override
    protected PaymentFree payment() {

        payment.setPaymentMarker(false);
        payment.setPayMark(PayMark.FREE);

        return payment;

    }

    @Override
    public MakeInfo build() {

        super.build();

        makeInfo.setSuccess(true);
        makeInfo.setMsg(PayMark.FREE.toString());

        return makeInfo;

    }

}
