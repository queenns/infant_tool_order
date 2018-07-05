package cc.tool.order.model.confirm;

import com.pingplusplus.model.Charge;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmInfoPing extends ConfirmInfo{

    private Charge charge;

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "ConfirmInfoPing{" +
                "charge=" + charge +
                "} " + super.toString();
    }

}
