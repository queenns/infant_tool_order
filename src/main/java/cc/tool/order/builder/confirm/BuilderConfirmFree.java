package cc.tool.order.builder.confirm;

import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentFree;
import cc.tool.order.model.confirm.ConfirmInfo;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-22
 */
public class BuilderConfirmFree extends BuilderConfirm <PaymentFree>{

    public BuilderConfirmFree(ConfirmInfo confirmInfo) {
        super(confirmInfo);
    }

    @Override
    public PaymentFree build() {

        payment.setPaymentMarker(true);

        payment.setPayMark(PayMark.FREE);

        payment.setPaymentDate(new DateTime());

        return payment;

    }

}
