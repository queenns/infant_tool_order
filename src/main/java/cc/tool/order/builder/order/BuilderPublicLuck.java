package cc.tool.order.builder.order;

import cc.admore.infant.model.order.LuckOrder;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.order.enums.Status;
import cc.admore.infant.model.payment.PaymentFree;
import cc.tool.order.generator.order.OrderGenerator;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-8
 * <p>
 * 公众号订单构造
 * <p>
 * 抽奖订单
 */
public class BuilderPublicLuck extends OrderBuilder<LuckOrder> {

    public BuilderPublicLuck(OrderGenerator<LuckOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public LuckOrder build() {

        super.build();

        order.setFromMark(FromMark.LUCKYDRAW);

        order.setChannelFromMark(ChannelFromMark.WX_PUBLIC_ENSJ);

        return order;

    }

}
