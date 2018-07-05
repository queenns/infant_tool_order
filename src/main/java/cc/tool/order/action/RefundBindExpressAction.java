package cc.tool.order.action;

import cc.admore.infant.dao.entity.KuaiDiTaskRequest;
import cc.admore.infant.dao.mongodb.util.KuaiDiUtil;
import cc.admore.infant.model.follow.FollowOrder;
import cc.admore.infant.model.follow.enums.FollowStatus;
import cc.admore.infant.model.order.reference.ExpressCompany;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.OrderConfigureUtil;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-6-21
 */
public class RefundBindExpressAction implements Action<MakeInfo> {

    private static final Logger logger = LoggerFactory.getLogger(RefundBindExpressAction.class);

    private String code;

    private String trackNo;

    private ObjectId followId;

    private OperationManager operationManager;

    public RefundBindExpressAction(String code, String trackNo, ObjectId followId, OperationManager operationManager) {

        this.code = code;

        this.trackNo = trackNo;

        this.followId = followId;

        this.operationManager = operationManager;

    }

    @Override
    public MakeInfo action() {

        MakeInfo makeInfo = new MakeInfo();

        if (!operationManager.lockRedisHandle.lock(followId.toString())) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.LOCK_FAIL.toString());

            return makeInfo;

        } else {

            FollowOrder followOrder = operationManager.followOrderDao.createQuery().field("id").equal(followId).get();

            if (!FollowStatus.AGREE.equals(followOrder.getFollowStatus())) {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.REFUND_FAIL_STATUS.toString());

                operationManager.lockRedisHandle.unLock(followId.toString());

                return makeInfo;

            }

            try {

                KuaiDiTaskRequest kdtr = new KuaiDiTaskRequest();

                kdtr.setNumber(trackNo);
                kdtr.setKey(KuaiDiUtil.getKey());
                kdtr.setCompany(code.toLowerCase());

                HashMap<String, String> parameters = new HashMap<String, String>();

                parameters.put("callbackurl", OrderConfigureUtil.getValue("refundExpressCallbackUrl"));
                parameters.put("mobiletelephone", "");
                parameters.put("seller", "");
                parameters.put("commodity", "");

                kdtr.setParameters(parameters);

                operationManager.kuaiDiClientHandle.sendKuaiDiQueue(kdtr);

                logger.info("refund bind express followId[{}] trackNo[{}] code[{}]", followId, trackNo, code);

            } catch (Exception e) {

                e.printStackTrace();

                logger.error("refund bind express error followId[{}] trackNo[{}] code[{}]", followId, trackNo, code);

            } finally {

                ExpressCompany expressCompany = operationManager.expressCompanyDao.createQuery().field("code").equal(code).get();

                Map<String, Object> updates = new HashMap<String, Object>();

                updates.put("trackNo", trackNo);
                updates.put("trackTime", new DateTime());
                updates.put("expressCompany", expressCompany);
                updates.put("followStatus", FollowStatus.DELIVERED);

                operationManager.followOrderDao.update(followId, updates);

                logger.info("refund bind express success update[{}]", updates);

                operationManager.lockRedisHandle.unLock(followId.toString());

            }

            makeInfo.setSuccess(true);

            return makeInfo;

        }

    }

}
