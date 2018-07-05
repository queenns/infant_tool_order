package cc.tool.order.builder.voucher;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.MiniOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payInfo.PayVoucher;
import cc.tool.order.builder.AbstractBuilder;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.NumberUtil;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lxj on 18-5-24
 * <p>
 * 支付记录构造器
 */
public abstract class BuilderVoucher extends AbstractBuilder<List<PayVoucher>> {

    /**
     * order
     */
    protected Order order;

    /**
     * 确认订单信息
     */
    protected ConfirmInfo confirmInfo;

    /**
     * 数据操作资源管理器
     */
    protected OperationManager operationManager;

    /**
     * 支付记录
     */
    private List<PayVoucher> vouchers = new ArrayList<PayVoucher>(8);

    public BuilderVoucher(ConfirmInfo confirmInfo, OperationManager operationManager) {

        this.confirmInfo = confirmInfo;

        this.operationManager = operationManager;

        this.order = operationManager.masterOrderDao.find(confirmInfo.getOrderId());

    }

    public BuilderVoucher(Order order, ConfirmInfo confirmInfo, OperationManager operationManager) {

        this.order = order;

        this.confirmInfo = confirmInfo;

        this.operationManager = operationManager;

    }

    /**
     * build
     * 区分是否拆分订单
     * 1.是执行拆分订单构建
     * 2.否执行订单构建
     *
     * @return {@link this#vouchers}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PayVoucher> build() {

        try {

            Field field = order.getClass().getDeclaredField("depotOrderIds");

            field.setAccessible(true);

            buildBrokenOrder((List<ObjectId>) field.get(order));

        } catch (NoSuchFieldException e) {

            buildOrder(order);

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.error("builder voucher broken error orderId[{}] order type[{}]", order.getId(), order.toString());

        }

        return vouchers;

    }

    /**
     * 拆分订单构建
     *
     * @param depotOrderIds depotOrderIds
     */
    private void buildBrokenOrder(List<ObjectId> depotOrderIds) {

        List<DepotOrder> depotOrders = operationManager.minceOrderDao.find(depotOrderIds);

        Order[] orders = new Order[depotOrders.size()];

        depotOrders.toArray(orders);

        buildOrder(orders);

    }

    /**
     * 订单构建
     *
     * @param orders orders
     */
    private void buildOrder(Order... orders) {

        for (Order order : orders) action(order);

    }

    /**
     * 支付信息构建
     * 根据每订单每商品生成每条支付记录
     *
     * @param order order
     */
    protected void action(Order order) {

        for (ObjectId goodsId : order.getCountInfos().keySet()) {

            Long count = order.getCountInfos().get(goodsId);

            action(order, goodsId, count);

        }

    }

    /**
     * 每订单每商品支付记录构建
     * <p>
     * 1.构建公共信息
     * 2.构建支付类型信息
     * 3.构建订单类型及特殊信息
     * 4.构建商品及价格信息
     *
     * @param order   order
     * @param goodsId goodsId
     * @param count   count
     */
    protected void action(Order order, ObjectId goodsId, Long count) {

        PayVoucher voucher = new PayVoucher();

        build(voucher);

        buildType(voucher);

        buildOrder(order, voucher);

        buildGoods(order, goodsId, count, voucher);

        operationManager.paymentClientHandle.sendInfoQueue(voucher);

        logger.info("payment queue voucher : {}", voucher.toString());

        vouchers.add(voucher);

    }

    /**
     * 构建公共信息
     *
     * @param voucher voucher
     * @return voucher
     */
    protected PayVoucher build(PayVoucher voucher) {

        voucher.setOpenId(order.getOpenId());
        voucher.setOrderId(order.getId().toString());
        voucher.setCashFee(order.getActualAmount().intValue());
        voucher.setPayMark(order.getPayment().getPayMark().toString());
        voucher.setChannelFromMark(order.getChannelFromMark().toString());
        voucher.setTimeEnd(order.getPayment().getPaymentDate().toString("yyyyMMddHHmmss"));

        if (order instanceof MiniOrder) voucher.setMiniOpenId(((MiniOrder) order).getMiniOpenId());

        return voucher;

    }

    /**
     * 构建支付类型信息
     *
     * @param voucher voucher
     * @return voucher
     */
    protected abstract PayVoucher buildType(PayVoucher voucher);

    /**
     * 构建订单类型及特殊信息
     *
     * @param order   order
     * @param voucher voucher
     * @return voucher
     */
    private PayVoucher buildOrder(Order order, PayVoucher voucher) {

        voucher.setFromMark(order.getFromMark().toString());

        if (order instanceof DepotOrder) {

            voucher.setDepotOrderId(order.getId().toString());

            voucher.setShopId(((DepotOrder) order).getShopId().toString());

        }

        return voucher;

    }

    /**
     * 构建商品及价格信息
     *
     * @param order   order
     * @param goodsId goodsId
     * @param count   count
     * @param voucher voucher
     * @return voucher
     */
    protected PayVoucher buildGoods(Order order, ObjectId goodsId, Long count, PayVoucher voucher) {

        Long price = order.getOrderGoodsInfos().get(goodsId).getPrice();

        voucher.setGoodsId(goodsId.toString());

        voucher.setUnitPrice(price.intValue());

        voucher.setPurchaseCount(count.intValue());

        voucher.setTotalFee(NumberUtil.multiply(count, price).intValue());

        return voucher;

    }

}
