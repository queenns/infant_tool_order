package cc.tool.order.generator.confirm;

import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.confirm.BuilderConfirmPing;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.builder.voucher.BuilderVoucherPing;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoPing;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmOrderPing extends ConfirmGenerator {

    public ConfirmOrderPing(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    @Override
    protected BuilderConfirm createBuilderConfirm() {

        return new BuilderConfirmPing(confirmInfo);

    }

    @Override
    protected BuilderVoucher createBuilderVoucher() {

        return new BuilderVoucherPing(order, confirmInfo, operationManager);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        ConfirmInfoPing confirmInfo = (ConfirmInfoPing) i;

        ParamValidate.isEmpty("charge", confirmInfo.getCharge());

    }

}
