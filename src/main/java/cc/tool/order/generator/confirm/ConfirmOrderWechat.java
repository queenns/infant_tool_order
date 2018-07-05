package cc.tool.order.generator.confirm;

import cc.tool.order.builder.confirm.BuilderConfirm;
import cc.tool.order.builder.confirm.BuilderConfirmWechat;
import cc.tool.order.builder.voucher.BuilderVoucher;
import cc.tool.order.builder.voucher.BuilderVoucherWechat;
import cc.tool.order.model.Info;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoWechat;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.validate.ParamValidate;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmOrderWechat extends ConfirmGenerator {

    public ConfirmOrderWechat(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    @Override
    protected BuilderConfirm createBuilderConfirm() {

        return new BuilderConfirmWechat(confirmInfo);

    }

    @Override
    protected BuilderVoucher createBuilderVoucher() {

        return new BuilderVoucherWechat(order, confirmInfo, operationManager);

    }

    @Override
    public <I extends Info> void validate(I i) {

        super.validate(i);

        ConfirmInfoWechat confirmInfo = (ConfirmInfoWechat) i;

        ParamValidate.isEmpty("wechat", confirmInfo.getWechat());

    }

}
