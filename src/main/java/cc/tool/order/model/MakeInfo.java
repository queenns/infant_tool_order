package cc.tool.order.model;

/**
 * Created by lxj on 18-5-25
 */
public class MakeInfo extends Info {

    public boolean success = false;

    private String msg;

    private Object obj;

    public MakeInfo() {
    }

    public MakeInfo(boolean success) {
        this.success = success;
    }

    public MakeInfo(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "MakeInfo{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                "} ";
    }

}
