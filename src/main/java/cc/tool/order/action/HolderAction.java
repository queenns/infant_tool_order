package cc.tool.order.action;

import cc.admore.infant.dao.entity.enums.OperateResult;
import cc.admore.infant.model.holder.Holder;
import cc.tool.order.callback.Callback;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.operation.OperationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxj on 18-5-25
 * <p>
 * 库存持有执行器
 * 1.抢占库存
 * 2.失败快速返回
 * 3.失败后根据{@link this#holders}异步回写库存
 */
public abstract class HolderAction<H extends Holder> implements Action<MakeInfo>, Callback {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 持有成功记录
     */
    List<H> holders = new ArrayList<H>();

    /**
     * 数据操作资源管理器
     */
    protected OperationManager operationManager;

    /**
     * 结果信息
     */
    protected MakeInfo makeInfo = new MakeInfo();

    /**
     * 订单构造
     *
     * @param operationManager operationManager
     */
    public HolderAction(OperationManager operationManager) {

        this.operationManager = operationManager;

    }

    /**
     * 持有的实际处理
     */
    protected abstract void holder();

    /**
     * action
     *
     * @return {@link this#makeInfo}
     */
    @Override
    public MakeInfo action() {

        holder();

        return makeInfo;

    }

    /**
     * 库存持有成功回调
     */
    @Override
    public void callback() {

    }

    /**
     * 持有失败处理
     */
    protected abstract void fail();

    /**
     * 持有失败时，生成失败结果
     *
     * @param operateResult 持有失败类型
     */
    void holderFail(OperateResult operateResult) {

        switch (operateResult) {

            case FAIL:
                makeFail(Fail.HOLDER_FAIL_ZERO);
                break;

            case UNSAFE_FAIL:
                makeFail(Fail.HOLDER_FAIL_UNSAFE);
                break;

            case GREATER_FAIL:
                makeFail(Fail.HOLDER_FAIL_GREATER);
                break;

            default:
                makeFail(Fail.HOLDER_FAIL_ERROR);
                break;

        }

        fail();

    }

    /**
     * 生成失败结果
     *
     * @param fail {@link Fail#HOLDER_FAIL_ZERO}
     *             {@link Fail#HOLDER_FAIL_ERROR}
     *             {@link Fail#HOLDER_FAIL_UNSAFE}
     *             {@link Fail#HOLDER_FAIL_GREATER}
     * @return {@link this#makeInfo}
     */
    private MakeInfo makeFail(Fail fail) {

        makeInfo.setSuccess(false);

        makeInfo.setMsg(fail.toString());

        return makeInfo;

    }

}
