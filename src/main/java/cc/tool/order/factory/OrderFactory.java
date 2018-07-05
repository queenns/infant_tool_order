package cc.tool.order.factory;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.Payment;
import cc.admore.infant.model.payment.PaymentPing;
import cc.admore.infant.model.payment.PaymentWechat;
import cc.tool.order.*;
import cc.tool.order.action.*;
import cc.tool.order.callback.Callback;
import cc.tool.order.exception.GeneratorException;
import cc.tool.order.generator.broken.BrokenOrder;
import cc.tool.order.generator.confirm.*;
import cc.tool.order.generator.holder.HolderGenerator;
import cc.tool.order.generator.order.*;
import cc.tool.order.generator.payment.*;
import cc.tool.order.generator.refund.*;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.model.confirm.*;
import cc.tool.order.model.order.*;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.model.refund.RefundInfo;
import cc.tool.order.model.refund.RefundInfoOrder;
import cc.tool.order.model.refund.RefundInfoPart;
import cc.tool.order.model.refund.RefundInfoPartDepot;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.proxy.PaymentProxy;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-5-2
 */
@Component
public class OrderFactory {

    /**
     * 操作管理器
     * <p>
     * 将Spring管理的操作bean注入到生成器方便使用
     */
    @Autowired
    public OperationManager operationManager;

    /**
     * 支付构造器映射
     */
    private static Map<PayMark, Class<? extends PaymentGenerator>> paymentMappings;

    /**
     * 订单构造器参数类型映射
     */
    private static Map<Class<? extends OrderGenerator>, Class<? extends OrderInfo>> createMappings;

    /**
     * 确定订单构造参数类型映射
     */
    private static Map<Class<? extends ConfirmGenerator>, Class<? extends ConfirmInfo>> confirmMappings;

    /**
     * 申请售后记录构造器参数类型映射
     */
    private static Map<Class<? extends RefundGenerator>, Class<? extends RefundInfo>> refundMappings;

    static {

        paymentMappings = new HashMap<PayMark, Class<? extends PaymentGenerator>>(8) {{

            put(PayMark.WX, PaymentOrderWechat.class);

            put(PayMark.PING, PaymentOrderPing.class);

            put(PayMark.FREE, PaymentOrderFree.class);

            put(PayMark.MEMBER, PaymentOrderMember.class);

            put(PayMark.INTEGRAL, PaymentOrderIntegral.class);

        }};

        createMappings = new HashMap<Class<? extends OrderGenerator>, Class<? extends OrderInfo>>(12) {{

            put(CreatePublicLuck.class, OrderInfoPublicLuck.class);

            put(CreatePublicPanic.class, OrderInfoPublicPanic.class);

            put(CreatePublicNormal.class, OrderInfoPublicNormal.class);

            put(CreatePublicIntegral.class, OrderInfoPublicIntegral.class);

            put(CreateMiniDltx.class, OrderInfoMiniDltx.class);

            put(CreateMiniQzgc.class, OrderInfoMiniQzgc.class);

            put(CreateMiniKtzc.class, OrderInfoMiniKtzc.class);

            put(CreateDepotPublic.class, OrderInfoDepot.class);

            put(CreateDepotDltx.class, OrderInfoDepot.class);

            put(CreateDepotQzgc.class, OrderInfoDepot.class);

            put(CreateDepotKtzc.class, OrderInfoDepot.class);

        }};

        confirmMappings = new HashMap<Class<? extends ConfirmGenerator>, Class<? extends ConfirmInfo>>(8) {{

            put(ConfirmOrderFree.class, ConfirmInfo.class);

            put(ConfirmOrderPing.class, ConfirmInfoPing.class);

            put(ConfirmOrderWechat.class, ConfirmInfoWechat.class);

            put(ConfirmOrderMember.class, ConfirmInfoMember.class);

            put(ConfirmOrderIntegral.class, ConfirmInfoIntegral.class);

        }};

        refundMappings = new HashMap<Class<? extends RefundGenerator>, Class<? extends RefundInfo>>(4) {{

            put(RefundPart.class, RefundInfoPart.class);

            put(RefundPartDepot.class, RefundInfoPartDepot.class);

            put(RefundOrderPayment.class, RefundInfoOrder.class);

            put(RefundOrderDelivery.class, RefundInfoOrder.class);

        }};

    }

