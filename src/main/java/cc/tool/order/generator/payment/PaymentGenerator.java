package cc.tool.order.generator.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.OrderHolderTool;
import cc.tool.order.OrderPaymentTool;
import cc.tool.order.builder.payment.PaymentBuilder;
import cc.tool.order.exception.CreateException;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.Fail;
import cc.tool.order.model.Info;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;
import cc.tool.order.validate.Validate;

/**
 * Created by lxj on 18-5-18
 * <p>
 * 支付生成器
 */
public abstract class PaymentGenerator extends AbstractGenerator<MakeInfo> implements OrderPaymentTool, Validate {

    /**
     * 订单
     */
    public Order order;

    /**
     * 其他事件混合处理
     * 1.库存
     */
    private Boolean mixin = false;

    /**
     * 接口支付参数
     */
    public PaymentInfo paymentInfo;

    /**
     * 订单工厂
     */
    protected OrderFactory orderFactory;

    /**
     * 数据操作资源管理器
     */
    public OperationManager operationManager;

    /**
     * 支付结果
     */
    protected MakeInfo makeInfo = new MakeInfo();

    public PaymentGenerator(Boolean mixin, PaymentInfo paymentInfo, OrderFactory orderFactory) {

        validate(paymentInfo);

        this.mixin = mixin;

        this.paymentInfo = paymentInfo;

        this.orderFactory = orderFactory;

        this.operationManager = orderFactory.operationManager;

    }

    @Override
    public <I extends Info> void validate(I i) {

        PaymentInfo paymentInfo = (PaymentInfo) i;

        ParamValidate.isEmpty(

                new Object[]{"openId", paymentInfo.getOpenId()},

                new Object[]{"orderId", paymentInfo.getOrderId()},

                new Object[]{"payMark", paymentInfo.getPayMark()}

        );

    }

    public abstract PaymentBuilder createPaymentBuilder();

    @Override
    public MakeInfo create() throws CreateException {

        if (!validate()) return makeInfo;

        if (!mixin()) return makeInfo;

        makeInfo = createPaymentBuilder().build();

        if (!makeInfo.success) return makeInfo;

        confirm();

        return makeInfo;

    }

    protected Boolean validate() {

        Status status = order.getStatus();

        logger.info("order status [{}]", status);

        if (Status.EXPIRED.equals(status)) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.ORDER_FAIL_EXPIRE.toString());

            return false;

        }

        if (Status.PAYMENT.equals(status)) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.ORDER_FAIL_PAYMENT.toString());

            return false;

        }

        if (!Status.NONPAYMENT.equals(status)) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.ORDER_FAIL_STATUS.toString());

            return false;

        }

        return true;

    }

    private Boolean mixin() {

        if (!mixin) return true;

        OrderHolderTool orderHolderTool = orderFactory.findOrderHolderTool(paymentInfo.getOrderId());

        MakeInfo makeInfo = orderHolderTool.create();

        if (makeInfo.success) return true;

        this.makeInfo = makeInfo;

        return false;

    }

    protected abstract void confirm();

}
