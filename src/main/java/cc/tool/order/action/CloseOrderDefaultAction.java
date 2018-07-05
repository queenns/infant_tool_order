package cc.tool.order.action;

import cc.admore.infant.model.order.Order;
import cc.tool.order.model.MakeInfo;

/**
 * Created by lxj on 18-6-7
 */
public class CloseOrderDefaultAction extends CloseOrderAction {

    public CloseOrderDefaultAction(Order order) {
        super(order);
    }

    @Override
    public MakeInfo action() {

        MakeInfo makeInfo = new MakeInfo();

        makeInfo.setSuccess(true);

        return makeInfo;

    }

}
