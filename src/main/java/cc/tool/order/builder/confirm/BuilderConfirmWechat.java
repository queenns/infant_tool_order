package cc.tool.order.builder.confirm;

import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentWechat;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoWechat;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by lxj on 18-5-22
 */
public class BuilderConfirmWechat extends BuilderConfirm<PaymentWechat> {


    public BuilderConfirmWechat(ConfirmInfo confirmInfo) {
        super(confirmInfo);
    }

    @Override
    public PaymentWechat build() {

        ConfirmInfoWechat confirmInfo = (ConfirmInfoWechat) this.confirmInfo;

        payment.setPaymentMarker(true);

        payment.setPayMark(PayMark.WX);

        payment.setPaymentDate(DateTimeFormat.forPattern("yyyyMMddHHmmss").parseDateTime(confirmInfo.getWechat().get("time_end").toString()));

        return payment;

    }

}
