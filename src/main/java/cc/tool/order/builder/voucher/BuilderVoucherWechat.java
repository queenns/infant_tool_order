package cc.tool.order.builder.voucher;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.payInfo.PayVoucher;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoWechat;
import cc.tool.order.operation.OperationManager;

import java.util.Map;

/**
 * Created by lxj on 18-5-24
 */
public class BuilderVoucherWechat extends BuilderVoucher {

    public BuilderVoucherWechat(ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(confirmInfo, operationManager);
    }

    public BuilderVoucherWechat(Order order, ConfirmInfo confirmInfo, OperationManager operationManager) {
        super(order, confirmInfo, operationManager);
    }

    @Override
    protected PayVoucher buildType(PayVoucher voucher) {

        ConfirmInfoWechat confirmInfo = (ConfirmInfoWechat) this.confirmInfo;

        Map<String, Object> wechat = confirmInfo.getWechat();

        voucher.setMchId(wechat.get("mch_id").toString());
        voucher.setFeeType(wechat.get("fee_type").toString());
        voucher.setBankType(wechat.get("bank_type").toString());
        voucher.setDeviceInfo(wechat.get("device_info").toString());
        voucher.setResultCode(wechat.get("result_code").toString());
        voucher.setIsSubscribe(wechat.get("is_subscribe").toString());
        voucher.setTransactionId(wechat.get("transaction_id").toString());

        return voucher;

    }

}
