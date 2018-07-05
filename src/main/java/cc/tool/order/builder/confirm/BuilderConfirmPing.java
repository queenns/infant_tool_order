package cc.tool.order.builder.confirm;

import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentPing;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoPing;
import com.pingplusplus.model.Charge;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-22
 */
public class BuilderConfirmPing extends BuilderConfirm<PaymentPing> {

    public BuilderConfirmPing(ConfirmInfo confirmInfo) {
        super(confirmInfo);
    }

    @Override
    public PaymentPing build() {

        Charge charge = ((ConfirmInfoPing) this.confirmInfo).getCharge();

        payment.setPaymentMarker(true);

        payment.setPayMark(PayMark.PING);

        payment.setPaymentDate(new DateTime(charge.getTimePaid() * 1000));

        payment.setChargeId(charge.getId());

        return payment;

    }

}
