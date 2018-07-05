package cc.tool.order.util;

/**
 * Created by lxj on 16-11-7
 */
public class Encrypt {

    /**
     * Used building output as Hex
     */
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 检查校验MD5值，此方法会将key加到字符串最后
     * @param text 文本内容
     * @param sign 分隔符
     * @param key  双方约定key值串
     * @return md5值
     */
    public static String check(String text, String key, String sign) {
        StringBuffer sb = new StringBuffer();
        String[] str = text.split(sign);
        for (int i = 0; i < str.length; i++) {
            str[i] = null == str[i] ? "" : str[i];
            sb.append(str[i]);
        }
        sb.append(key);
        return sb.toString();
    }

    public static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return out;
    }
}
