package cc.tool.order.builder.order;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoDepot;

/**
 * Created by lxj on 18-5-14
 * <p>
 * 拆分订单构造
 * <p>
 * 看图找茬
 */
public class BuilderDepotKtzc extends BuilderDepot {

    public BuilderDepotKtzc(OrderGenerator<DepotOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public DepotOrder build() {

        super.build();

        order.setFromMark(FromMark.MINI);

        return order;

    }

}
