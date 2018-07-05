package cc.tool.order.builder.payment;

import cc.admore.infant.dao.entity.finance.MemberPoints;
import cc.admore.infant.dao.mongodb.finance.MemberPointsDao;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentMember;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.NumberUtil;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

/**
 * Created by lxj on 18-5-21
 */
public class BuilderPaymentMember extends PaymentBuilder<PaymentMember> {

    private MemberPoints memberPoints;

    public BuilderPaymentMember(MemberPoints memberPoints, Order order, PaymentInfo paymentInfo, OperationManager operationManager) {

        super(order, paymentInfo, operationManager);

        this.memberPoints = memberPoints;

    }

    @Override
    protected PaymentMember payment() {

        payment.setPaymentMarker(false);
        payment.setPayMark(PayMark.MEMBER);
        payment.setMemberId(memberPoints.getMemberID());

        return payment;

    }

    @Override
    public MakeInfo build() {

        Double delta = NumberUtil.format(order.getActualAmount());

        MemberPointsDao memberPointsDao = operationManager.memberPointsDao;

        Query<MemberPoints> query = memberPointsDao.createQuery().field("openId").equal(paymentInfo.getOpenId());

        UpdateOperations<MemberPoints> updateOperations = memberPointsDao.createUpdateOperations();

        updateOperations.inc("memberConAmount", delta);

        memberPointsDao.update(query, updateOperations);

        logger.info("member payment openId[{}] memberId[{}] delta[{}]", paymentInfo.getOpenId(), memberPoints.getMemberID(), delta);

        super.build();

        makeInfo.setSuccess(true);
        makeInfo.setMsg(PayMark.MEMBER.toString());
        makeInfo.setObj(memberPoints.getMemberID());

        return makeInfo;

    }

}
