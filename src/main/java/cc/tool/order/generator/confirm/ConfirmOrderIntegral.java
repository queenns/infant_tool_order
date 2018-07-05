package cc.tool.order.generator.confirm;

import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.confirm.BuilderConfirmIntegral;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.builder.voucher.BuilderVoucherIntegral;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoIntegral;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmOrderIntegral extends ConfirmGenerator {

    public ConfirmOrderIntegral(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    @Override
    protected BuilderConfirm createBuilderConfirm() {

        return new BuilderConfirmIntegral(confirmInfo);

    }

    @Override
    protected BuilderVoucher createBuilderVoucher() {

        return new BuilderVoucherIntegral(order, confirmInfo, operationManager);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        ConfirmInfoIntegral confirmInfo = (ConfirmInfoIntegral) i;

        ParamValidate.isEmpty("customerId", confirmInfo.getCustomerId());

    }

}
