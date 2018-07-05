package cc.tool.order.strategy.postage;

import cc.admore.infant.dao.entity.goods.PublicGoodsInfo;
import cc.tool.order.bucket.order.OrderBucket;
import cc.tool.order.util.NumberUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by lxj on 18-5-2
 */
@Component
public class DefaultPostage extends AbstractPostage {

    @Override
    public Long calculate(OrderBucket orderBucket) {

        Map groups = orderBucket.findGroups();

        PublicGoodsInfo publicGoodsInfo = (PublicGoodsInfo) Collections.min(groups.values(), new Comparator<PublicGoodsInfo>() {

            @Override
            public int compare(PublicGoodsInfo o1, PublicGoodsInfo o2) {

                Long x = NumberUtil.format(o1.getPostage());

                Long y = NumberUtil.format(o2.getPostage());

                Long subtract = NumberUtil.subtract(x, y);

                return (subtract == 0) ? 0 : (subtract > 0 ? 1 : -1);

            }

        });

        return NumberUtil.format(publicGoodsInfo.getPostage());

    }

}
