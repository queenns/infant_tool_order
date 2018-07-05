package cc.tool.order.proxy;

import cc.tool.order.OrderPaymentTool;
import cc.tool.order.generator.payment.PaymentGenerator;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lxj on 18-5-18
 */
public class PaymentProxy implements InvocationHandler {

    /**
     * 代理对象
     */
    private PaymentGenerator target;

    public OrderPaymentTool bind(PaymentGenerator target) {

        this.target = target;

        return (OrderPaymentTool) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{OrderPaymentTool.class}, this);

    }

    @Override
    public MakeInfo invoke(Object proxy, Method method, Object[] args) throws Throwable {

        ObjectId orderId = target.paymentInfo.getOrderId();

        OperationManager operationManager = target.operationManager;

        MakeInfo makeInfo;

        if (operationManager.lockRedisHandle.lock(orderId.toString())) {

            target.order = operationManager.masterOrderDao.find(orderId);

            makeInfo = (MakeInfo) method.invoke(target, args);

            operationManager.lockRedisHandle.unLock(orderId.toString());

        } else {

            makeInfo = new MakeInfo(false, Fail.LOCK_FAIL.toString());

        }

        return makeInfo;

    }

}
