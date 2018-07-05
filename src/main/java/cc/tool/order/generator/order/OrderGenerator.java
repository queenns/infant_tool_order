package cc.tool.order.generator.order;

import cc.admore.infant.model.customer.Customer;
import cc.admore.infant.model.dress.Dress;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.reference.OrderGoodsInfo;
import cc.tool.order.OrderCreateTool;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.builder.order.OrderBuilder;
import cc.tool.order.exception.CreateOrderException;
import cc.tool.order.generator.AbstractGenerator;
import cc.tool.order.model.Info;
import cc.tool.order.model.order.OrderInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;
import cc.tool.order.validate.Validate;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by lxj on 18-4-25
 * <p>
 * 订单生成器
 */
public abstract class OrderGenerator<T extends Order, P, G> extends AbstractGenerator<T> implements OrderCreateTool<T, P, G>, Validate {

    /**
     * 订单
     */
    protected T order;

    /**
     * 接口订单参数
     */
    public OrderInfo orderInfo;

    /**
     * 订单预信息容器
     */
    private OrderBucket<P, G> orderBucket;

    /**
     * 订单生成器
     */
    private OrderBuilder<T> orderBuilder;

    /**
     * 数据操作资源管理器
     */
    public OperationManager operationManager;

    /**
     * 创建订单后是否默认存储数据库
     * 默认:true
     */
    private Boolean keep = true;

    OrderGenerator(OrderInfo orderInfo, OperationManager operationManager) {

        validate(orderInfo);

        this.orderInfo = orderInfo;

        this.operationManager = operationManager;

        this.orderBucket = createOrderBucket();

        this.orderBuilder = createOrderBuilder();

    }

    @Override
    public <I extends Info> void validate(I i) {

        OrderInfo orderInfo = (OrderInfo) i;

        ParamValidate.isEmpty("countInfos", orderInfo.getCountInfos());

    }

    @Override
    public T create() throws CreateOrderException {

        this.order = orderBuilder.build();

        if (keep) saveDataBase();

        pushDeadQueue();

        return this.order;

    }

    @Override
    public Dress findDress() {

        return orderBucket.findDress();

    }

    @Override
    public Long findAmount() {

        return orderBucket.findAmount();

    }

    @Override
    public Long findPostage() {

        return orderBucket.findPostage();

    }

    @Override
    public Long findActualAmount() {

        return orderBucket.findActualAmount();

    }

    @Override
    public Customer findCustomer() {

        return orderBucket.findCustomer();

    }

    @Override
    public Set<String> findGoodsNos() {

        return orderBucket.findGoodsNos();

    }

    @Override
    public Map<ObjectId, P> findGroups() {

        return orderBucket.findGroups();

    }

    @Override
    public Map<ObjectId, G> findGoodses() {

        return orderBucket.findGoodses();

    }

    @Override
    public Map<ObjectId, OrderGoodsInfo> findOrderGoodsInfos() {

        return orderBucket.findOrderGoodsInfos();

    }

    protected abstract OrderBucket<P, G> createOrderBucket();

    protected abstract OrderBuilder<T> createOrderBuilder();

    protected void saveDataBase() {

        operationManager.masterOrderDao.save(this.order);

    }

    protected void pushDeadQueue() {

        if (Objects.equals(order.getExpire(), 0)) return;

        operationManager.orderClientHandle.sendOrderDeadQueue(order.getId().toString(), order.getExpire());

    }

    @Override
    public void enableKeep() {

        this.keep = true;

    }

    @Override
    public void unableKeep() {

        this.keep = false;

    }

    public OrderBucket getOrderBucket() {
        return orderBucket;
    }

    public void setOrderBucket(OrderBucket orderBucket) {
        this.orderBucket = orderBucket;
    }

    public OrderBuilder<T> getOrderBuilder() {
        return orderBuilder;
    }

    public void setOrderBuilder(OrderBuilder<T> orderBuilder) {
        this.orderBuilder = orderBuilder;
    }

}