    /**
     * 获取订单生成器
     *
     * @param orderInfo 订单生成必要信息
     *                  Subclasses of {@link OrderInfo}
     *                  {@link cc.tool.order.model.order.OrderInfoDepot}
     *                  {@link cc.tool.order.model.order.OrderInfoMiniDltx}
     *                  {@link cc.tool.order.model.order.OrderInfoMiniQzgc}
     *                  {@link cc.tool.order.model.order.OrderInfoMiniKtzc}
     *                  {@link cc.tool.order.model.order.OrderInfoPublicLuck}
     *                  {@link cc.tool.order.model.order.OrderInfoPublicPanic}
     *                  {@link cc.tool.order.model.order.OrderInfoPublicNormal}
     *                  {@link cc.tool.order.model.order.OrderInfoPublicIntegral}
     * @param clazz     指定订单生成器
     *                  Subclasses of {@link OrderGenerator}
     *                  小程序订单
     *                  {@link cc.tool.order.generator.order.CreateMiniDltx}
     *                  {@link cc.tool.order.generator.order.CreateMiniQzgc}
     *                  {@link cc.tool.order.generator.order.CreateMiniKtzc}
     *                  拆分订单
     *                  {@link cc.tool.order.generator.order.CreateDepotDltx}
     *                  {@link cc.tool.order.generator.order.CreateDepotQzgc}
     *                  {@link cc.tool.order.generator.order.CreateDepotKtzc}
     *                  {@link cc.tool.order.generator.order.CreateDepotPublic}
     *                  公众号订单
     *                  {@link cc.tool.order.generator.order.CreatePublicLuck}
     *                  {@link cc.tool.order.generator.order.CreatePublicPanic}
     *                  {@link cc.tool.order.generator.order.CreatePublicNormal}
     *                  {@link cc.tool.order.generator.order.CreatePublicIntegral}
     * @param <T>       订单类型
     *                  {@link cc.admore.infant.model.order.MiniOrder}
     *                  {@link cc.admore.infant.model.order.LuckOrder}
     *                  {@link cc.admore.infant.model.order.PanicOrder}
     *                  {@link cc.admore.infant.model.order.DepotOrder}
     *                  {@link cc.admore.infant.model.order.NormalOrder}
     *                  {@link cc.admore.infant.model.order.IntegralOrder}
     * @param <P>       商品组
     *                  {@link cc.admore.infant.dao.entity.GoodsGroup}
     *                  {@link cc.admore.infant.dao.entity.NormalGoodsGroup}
     *                  {@link cc.admore.infant.dao.entity.IntegralGoodsGroup}
     * @param <G>       商品
     *                  {@link cc.admore.infant.dao.entity.Goods}
     *                  {@link cc.admore.infant.dao.entity.luckyDraw.LuckyDrawGoods}
     * @return {@link OrderCreateTool}
     */
    public <I extends OrderInfo, T extends Order, P, G> OrderCreateTool<T, P, G> findOrderCreateTool(I orderInfo, Class<? extends OrderGenerator> clazz) {

        match(orderInfo, clazz);

        try {

            Constructor<?> constructor = clazz.getConstructor(OrderInfo.class, OperationManager.class);

            return (OrderCreateTool<T, P, G>) constructor.newInstance(orderInfo, operationManager);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new GeneratorException("OrderCreateTool reflection instantiation error", e);

        }

    }

    /**
     * 获取库存持有生成器
     *
     * @param orderId orderId
     * @return {@link OrderHolderTool}
     */
    public OrderHolderTool findOrderHolderTool(ObjectId orderId) {

        return new HolderGenerator(orderId, this);

    }

    /**
     * 获取支付信息生成器
     *
     * @param paymentInfo 支付参数信息
     * @return {@link OrderPaymentTool}
     */
    public OrderPaymentTool findOrderPaymentTool(PaymentInfo paymentInfo) {

        Class<? extends PaymentGenerator> clazz = match(paymentInfo);

        try {

            Constructor<?> constructor = clazz.getConstructor(Boolean.class, PaymentInfo.class, OrderFactory.class);

            PaymentGenerator paymentGenerator = (PaymentGenerator) constructor.newInstance(false, paymentInfo, this);

            return new PaymentProxy().bind(paymentGenerator);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new GeneratorException("OrderPaymentTool reflection instantiation error", e);

        }

    }

