package cc.tool.order.generator.payment;

import cc.admore.infant.dao.entity.finance.MemberPoints;
import cc.tool.order.OrderConfirmTool;
import cc.tool.order.builder.payment.BuilderPaymentMember;
import cc.tool.order.builder.payment.PaymentBuilder;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.generator.confirm.ConfirmOrderMember;
import cc.tool.order.model.Fail;
import cc.tool.order.model.confirm.ConfirmInfoMember;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.util.NumberUtil;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Created by lxj on 18-5-18
 */
public class PaymentOrderMember extends PaymentGenerator {

    private MemberPoints memberPoints;

    public PaymentOrderMember(Boolean mixin, PaymentInfo paymentInfo, OrderFactory orderFactory) {
        super(mixin, paymentInfo, orderFactory);
    }

    @Override
    public PaymentBuilder createPaymentBuilder() {

        return new BuilderPaymentMember(memberPoints, order, paymentInfo, operationManager);

    }

    @Override
    protected Boolean validate() {

        Boolean validate = super.validate();

        if (!validate) return false;

        memberPoints = operationManager.memberPointsDao.createQuery().field("openId").equal(paymentInfo.getOpenId()).get();

        boolean memberMarker = !ObjectUtils.isEmpty(memberPoints) && !StringUtils.isEmpty(memberPoints.getMemberID());

        if (!memberMarker) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.MEMBER_FAIL_MARKER.toString());

            return false;

        }

        double balance = memberPoints.getMemberAmount() - memberPoints.getMemberConAmount();

        if (order.getActualAmount() > NumberUtil.format(balance)) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.MEMBER_FAIL_BALANCE.toString());

            return false;

        }

        return true;

    }

    @Override
    protected void confirm() {

        ConfirmInfoMember confirmInfo = new ConfirmInfoMember();

        confirmInfo.setOrderId(order.getId());
        confirmInfo.setMemberId(memberPoints.getMemberID());

        OrderConfirmTool orderConfirmTool = orderFactory.findOrderConfirmTool(confirmInfo, ConfirmOrderMember.class);

        orderConfirmTool.create();

    }

    public MemberPoints getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(MemberPoints memberPoints) {
        this.memberPoints = memberPoints;
    }

}
