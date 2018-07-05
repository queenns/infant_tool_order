package cc.tool.order.action;

import cc.admore.infant.model.order.DepotOrder;
import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.FromMark;
import cc.tool.order.OrderBrokenTool;
import cc.tool.order.OrderCreateTool;
import cc.tool.order.callback.Callback;
import cc.tool.order.exception.CreateException;
import cc.tool.order.factory.OrderFactory;
import cc.tool.order.generator.order.*;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.broken.BrokenInfo;
import cc.tool.order.model.order.OrderInfoDepot;
import cc.tool.order.operation.OperationManager;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxj on 18-5-24
 * <p>
 * 拆分订单执行器
 */
public class BrokenAction implements Action<MakeInfo> {

    private static final Logger logger = LoggerFactory.getLogger(BrokenAction.class);

    /**
     * 订单
     */
    private Order order;

    /**
     * 订单工厂
     */
    private OrderFactory orderFactory;

    /**
     * 数据操作资源管理器
     */
    private OperationManager operationManager;

    public BrokenAction(Order order, OrderFactory orderFactory) {

        this.order = order;

        this.orderFactory = orderFactory;

        this.operationManager = orderFactory.operationManager;

    }

    /**
     * action
     * 1.判断是否拆分订单,如果不是忽略直接返回
     * 2.是拆分订单类型时，执行拆分
     *
     * @return {@link MakeInfo}
     */
    @Override
    public MakeInfo action() {

        try {

            Field field = order.getClass().getDeclaredField("depotMarker");

            field.setAccessible(true);

            Boolean depotMarker = (Boolean) field.get(order);

            return actionBroken(depotMarker);

        } catch (NoSuchFieldException e) {

            operationManager.orderClientHandle.sendBuildHolderMaster(order.getId().toString());

            operationManager.masterOrderDao.update(order.getId(), "holdMarker", true);

            logger.info("broken ignore type[{}]", order.getClass().getName());

            return new MakeInfo(true);

        } catch (IllegalAccessException e) {

            e.printStackTrace();

            logger.error("broken error orderId[{}] order type[{}]", order.getId(), order.getClass().getName());

            MakeInfo makeInfo = new MakeInfo();

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.ORDER_FAIL_BROKEN.toString());

            return makeInfo;

        }

    }

    /**
     * 执行拆分
     * 1.判断是否拆分，拆分完成直接返回
     * 2.获取订单拆分信息生成器，生成拆分信息
     * 3.获取拆分库存持有执行器,执行持有库存，失败直接返回
     * 4.根据订单类型及渠道类型获取获取订单生成器生成拆分订单
     * 5.拆分完成更新主单拆分信息
     *
     * @param depotMarker 　是否已经拆分
     * @return {@link MakeInfo}
     */
    private MakeInfo actionBroken(Boolean depotMarker) {

        logger.info("broken necessary depotMarker[{}] order type[{}]", depotMarker, order.getClass().getName());

        MakeInfo makeInfo = new MakeInfo(depotMarker);

        if (makeInfo.success) return makeInfo;

        try {

            OrderBrokenTool orderBrokenTool = orderFactory.findOrderBrokenTool(order);

            final Map<ObjectId, BrokenInfo> brokenInfos = orderBrokenTool.create();

            return orderFactory.findHolderBrokenAction(brokenInfos, new Callback() {

                @Override
                public void callback() {

                    operationManager.orderClientHandle.sendBuildHolderMaster(order.getId().toString());

                    Class<? extends GeneratorDepot> clazz = depotOrderTypes.get(findTypeKey(order.getFromMark(), order.getChannelFromMark()));

                    List<ObjectId> depotOrderIds = new ArrayList<ObjectId>();

                    for (BrokenInfo brokenInfo : brokenInfos.values()) {

                        OrderInfoDepot orderInfoDepot = new OrderInfoDepot();

                        orderInfoDepot.setOrder(order);
                        orderInfoDepot.setOpenId(order.getOpenId());
                        orderInfoDepot.setShopId(brokenInfo.getShopId());
                        orderInfoDepot.setCountInfos(brokenInfo.getCountInfos());

                        OrderCreateTool<DepotOrder, ?, ?> orderCreateTool = orderFactory.findOrderCreateTool(orderInfoDepot, clazz);

                        DepotOrder depotOrder = orderCreateTool.create();

                        operationManager.orderClientHandle.sendBuildHolderBroken(depotOrder.getId().toString());

                        depotOrderIds.add(depotOrder.getId());

                    }

                    Map<String, Object> updates = new HashMap<String, Object>();

                    updates.put("holdMarker", true);
                    updates.put("depotMarker", true);
                    updates.put("depotOrderIds", depotOrderIds);

                    operationManager.masterOrderDao.update(order.getId(), updates, false);

                }

            }).action();

        } catch (CreateException e) {

            e.printStackTrace();

            makeInfo.setMsg(Fail.ORDER_FAIL_BROKEN.toString());

            return makeInfo;

        }

    }

    private static Map<String, Class<? extends GeneratorDepot>> depotOrderTypes = new HashMap<String, Class<? extends GeneratorDepot>>(4) {{

        put(findTypeKey(FromMark.NORMAL, ChannelFromMark.WX_PUBLIC_ENSJ), CreateDepotPublic.class);

        put(findTypeKey(FromMark.MINI, ChannelFromMark.WX_PROGRAM_DLTX), CreateDepotDltx.class);

        put(findTypeKey(FromMark.MINI, ChannelFromMark.WX_PROGRAM_QZGC), CreateDepotQzgc.class);

        put(findTypeKey(FromMark.MINI, ChannelFromMark.WX_PROGRAM_KTZC), CreateDepotKtzc.class);

    }};

    private static String findTypeKey(FromMark fromMark, ChannelFromMark channelFromMark) {

        return fromMark.toString() + channelFromMark.toString();

    }

}
