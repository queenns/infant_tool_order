package cc.tool.order.builder.voucher;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payInfo.PayVoucher;
import cc.admore.infant.model.payment.PaymentMember;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-5-24
 */
public class BuilderVoucherMember extends BuilderVoucher {

    public BuilderVoucherMember(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    public BuilderVoucherMember(Order order, ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(order, confirmInfo, operationManager);
    }

    @Override
    protected PayVoucher buildType(PayVoucher voucher) {

        PaymentMember payment = (PaymentMember) order.getPayment();

        voucher.setMemberId(payment.getMemberId());

        return voucher;

    }

}
