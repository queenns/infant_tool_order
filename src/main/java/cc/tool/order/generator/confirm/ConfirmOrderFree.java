package cc.tool.order.generator.confirm;

import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.confirm.BuilderConfirmFree;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.builder.voucher.BuilderVoucherFree;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.operation.OperationManager;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmOrderFree extends ConfirmGenerator {

    public ConfirmOrderFree(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    @Override
    protected BuilderConfirm createBuilderConfirm() {

        return new BuilderConfirmFree(confirmInfo);

    }

    @Override
    protected BuilderVoucher createBuilderVoucher() {

        return new BuilderVoucherFree(order, confirmInfo, operationManager);

    }

}
