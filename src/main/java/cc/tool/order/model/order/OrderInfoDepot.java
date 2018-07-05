package cc.tool.order.model.order;

import cc.admore.infant.model.order.Order;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by lxj on 18-5-11
 */
public class OrderInfoDepot extends OrderInfo {

    private Order order;

    private ObjectId shopId;

    public OrderInfoDepot() {
    }

    public OrderInfoDepot(Order order, ObjectId shopId, Map<ObjectId, Long> countInfos) {

        this.order = order;

        this.shopId = shopId;

        setCountInfos(countInfos);

        setOpenId(order.getOpenId());

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ObjectId getShopId() {
        return shopId;
    }

    public void setShopId(ObjectId shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "OrderInfoDepot{" +
                "order=" + order +
                ", shopId=" + shopId +
                "} " + super.toString();
    }

}
