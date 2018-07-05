package cc.tool.order.action;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ModifyType;
import cc.admore.infant.model.order.enums.Status;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.order.ModifyOrderInfo;
import cc.tool.order.operation.OperationManager;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by lxj on 18-6-22
 */
public class ModifyOrderAction implements Action<MakeInfo> {

    private static final Logger logger = LoggerFactory.getLogger(ModifyOrderAction.class);

    private ModifyOrderInfo modifyOrderInfo;

    private OperationManager operationManager;

    public ModifyOrderAction(ModifyOrderInfo modifyOrderInfo, OperationManager operationManager) {

        this.modifyOrderInfo = modifyOrderInfo;

        this.operationManager = operationManager;

    }

    @Override
    public MakeInfo action() {

        MakeInfo makeInfo = new MakeInfo();

        ObjectId orderId = modifyOrderInfo.getOrderId();

        if (!operationManager.lockRedisHandle.lock(orderId.toString())) {

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.LOCK_FAIL.toString());

            return makeInfo;

        }

        Order order = operationManager.masterOrderDao.find(modifyOrderInfo.getOrderId());

        try {

            if (!Status.PAYMENT.equals(order.getStatus())) {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.ORDER_FAIL_STATUS.toString());
                return makeInfo;

            }

            if (order.getPackingMarker()) {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.ORDER_FAIL_PACKING.toString());

                return makeInfo;

            }

            if (order.getLomsPushMarker()) {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.ORDER_FAIL_LOMS_PUSH.toString());

                return makeInfo;

            }

            if (ModifyType.MODIFY.equals(order.getModifyType())) {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.ORDER_FAIL_MODIFY.toString());

                return makeInfo;

            }

            // 更新主订单信息
            Query<Order> queryMaster = operationManager.masterOrderDao.createQuery().field("id").equal(orderId);

            UpdateOperations<Order> updateOperationsMaster = operationManager.masterOrderDao.createUpdateOperations();

            updateOperationsMaster.set("modifyType", ModifyType.MODIFY);

            updateOperationsMaster.set("dress", modifyOrderInfo.getDress());

            if (StringUtils.isEmpty(modifyOrderInfo.getRemark())) {

                updateOperationsMaster.unset("remark");

            } else {

                updateOperationsMaster.set("remark", modifyOrderInfo.getRemark());

            }

            operationManager.masterOrderDao.update(queryMaster, updateOperationsMaster);

            makeInfo.setSuccess(true);

            // 更新拆分订单信息,如果是拆分类型订单
            Field field = order.getClass().getDeclaredField("depotMarker");

            field.setAccessible(true);

            Boolean depotMarker = (Boolean) field.get(order);

            if (!depotMarker) return makeInfo;

            Query<DepotOrder> queryDepot = operationManager.minceOrderDao.createQuery().field("masterId").equal(orderId);

            UpdateOperations<DepotOrder> updateOperationsDepot = operationManager.minceOrderDao.createUpdateOperations();

            updateOperationsDepot.set("modifyType", ModifyType.MODIFY);

            updateOperationsDepot.set("dress", modifyOrderInfo.getDress());

            if (StringUtils.isEmpty(modifyOrderInfo.getRemark())) {

                updateOperationsDepot.unset("remark");

            } else {

                updateOperationsDepot.set("remark", modifyOrderInfo.getRemark());

            }

            operationManager.minceOrderDao.update(queryDepot, updateOperationsDepot);

            return makeInfo;

        } catch (NoSuchFieldException e) {

            logger.info("modify order info ignore orderId[{}] orderType[{}]", orderId, order.getClass().getName());

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.info("modify order info error orderId[{}] orderType[{}]", orderId, order.getClass().getName());

        } finally {

            operationManager.lockRedisHandle.unLock(orderId.toString());

        }

        return makeInfo;

    }

}
