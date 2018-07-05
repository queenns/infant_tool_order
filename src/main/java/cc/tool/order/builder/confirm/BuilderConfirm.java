package cc.tool.order.builder.confirm;

import cc.admore.infant.model.payment.Payment;
import cc.tool.order.builder.AbstractBuilder;
import cc.tool.order.model.confirm.ConfirmInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lxj on 18-5-22
 */
public abstract class BuilderConfirm<T extends Payment> extends AbstractBuilder<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    T payment;

    protected ConfirmInfo confirmInfo;

    public BuilderConfirm(ConfirmInfo confirmInfo) {

        this.confirmInfo = confirmInfo;

        initType();

        try {

            this.payment = (T) this.clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

        }

    }


}
