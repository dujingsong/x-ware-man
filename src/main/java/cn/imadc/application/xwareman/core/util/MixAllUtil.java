package cn.imadc.application.xwareman.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 工具类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-13
 */
public class MixAllUtil {

    private static final long KB = 1024;
    private static final long MB = 1024 * KB;
    private static final long GB = 1024 * MB;


    private static final long MINUTE = 60;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    /**
     * 优化内存展示
     *
     * @param memory byte为单位的内存
     * @return 优化后的描述文字
     */
    public static String prettyMemory(Long memory) {
        if (null == memory) return null;

        BigDecimal bigDecimal = new BigDecimal(memory);

        if (memory < KB) return memory + "b";
        if (memory < MB) return bigDecimal.divide(new BigDecimal(KB), 2, RoundingMode.FLOOR) + "kb";
        if (memory < GB) return bigDecimal.divide(new BigDecimal(MB), 2, RoundingMode.FLOOR) + "mb";
        return bigDecimal.divide(new BigDecimal(GB), 2, RoundingMode.FLOOR) + "gb";
    }

    /**
     * 优化时间展示
     *
     * @param second 秒为单位的时间
     * @return 优化后的描述文字
     */
    public static String prettyTime(Long second) {
        if (null == second) return null;

        StringBuffer stringBuffer = new StringBuffer();

        long time = second;

        long day = time / DAY;
        if (day > 0) {
            stringBuffer.append(day).append("天");
            time = time - day * DAY;
        }
        long hour = time / HOUR;
        if (hour > 0) {
            stringBuffer.append(hour).append("小时");
            time = time - hour * HOUR;
        }
        long minute = time / MINUTE;
        if (minute > 0) {
            stringBuffer.append(minute).append("分钟");
            time = time - minute * MINUTE;
        }
        if (time > 0) {
            stringBuffer.append(time).append("秒");
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(prettyMemory(6 * GB));
        System.out.println(prettyTime(6 * DAY + 22 * HOUR + 65 * MINUTE + 100 * MINUTE));
    }

}
