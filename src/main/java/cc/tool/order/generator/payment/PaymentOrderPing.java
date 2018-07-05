package cc.tool.order.generator.payment;

import cc.tool.order.builder.payment.BuilderPaymentPing;
import cc.tool.order.builder.payment.PaymentBuilder;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.model.payment.PaymentInfo;

/**
 * Created by lxj on 18-5-18
 */
public class PaymentOrderPing extends PaymentGenerator {

    public PaymentOrderPing(Boolean mixin, PaymentInfo paymentInfo, OrderFactory orderFactory) {
        super(mixin, paymentInfo, orderFactory);
    }

    @Override
    public PaymentBuilder createPaymentBuilder() {

        return new BuilderPaymentPing(order, paymentInfo, operationManager);

    }

    @Override
    protected void confirm() {

    }

}
