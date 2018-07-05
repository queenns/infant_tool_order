package cc.tool.order.builder.confirm;

import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentIntegral;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoIntegral;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-22
 */
public class BuilderConfirmIntegral extends BuilderConfirm<PaymentIntegral> {

    public BuilderConfirmIntegral(ConfirmInfo confirmInfo) {
        super(confirmInfo);
    }

    @Override
    public PaymentIntegral build() {

        ConfirmInfoIntegral confirmInfo = (ConfirmInfoIntegral) this.confirmInfo;

        payment.setPaymentMarker(true);

        payment.setPayMark(PayMark.INTEGRAL);

        payment.setPaymentDate(new DateTime());

        payment.setCustomerId(confirmInfo.getCustomerId());

        return payment;

    }

}
