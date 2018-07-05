package cc.tool.order.generator.confirm;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.OrderConfirmTool;
import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.exception.CreateException;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;
import cc.tool.order.validate.Validate;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-22
 * <p>
 * 支付完成生成器
 */
public abstract class ConfirmGenerator extends AbstractGenerator<Order> implements OrderConfirmTool, Validate {

    /**
     * 订单
     */
    public Order order;

    /**
     * 支付完成参数
     */
    protected ConfirmInfo confirmInfo;

    /**
     * 支付完成构造器
     */
    protected BuilderConfirm builderConfirm;

    /**
     * 数据操作资源管理器
     */
    public OperationManager operationManager;

    public ConfirmGenerator(ConfirmInfo confirmInfo, OperationManager operationManager) {

        validate(confirmInfo);

        this.confirmInfo = confirmInfo;

        this.operationManager = operationManager;

        this.builderConfirm = createBuilderConfirm();

    }

    /**
     * 支付完成信息构造器
     *
     * @return {@link BuilderConfirm}
     */
    protected abstract BuilderConfirm createBuilderConfirm();

    /**
     * 支付记录信息构造器
     *
     * @return {@link BuilderVoucher}
     */
    protected abstract BuilderVoucher createBuilderVoucher();

    /**
     * 验证参数
     *
     * @param i   i
     * @param <I> <I>
     */
    @Override
    public <I extends Info> void validate(I i) {

        ConfirmInfo confirmInfo = (ConfirmInfo) i;

        ParamValidate.isEmpty("orderId", confirmInfo.getOrderId());

    }

    /**
     * 1.更新总单信息
     * 2.刷新支付完成{@link Order}
     * 3.更新拆分订单信息，如果含有拆分订单的话
     * 4.生成支付记录
     *
     * @return {@link Order}
     * @throws CreateException 当支付完成构造器执行中有异常时抛出
     */
    @Override
    public Order create() throws CreateException {

        orderUpdate();

        refresh();

        brokenOrderUpdate();

        operationManager.paymentClientHandle.sendSyncQueue(order.getId().toString());

        createBuilderVoucher().build();

        return this.order;

    }

    /**
     * 刷新最新的Order信息{@link this#order}
     */
    protected void refresh() {

        this.order = operationManager.masterOrderDao.find(confirmInfo.getOrderId());

    }

    /**
     * 更新总单的支付完成信息
     */
    private void orderUpdate() {

        Map<String, Object> updates = new HashMap<String, Object>();

        updates.put("status", Status.PAYMENT);
        updates.put("payment", builderConfirm.build());

        operationManager.masterOrderDao.update(confirmInfo.getOrderId(), updates);

    }

    /**
     * 是否是拆分类型订单，如果是开始更新
     */
    @SuppressWarnings("unchecked")
    private void brokenOrderUpdate() {

        try {

            Field field = order.getClass().getDeclaredField("depotOrderIds");

            field.setAccessible(true);

            brokensUpdate((List<ObjectId>) field.get(order));

        } catch (NoSuchFieldException e) {

            logger.info("confirm broken update ignore orderId[{}] order type[{}]",order.getId(), order.getClass().getName());

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.error("confirm broken update error orderId[{}] order type[{}]", order.getId(), order.toString());

        }

    }

    /**
     * 更新拆分订单支付完成信息
     *
     * @param depotOrderIds 　depotOrderIds
     */
    private void brokensUpdate(List<ObjectId> depotOrderIds) {

        Map<String, Object> updates = new HashMap<String, Object>();

        updates.put("status", Status.PAYMENT);
        updates.put("dress", order.getDress());
        updates.put("remark", order.getRemark());
        updates.put("welfare", order.getWelfare());
        updates.put("payment", order.getPayment());

        operationManager.minceOrderDao.update(depotOrderIds, updates);

    }

}