    /**
     * 获取支付信息生成器
     *
     * @param paymentInfo 支付参数信息
     * @param mixin       是否包含其它事件处理
     *                    1.库存持有
     * @return {@link OrderPaymentTool}
     */
    public OrderPaymentTool findOrderPaymentTool(PaymentInfo paymentInfo, Boolean mixin) {

        Class<? extends PaymentGenerator> clazz = match(paymentInfo);

        try {

            Constructor<?> constructor = clazz.getConstructor(Boolean.class, PaymentInfo.class, OrderFactory.class);

            PaymentGenerator paymentGenerator = (PaymentGenerator) constructor.newInstance(mixin, paymentInfo, this);

            return new PaymentProxy().bind(paymentGenerator);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new GeneratorException("OrderPaymentTool reflection instantiation error", e);

        }

    }

    /**
     * 获取拆分信息生成器
     *
     * @param order 拆单订单
     * @return {@link OrderBrokenTool}
     */
    public OrderBrokenTool findOrderBrokenTool(Order order) {

        return new BrokenOrder(order, operationManager);

    }

    /**
     * 获取拆分订单执行器
     *
     * @param order order
     * @return {@link BrokenAction}
     */
    public BrokenAction findBrokenAction(Order order) {

        return new BrokenAction(order, this);

    }

    /**
     * 获取拆分库存持有执行器
     *
     * @param brokenInfos 拆分信息
     * @return {@link HolderBrokenAction}
     */
    public HolderBrokenAction findHolderBrokenAction(Map<ObjectId, BrokenInfo> brokenInfos) {

        return new HolderBrokenAction(brokenInfos, operationManager);

    }


    /**
     * 获取拆分库存持有执行器
     *
     * @param brokenInfos 拆分信息
     * @return {@link HolderBrokenAction}
     */
    public HolderBrokenAction findHolderBrokenAction(Map<ObjectId, BrokenInfo> brokenInfos, final Callback callback) {

        return new HolderBrokenAction(brokenInfos, operationManager) {

            @Override
            public void callback() {

                super.callback();

                callback.callback();

            }

        };

    }

    /**
     * 获取常规商品库存持有执行器
     *
     * @param countInfos 待持有信息{goodsId,count}
     * @return {@link HolderMasterAction}
     */
    public HolderMasterAction findHolderMasterAction(Map<ObjectId, Long> countInfos) {

        return new HolderMasterAction(countInfos, operationManager);

    }

    /**
     * 获取常规商品库存持有执行器
     *
     * @param countInfos 待持有信息{goodsId,count}
     * @param callback   持有成功回调
     * @return {@link HolderMasterAction}
     */
    public HolderMasterAction findHolderMasterAction(Map<ObjectId, Long> countInfos, final Callback callback) {

        return new HolderMasterAction(countInfos, operationManager) {

            @Override
            public void callback() {

                super.callback();

                callback.callback();

            }

        };

    }

    /**
     * 获取关闭订单执行器
     *
     * @param order order
     * @return {@link cc.tool.order.action.CloseOrderPingAction}
     * {@link cc.tool.order.action.CloseOrderWechatAction}
     * {@link cc.tool.order.action.CloseOrderDefaultAction}
     */
    public Action<MakeInfo> findCloseOrderAction(Order order) {

        Payment payment = order.getPayment();

        if (ObjectUtils.isEmpty(payment)) return new CloseOrderDefaultAction(order);

        if (payment instanceof PaymentPing) {

            return new CloseOrderPingAction(order);

        } else if (payment instanceof PaymentWechat) {

            return new CloseOrderWechatAction(order);

        } else {

            return new CloseOrderDefaultAction(order);

        }

    }

