package cc.tool.order.builder.order;

import cc.admore.infant.dao.entity.homePage.SystemSetUp;
import cc.admore.infant.model.order.MiniOrder;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.generator.order.OrderGenerator;
import cc.tool.order.model.order.OrderInfoMiniDltx;

/**
 * Created by lxj on 18-5-7
 * <p>
 * 小程序订单构造
 * <p>
 * 点亮童星
 */
public class BuilderMiniDltx extends OrderBuilder<MiniOrder> {

    public BuilderMiniDltx(OrderGenerator<MiniOrder, ?, ?> orderGenerator) {
        super(orderGenerator);
    }

    @Override
    public MiniOrder build() {

        super.build();

        OrderInfoMiniDltx orderInfo = (OrderInfoMiniDltx) orderGenerator.orderInfo;

        order.setFromMark(FromMark.MINI);

        order.setMiniOpenId(orderInfo.getMiniOpenId());

        order.setChannelFromMark(ChannelFromMark.WX_PROGRAM_DLTX);

        SystemSetUp systemSetUp = orderGenerator.operationManager.homePageRedisHandle.findSystemSetUp(order.getChannelFromMark(), order.getFromMark());

        order.setExpire(systemSetUp.getExpire());

        return order;

    }

}
