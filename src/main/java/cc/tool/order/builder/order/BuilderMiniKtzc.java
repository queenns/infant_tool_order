package cc.tool.order.builder.order;

import cc.admore.infant.dao.entity.homePage.SystemSetUp;
import cc.admore.infant.model.order.MiniOrder;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.order.enums.Status;
import cc.admore.infant.model.payment.PaymentFree;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoMiniKtzc;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-14
 */
public class BuilderMiniKtzc extends OrderBuilder<MiniOrder> {

    public BuilderMiniKtzc(OrderGenerator<MiniOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public MiniOrder build() {

        super.build();

        OrderInfoMiniKtzc orderInfo = (OrderInfoMiniKtzc) orderGenerator.orderInfo;

        order.setFromMark(FromMark.MINI);

        order.setMiniOpenId(orderInfo.getMiniOpenId());

        order.setChannelFromMark(ChannelFromMark.WX_PROGRAM_KTZC);

        SystemSetUp systemSetUp = orderGenerator.operationManager.homePageRedisHandle.findSystemSetUp(order.getChannelFromMark(), order.getFromMark());

        order.setExpire(systemSetUp.getExpire());

        return order;

    }

}
