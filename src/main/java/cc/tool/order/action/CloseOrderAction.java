package cc.tool.order.action;

import cc.admore.infant.model.order.Order;
import cc.tool.order.model.MakeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lxj on 18-6-7
 */
public abstract class CloseOrderAction implements Action<MakeInfo> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected Order order;

    public CloseOrderAction(Order order) {

        this.order = order;

    }

}
