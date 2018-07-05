package cc.tool.order.generator.payment;

import cc.tool.order.OrderConfirmTool;
import cc.tool.order.builder.payment.BuilderPaymentFree;
import cc.tool.order.builder.payment.PaymentBuilder;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.generator.confirm.ConfirmOrderFree;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.payment.PaymentInfo;

/**
 * Created by lxj on 18-5-21
 */
public class PaymentOrderFree extends PaymentGenerator {

    public PaymentOrderFree(Boolean mixin, PaymentInfo paymentInfo, OrderFactory orderFactory) {
        super(mixin, paymentInfo, orderFactory);
    }

    @Override
    public PaymentBuilder createPaymentBuilder() {

        return new BuilderPaymentFree(order, paymentInfo, operationManager);

    }

    @Override
    protected Boolean validate() {

        return super.validate();

    }

    @Override
    protected void confirm() {

        ConfirmInfo confirmInfo = new ConfirmInfo();

        confirmInfo.setOrderId(order.getId());

        OrderConfirmTool orderConfirmTool = orderFactory.findOrderConfirmTool(confirmInfo, ConfirmOrderFree.class);

        orderConfirmTool.create();

    }

}
