package cc.tool.order.model.confirm;

/**
 * Created by lxj on 18-5-22
 */
public class ConfirmInfoMember extends ConfirmInfo {

    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "ConfirmInfoMember{" +
                "memberId='" + memberId + '\'' +
                "} " + super.toString();
    }

}
