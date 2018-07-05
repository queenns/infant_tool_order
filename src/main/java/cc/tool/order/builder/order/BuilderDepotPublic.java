package cc.tool.order.builder.order;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.generator.order.OrderGenerator;

/**
 * Created by lxj on 18-5-14
 * <p>
 * 拆分订单构造
 * <p>
 * 公众号
 */
public class BuilderDepotPublic extends BuilderDepot {

    public BuilderDepotPublic(OrderGenerator<DepotOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public DepotOrder build() {

        super.build();

        order.setFromMark(FromMark.NORMAL);

        return order;

    }

}
