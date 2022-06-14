package cn.imadc.application.xwareman.core.data.component.collector;

import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRocketMQData;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;
import cn.imadc.application.xwareman.module.item.service.IItemRocketmqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * <p>
 * rocketmq监控项收集
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-01
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceItemRocketMQCollectHandler {

    private final IInstanceRocketmqService instanceRocketmqService;

    private final IItemRocketmqService itemRocketmqService;

    public void handle(InstanceRocketMQData instanceRocketMQData) {
        // 拉取并解析rocketmq节点信息
        try {
            DefaultMQAdminExt defaultMQAdminExt = instanceRocketmqService.getDefaultMQAdminExt(instanceRocketMQData);

            String brokerAddr = instanceRocketMQData.getIp() + Constant.COLON + instanceRocketMQData.getPort();

            KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokerAddr);

            HashMap<String, String> brokerRuntimeStats = kvTable.getTable();

            String putMessageDistributeTime = brokerRuntimeStats.get("putMessageDistributeTime");

            ItemRocketmq itemRocketmq = new ItemRocketmq();

            itemRocketmq.setInstanceRocketmqId(instanceRocketMQData.getId());
            itemRocketmq.setInstanceId(instanceRocketMQData.getInstanceId());

            // 写入耗时分布
            if (StringUtils.isNotEmpty(putMessageDistributeTime)) {
                String[] putMessageDistributeTimeArray = putMessageDistributeTime.split(" ");

                Long putMessageDistributeTime0 = Long.parseLong(putMessageDistributeTimeArray[0].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime0(putMessageDistributeTime0);

                Long putMessageDistributeTime10 = Long.parseLong(putMessageDistributeTimeArray[1].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime10(putMessageDistributeTime10);

                Long putMessageDistributeTime50 = Long.parseLong(putMessageDistributeTimeArray[2].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime50(putMessageDistributeTime50);

                Long putMessageDistributeTime100 = Long.parseLong(putMessageDistributeTimeArray[3].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime100(putMessageDistributeTime100);

                Long putMessageDistributeTime200 = Long.parseLong(putMessageDistributeTimeArray[4].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime200(putMessageDistributeTime200);

                Long putMessageDistributeTime500 = Long.parseLong(putMessageDistributeTimeArray[5].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime500(putMessageDistributeTime500);

                Long putMessageDistributeTime1000 = Long.parseLong(putMessageDistributeTimeArray[6].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime1000(putMessageDistributeTime1000);

                Long putMessageDistributeTime2000 = Long.parseLong(putMessageDistributeTimeArray[7].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime2000(putMessageDistributeTime2000);

                Long putMessageDistributeTime3000 = Long.parseLong(putMessageDistributeTimeArray[8].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime3000(putMessageDistributeTime3000);

                Long putMessageDistributeTime4000 = Long.parseLong(putMessageDistributeTimeArray[9].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime4000(putMessageDistributeTime4000);

                Long putMessageDistributeTime5000 = Long.parseLong(putMessageDistributeTimeArray[10].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime5000(putMessageDistributeTime5000);

                Long putMessageDistributeTime10000 = Long.parseLong(putMessageDistributeTimeArray[11].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTime10000(putMessageDistributeTime10000);

                Long putMessageDistributeTimeOver = Long.parseLong(putMessageDistributeTimeArray[12].split(Constant.COLON)[1]);
                itemRocketmq.setPutMessageDistributeTimeOver(putMessageDistributeTimeOver);

            }

            // 剩余可用堆外内存
            String remainTransientStoreBufferNumbs = brokerRuntimeStats.get("remainTransientStoreBufferNumbs");
            if (StringUtils.isNotEmpty(remainTransientStoreBufferNumbs)) {
                Long remainTransientStoreBufferNumbsVal = Long.parseLong(remainTransientStoreBufferNumbs);
                itemRocketmq.setRemainTransientStoreBufferNumbs(remainTransientStoreBufferNumbsVal);
            }

            // 消息写入均值
            String putMessageAverageSize = brokerRuntimeStats.get("putMessageAverageSize");
            if (StringUtils.isNotEmpty(putMessageAverageSize)) {
                Double putMessageAverageSizeVal = Double.parseDouble(putMessageAverageSize);
                itemRocketmq.setPutMessageAverageSize(putMessageAverageSizeVal);
            }

            // pagecache锁定时长
            String pageCacheLockTimeMills = brokerRuntimeStats.get("pageCacheLockTimeMills");
            if (StringUtils.isNotEmpty(pageCacheLockTimeMills)) {
                Long pageCacheLockTimeMillsVal = Long.parseLong(pageCacheLockTimeMills);
                itemRocketmq.setPageCacheLockTimeMills(pageCacheLockTimeMillsVal);
            }

            // 现在存储的消息数量
            String msgPutTotalTodayNow = brokerRuntimeStats.get("msgPutTotalTodayNow");
            if (StringUtils.isNotEmpty(msgPutTotalTodayNow)) {
                Long msgPutTotalTodayNowVal = Long.parseLong(msgPutTotalTodayNow);
                itemRocketmq.setMsgPutTotalTodayNow(msgPutTotalTodayNowVal);
            }

            // 今天存储的消息数量
            String msgPutTotalTodayMorning = brokerRuntimeStats.get("msgPutTotalTodayMorning");
            if (StringUtils.isNotEmpty(msgPutTotalTodayMorning)) {
                Long msgPutTotalTodayMorningVal = Long.parseLong(msgPutTotalTodayMorning);
                itemRocketmq.setMsgPutTotalTodayMorning(msgPutTotalTodayMorningVal);
            }

            // 昨天存储的消息数量
            String msgPutTotalYesterdayMorning = brokerRuntimeStats.get("msgPutTotalYesterdayMorning");
            if (StringUtils.isNotEmpty(msgPutTotalYesterdayMorning)) {
                Long msgPutTotalYesterdayMorningVal = Long.parseLong(msgPutTotalYesterdayMorning);
                itemRocketmq.setMsgPutTotalYesterdayMorning(msgPutTotalYesterdayMorningVal);
            }

            // 昨天消费的消息数量
            String msgGetTotalYesterdayMorning = brokerRuntimeStats.get("msgGetTotalYesterdayMorning");
            if (StringUtils.isNotEmpty(msgGetTotalYesterdayMorning)) {
                Long msgGetTotalYesterdayMorningVal = Long.parseLong(msgGetTotalYesterdayMorning);
                itemRocketmq.setMsgGetTotalYesterdayMorning(msgGetTotalYesterdayMorningVal);
            }

            // 今天消费的消息数量
            String msgGetTotalTodayMorning = brokerRuntimeStats.get("msgGetTotalTodayMorning");
            if (StringUtils.isNotEmpty(msgGetTotalTodayMorning)) {
                Long msgGetTotalTodayMorningVal = Long.parseLong(msgGetTotalTodayMorning);
                itemRocketmq.setMsgGetTotalTodayMorning(msgGetTotalTodayMorningVal);
            }

            // 现在消费的消息数量
            String msgGetTotalTodayNow = brokerRuntimeStats.get("msgGetTotalTodayNow");
            if (StringUtils.isNotEmpty(msgGetTotalTodayNow)) {
                Long msgGetTotalTodayNowVal = Long.parseLong(msgGetTotalTodayNow);
                itemRocketmq.setMsgGetTotalTodayNow(msgGetTotalTodayNowVal);
            }

            // tps
            String putTps = brokerRuntimeStats.get("putTps");
            if (StringUtils.isNotEmpty(putTps)) {
                Double putTpsVal = Math.ceil(Double.parseDouble(putTps.split(" ")[0]));
                itemRocketmq.setPutTps(putTpsVal);
            }

            itemRocketmqService.add(itemRocketmq);

        } catch (Exception e) {
            log.error("rocketmq监控项收集出现异常", e);
        }
    }

}
