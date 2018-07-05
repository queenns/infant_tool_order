package cc.tool.order.generator.confirm;

import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.confirm.BuilderConfirmMember;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.builder.voucher.BuilderVoucherMember;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoMember;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmOrderMember extends ConfirmGenerator {

    public ConfirmOrderMember(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    @Override
    protected BuilderConfirm createBuilderConfirm() {

        return new BuilderConfirmMember(confirmInfo);

    }

    @Override
    protected BuilderVoucher createBuilderVoucher() {

        return new BuilderVoucherMember(order, confirmInfo, operationManager);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        ConfirmInfoMember confirmInfo = (ConfirmInfoMember) i;

        ParamValidate.isEmpty("memberId", confirmInfo.getMemberId());

    }

}
