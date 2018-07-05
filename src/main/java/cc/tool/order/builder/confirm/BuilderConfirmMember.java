package cc.tool.order.builder.confirm;

import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentMember;
import cc.tool.order.model.confirm.ConfirmInfo;
import cc.tool.order.model.confirm.ConfirmInfoMember;
import org.joda.time.DateTime;

/**
 * Created by lxj on 18-5-22
 */
public class BuilderConfirmMember extends BuilderConfirm<PaymentMember> {

    public BuilderConfirmMember(ConfirmInfo confirmInfo) {
        super(confirmInfo);
    }

    @Override
    public PaymentMember build() {

        ConfirmInfoMember confirmInfo = (ConfirmInfoMember) this.confirmInfo;

        payment.setPaymentMarker(true);

        payment.setPayMark(PayMark.MEMBER);

        payment.setPaymentDate(new DateTime());

        payment.setMemberId(confirmInfo.getMemberId());

        return payment;

    }

}
