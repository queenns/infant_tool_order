package cc.tool.order.builder.payment;

import cc.admore.infant.dao.entity.finance.MemberPoints;
import cc.admore.infant.dao.mongodb.finance.MemberPointsDao;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentIntegral;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.NumberUtil;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by lxj on 18-5-21
 */
public class BuilderPaymentIntegral extends PaymentBuilder<PaymentIntegral> {

    public BuilderPaymentIntegral(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {
        super(order, paymentInfo, operationManager);
    }

    @Override
    protected PaymentIntegral payment() {

        payment.setPaymentMarker(false);
        payment.setPayMark(PayMark.INTEGRAL);
        payment.setCustomerId(order.getCustomer().getId());

        return payment;

    }

    @Override
    public MakeInfo build() {

        Double delta = NumberUtil.format(order.getActualAmount());

        MemberPointsDao memberPointsDao = operationManager.memberPointsDao;

        Query<MemberPoints> query = memberPointsDao.createQuery().field("openId").equal(paymentInfo.getOpenId());

        UpdateOperations<MemberPoints> updateOperations = memberPointsDao.createUpdateOperations();

        updateOperations.inc("integralConAmount", delta);

        memberPointsDao.update(query, updateOperations);

        logger.info("integral payment openId[{}] delta[{}]", paymentInfo.getOpenId(), delta);

        super.build();

        MemberPoints memberPoints = memberPointsDao.createQuery().field("openId").equal(paymentInfo.getOpenId()).get();

        final Double balance = memberPoints.getIntegralAmount() - memberPoints.getIntegralConAmount();

        makeInfo.setSuccess(true);
        makeInfo.setMsg(PayMark.INTEGRAL.toString());
        makeInfo.setObj(new HashMap<String,String>(){{

            put("consume",String.valueOf(new DecimalFormat("#").format(NumberUtil.format(order.getActualAmount()))));

            put("balance",String.valueOf(new DecimalFormat("#").format(balance)));

        }});

        return makeInfo;

    }

}