    /**
     * 获取支付完成信息生成器
     *
     * @param confirmInfo 支付完成参数信息
     *                    {@link ConfirmInfo}
     *                    {@link ConfirmInfoPing}
     *                    {@link ConfirmInfoWechat}
     *                    {@link ConfirmInfoMember}
     *                    {@link ConfirmInfoIntegral}
     * @param clazz       支付完成生成器
     *                    {@link ConfirmOrderFree}
     *                    {@link ConfirmOrderPing}
     *                    {@link ConfirmOrderWechat}
     *                    {@link ConfirmOrderMember}
     *                    {@link ConfirmOrderIntegral}
     * @return {@link OrderConfirmTool}
     */
    public <I extends ConfirmInfo> OrderConfirmTool findOrderConfirmTool(I confirmInfo, Class<? extends ConfirmGenerator> clazz) {

        match(confirmInfo, clazz);

        try {

            Constructor<?> constructor = clazz.getConstructor(ConfirmInfo.class, OperationManager.class);

            return (OrderConfirmTool) constructor.newInstance(confirmInfo, operationManager);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new GeneratorException("OrderConfirmTool reflection instantiation error", e);

        }

    }

    /**
     * 获取售后申请生成器
     *
     * @param refundInfo 售后申请参数
     *                   {@link RefundInfo}
     *                   {@link RefundInfoPart}
     *                   {@link RefundInfoPartDepot}
     * @param clazz      售后申请生成器
     *                   {@link RefundPart}
     *                   {@link RefundInfoPartDepot}
     *                   {@link RefundOrderPayment}
     *                   {@link RefundOrderDelivery}
     * @return {@link OrderRefundTool}
     */
    public <I extends RefundInfo> OrderRefundTool findOrderRefundTool(I refundInfo, Class<? extends RefundGenerator> clazz) {

        match(refundInfo, clazz);

        try {

            Constructor<?> constructor = clazz.getConstructor(RefundInfo.class, OperationManager.class);

            return (OrderRefundTool) constructor.newInstance(refundInfo, operationManager);

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

            throw new GeneratorException("OrderRefundTool reflection instantiation error", e);

        }

    }

    private void match(OrderInfo orderInfo, Class<? extends OrderGenerator> clazz) {

        Class<? extends OrderInfo> matchClazz = createMappings.get(clazz);

        if (orderInfo.getClass().equals(matchClazz)) return;

        throw new GeneratorException("match error,order generator argument must be [" + matchClazz + "]");

    }

    private void match(ConfirmInfo confirmInfo, Class<? extends ConfirmGenerator> clazz) {

        Class<? extends ConfirmInfo> matchClazz = confirmMappings.get(clazz);

        if (confirmInfo.getClass().equals(matchClazz)) return;

        throw new GeneratorException("match error, confirm generator argument must be [" + matchClazz + "]");

    }

    private Class<? extends PaymentGenerator> match(PaymentInfo paymentInfo) {

        PayMark payMark = paymentInfo.getPayMark();

        if (PayMark.PARTY.equals(paymentInfo.getPayMark())) {

            payMark = operationManager.homePageRedisHandle.findSystemSetUp(paymentInfo.getChannelFromMark(), paymentInfo.getFromMark()).getPayMark();

        }

        return paymentMappings.get(payMark);

    }

    private void match(RefundInfo refundInfo, Class<? extends RefundGenerator> clazz) {

        Class<? extends RefundInfo> matchClazz = refundMappings.get(clazz);

        if (refundInfo.getClass().equals(matchClazz)) return;

        throw new GeneratorException("match error, refund generator argument must be [" + matchClazz + "]");

    }

    /**
     * 获取退款金额执行器
     *
     * @param orderId          orderId
     * @param refundGoodsInfos refundGoodsInfos
     * @return {@link RefundMoneyAction}
     */
    public Action<Double> findRefundMoneyAction(ObjectId orderId, Map<ObjectId, Long> refundGoodsInfos) {

        return new RefundMoneyAction(orderId, refundGoodsInfos, operationManager);

    }

    /**
     * 获取售后绑定物流执行器
     *
     * @param code     code
     * @param trackNo  trackNo
     * @param followId followId
     * @return {@link RefundBindExpressAction}
     */
    public RefundBindExpressAction findRefundBindExpressAction(String code, String trackNo, ObjectId followId) {

        return new RefundBindExpressAction(code, trackNo, followId, operationManager);

    }

    /**
     * 获取订单修改信息执行器
     *
     * @param modifyOrderInfo modifyOrderInfo
     * @return {@link ModifyOrderAction}
     */
    public ModifyOrderAction findModifyOrderAction(ModifyOrderInfo modifyOrderInfo) {

        return new ModifyOrderAction(modifyOrderInfo, operationManager);

    }

}
