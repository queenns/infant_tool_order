package cc.tool.order.builder.order;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.generator.order.OrderGenerator;

/**
 * Created by lxj on 18-5-14
 * <p>
 * 拆分订单构造
 * <p>
 * 点亮童星
 */
public class BuilderDepotDltx extends BuilderDepot {

    public BuilderDepotDltx(OrderGenerator<DepotOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public DepotOrder build() {

        super.build();

        order.setFromMark(FromMark.MINI);

        return order;

    }

}
