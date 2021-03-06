package cc.tool.order.builder.voucher;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payInfo.PayVoucher;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-5-24
 */
public class BuilderVoucherPing extends BuilderVoucher {

    public BuilderVoucherPing(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    public BuilderVoucherPing(Order order, ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(order, confirmInfo, operationManager);
    }

    @Override
    protected PayVoucher buildType(PayVoucher voucher) {

        return voucher;

    }

}
