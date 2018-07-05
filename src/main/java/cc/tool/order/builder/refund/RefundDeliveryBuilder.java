package cc.tool.order.builder.refund;

import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.generator.refund.RefundGenerator;

/**
 * Created by lxj on 18-6-15
 */
public class RefundDeliveryBuilder extends RefundOrderBuilder {

    public RefundDeliveryBuilder(Order order, RefundGenerator refundGenerator) {
        super(order, refundGenerator);
    }

    @Override
    public FollowOrder build() {

        super.build();

        refundInfo.setStatus(Status.AFTERSALE_AFTER);
        refundInfo.setFollowType(refundGenerator.refundInfo.getFollowType());

        try {

            order.getClass().getDeclaredField("depotMarker");

            buildDepotInfos();

        } catch (NoSuchFieldException e) {

            buildGoodsInfos();

        }

        return refundInfo;

    }

}
