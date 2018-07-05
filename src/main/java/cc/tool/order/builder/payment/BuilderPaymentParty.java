package cc.tool.order.builder.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.payment.Payment;
import cc.tool.order.generator.payment.PaymentGenerator;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.OrderConfigureUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-5-21
 */
public abstract class BuilderPaymentParty<P extends Payment> extends PaymentBuilder<P> {

    static final Map<ChannelFromMark, String> bodys = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("body"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("bodyDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("bodyQzgc"));

    }};

    public BuilderPaymentParty(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {
        super(order, paymentInfo, operationManager);
    }

}
