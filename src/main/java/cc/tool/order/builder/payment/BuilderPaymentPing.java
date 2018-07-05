package cc.tool.order.builder.payment;

import cc.admore.infant.model.order.Order;
import cc.admore.infant.model.order.enums.ChannelFromMark;
import cc.admore.infant.model.order.enums.PayMark;
import cc.admore.infant.model.payment.PaymentPing;
import cc.admore.infant.model.payment.PingCharge;
import cc.tool.order.model.Fail;
import cc.tool.order.model.MakeInfo;
import cc.tool.order.model.payment.PaymentInfo;
import cc.tool.order.operation.OperationManager;
import cc.tool.order.util.DateUtil;
import cc.tool.order.util.OrderConfigureUtil;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.net.APIResource;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxj on 18-5-21
 */
public class BuilderPaymentPing extends BuilderPaymentParty<PaymentPing> {

    private String changeId;

    public BuilderPaymentPing(Order order, PaymentInfo paymentInfo, OperationManager operationManager) {
        super(order, paymentInfo, operationManager);
    }

    @Override
    protected PaymentPing payment() {

        payment.setPaymentMarker(false);
        payment.setPayMark(PayMark.PING);
        payment.setChargeId(changeId);

        return payment;

    }

    @Override
    public MakeInfo build() {

        PingCharge pingCharge = operationManager.pingChargeDao.findByOrderId(order.getId());

        Charge charge;

        if (!ObjectUtils.isEmpty(pingCharge)) {

            charge = APIResource.GSON.fromJson(pingCharge.getChargeJson(), Charge.class);

        } else {

            ChannelFromMark channelFromMark = order.getChannelFromMark();

            Pingpp.apiKey = pingApiKeys.get(channelFromMark);

            Pingpp.privateKey = pingPrivateKeys.get(channelFromMark);

            Map<String, Object> pingChargeParams = new HashMap<String, Object>();

            pingChargeParams.put("order_no", order.getId().toString());

            pingChargeParams.put("amount", order.getActualAmount());

            Map<String, String> appChargeParam = new HashMap<String, String>();

            appChargeParam.put("id", pingAppIds.get(channelFromMark));

            String channel = channels.get(channelFromMark);
            pingChargeParams.put("currency", "cny");
            pingChargeParams.put("channel", channel);
            pingChargeParams.put("app", appChargeParam);
            pingChargeParams.put("client_ip", "183.60.92.219");
            pingChargeParams.put("body", bodys.get(channelFromMark));
            pingChargeParams.put("subject", bodys.get(channelFromMark));

            Map<String, String> extraChargeParam = new HashMap<String, String>();

            if ("isv_wap".equals(channel)) {

                extraChargeParam.put("pay_channel", "wx");
                extraChargeParam.put("terminal_id", "00000001");
                extraChargeParam.put("result_url", OrderConfigureUtil.getValue("result_url"));

            } else if ("wx_lite".equals(channel)) {

                extraChargeParam.put("open_id", paymentInfo.getOpenId());

                pingChargeParams.put("time_expire", DateUtil.createUnixDate(order.getCreated().getMillis(), order.getExpire()));

            }

            pingChargeParams.put("extra", extraChargeParam);

            try {

                charge = Charge.create(pingChargeParams);

            } catch (AuthenticationException | InvalidRequestException | APIConnectionException | ChannelException | RateLimitException | APIException e) {

                e.printStackTrace();

                makeInfo.setSuccess(false);
                makeInfo.setMsg(Fail.PAYMENT_FAIL_PING.toString());

                return makeInfo;

            }

            pingCharge = new PingCharge();

            pingCharge.setOrderId(order.getId());
            pingCharge.setChargeId(charge.getId());
            pingCharge.setStatus(order.getStatus());
            pingCharge.setChargeJson(APIResource.getGson().toJson(charge));

            operationManager.pingChargeDao.save(pingCharge);

        }

        this.changeId = charge.getId();

        super.build();

        makeInfo.setSuccess(true);
        makeInfo.setObj(charge);
        makeInfo.setMsg(PayMark.PING.toString());

        return makeInfo;

    }

    private static final Map<ChannelFromMark, String> channels = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, "isv_wap");

        put(ChannelFromMark.WX_PROGRAM_DLTX, "wx_lite");

        put(ChannelFromMark.WX_PROGRAM_QZGC, "wx_lite");

    }};

    private static final Map<ChannelFromMark, String> pingAppIds = new HashMap<ChannelFromMark, String>() {{

        put(ChannelFromMark.WX_PUBLIC_ENSJ, OrderConfigureUtil.getValue("pingAppId"));

        put(ChannelFromMark.WX_PROGRAM_DLTX, OrderConfigureUtil.getValue("pingAppIdDltx"));

        put(ChannelFromMark.WX_PROGRAM_QZGC, OrderConfigureUtil.getValue("pingAppIdQzgc"));

    }};


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

}
