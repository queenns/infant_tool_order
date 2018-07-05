package cc.tool.order.generator.payment;

import cc.tool.order.builder.payment.BuilderPaymentWechat;
import cc.tool.order.builder.payment.PaymentBuilder;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.model.payment.PaymentInfo;

/**
 * Created by lxj on 18-5-18
 */
public class PaymentOrderWechat extends PaymentGenerator {

    public PaymentOrderWechat(Boolean mixin, PaymentInfo paymentInfo, OrderFactory orderFactory) {
        super(mixin, paymentInfo, orderFactory);
    }

    @Override
    public PaymentBuilder createPaymentBuilder() {

        return new BuilderPaymentWechat(order, paymentInfo, operationManager);

    }

    @Override
    protected void confirm() {

    }

}
