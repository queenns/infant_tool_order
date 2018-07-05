package cc.tool.order.action;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.payment.PaymentPing;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.util.OrderConfigureUtil;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-6-7
 */
public class CloseOrderPingAction extends CloseOrderAction {

    public CloseOrderPingAction(Order order) {
        super(order);
    }

    @Override
    public MakeInfo action() {

        MakeInfo makeInfo = new MakeInfo();

        Pingpp.apiKey = pingApiKeys.get(order.getChannelFromMark());

        Pingpp.privateKey = pingPrivateKeys.get(order.getChannelFromMark());

        try {

            String channel = channels.get(order.getChannelFromMark());

            String chargeId = ((PaymentPing) order.getPayment()).getChargeId();

            Charge charge = Charge.retrieve(chargeId);

            logger.info("close order ping retrieve charge[{}]", charge);

            if ("isv_wap".equals(channel)) {

                logger.info("close order ping orderId[{}] channel [isv_wap]", order.getId());

                if (charge.getPaid()) {

                    makeInfo.setSuccess(false);
                    makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_PING_PAYMENT.toString());

                } else {

                    charge = Charge.reverse(chargeId);

                    logger.info("close order ping reverse charge[{}]", charge);

                    if (!charge.getReversed()) {

                        makeInfo.setSuccess(false);
                        makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_PING_REVERSE.toString());

                    } else {

                        makeInfo.setSuccess(true);

                        if (charge.getPaid())

                            logger.error("close order ping error paid[{}] and reverse[{}]", charge.getPaid(), charge.getReversed());

                    }

                }

            } else if ("wx_lite".equals(channel)) {

                logger.info("close order ping orderId[{}] channel [wx_lite]", order.getId());

                if (charge.getPaid()) {

                    makeInfo.setSuccess(false);
                    makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_PING_PAYMENT.toString());

                } else {

                    makeInfo.setSuccess(true);

                }

            } else {

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_PING.toString());

            }

            logger.info("close order ping result[{}]", makeInfo.success);

            return makeInfo;

        } catch (AuthenticationException | InvalidRequestException | APIException | APIConnectionException | ChannelException | RateLimitException e) {

            e.printStackTrace();

            makeInfo.setSuccess(false);
            makeInfo.setMsg(Fail.EXPIRE_FAIL_CLOSE_PING.toString());

            return makeInfo;

        }

    }

    private static final Map<ChannelFromMark, String> pingApiKeys = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("pingApiKey"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("pingApiKeyDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("pingApiKeyQzgc"));

    }};

    private static final Map<ChannelFromMark, String> pingPrivateKeys = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("pingPrivateKey"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("pingPrivateKeyDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("pingPrivateKeyQzgc"));

    }};

    private static final Map<ChannelFromMark, String> channels = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, "isv_wap");

        put(ChannelFromMark.WX_PROGRAM_DLTX, "wx_lite");

        put(ChannelFromMark.WX_PROGRAM_QZGC, "wx_lite");

    }};

}
