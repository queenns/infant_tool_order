package cc.tool.order.model.order;

import cc.admore.infant.dao.entity.luckyDraw.LuckDrawCustomer;

/**
 * Created by lxj on 18-5-8
 */
public class OrderInfoPublicLuck extends OrderInfo {

    private LuckDrawCustomer luckDrawCustomer;

    public LuckDrawCustomer getLuckDrawCustomer() {
        return luckDrawCustomer;
    }

    public void setLuckDrawCustomer(LuckDrawCustomer luckDrawCustomer) {
        this.luckDrawCustomer = luckDrawCustomer;
    }

    @Override
    public String toString() {
        return "OrderInfoPublicLuck{" +
                "luckDrawCustomer=" + luckDrawCustomer +
                "} " + super.toString();
    }

}
