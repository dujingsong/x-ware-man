package cn.imadc.application.xwareman.module.item.util;

import cn.imadc.application.base.data.structure.redis.RedisInfo;
import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * redis监控项工具类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
public class ItemRedisUtil {

    /**
     * 解析redis info中的信息为ItemRedis
     *
     * @param redisNode 节点类型
     * @param redisInfo info信息
     * @return ItemRedis实例
     */
    public static ItemRedis createItemRedis(RedisNode redisNode, RedisInfo redisInfo) {
        ItemRedis itemRedis = new ItemRedis();

        // Server
        RedisInfo.Server server = redisInfo.getServer();
        itemRedis.setProcessId(server.getProcessId());
        itemRedis.setRunId(server.getRunId());
        itemRedis.setUptimeInSeconds(server.getUptimeInSeconds());

        // Clients
        RedisInfo.Clients clients = redisInfo.getClients();
        itemRedis.setConnectedClients(clients.getConnectedClients());
        itemRedis.setBlockedClients(clients.getBlockedClients());

        // Memory
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Memory memory = redisInfo.getMemory();
            itemRedis.setUsedMemory(memory.getUsedMemory());

        }

        // Persistence
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Persistence persistence = redisInfo.getPersistence();
            itemRedis.setRdbChangesSinceLastSave(persistence.getRdbChangesSinceLastSave());
            itemRedis.setRdbBgSaveInProgress(persistence.getRdbBgSaveInProgress());
            itemRedis.setRdbLastSaveTime(persistence.getRdbLastSaveTime());
            itemRedis.setRdbLastBgSaveStatus(persistence.getRdbLastBgSaveStatus());
            itemRedis.setRdbLastBgSaveTimeSec(persistence.getRdbLastBgSaveTimeSec());
            itemRedis.setRdbCurrentBgSaveTimeSec(persistence.getRdbCurrentBgSaveTimeSec());
            itemRedis.setRdbLastCowSize(persistence.getRdbLastCowSize());
            itemRedis.setAofEnabled(persistence.getAofEnabled());
            itemRedis.setAofRewriteInProgress(persistence.getAofRewriteInProgress());
            itemRedis.setAofRewriteScheduled(persistence.getAofRewriteScheduled());
            itemRedis.setAofLastRewriteTimeSec(persistence.getAofLastRewriteTimeSec());
            itemRedis.setAofCurrentRewriteTimeSec(persistence.getAofCurrentRewriteTimeSec());
            itemRedis.setAofLastBgRewriteStatus(persistence.getAofLastBgRewriteStatus());
            itemRedis.setAofLastWriteStatus(persistence.getAofLastWriteStatus());
            itemRedis.setAofLastCowSize(persistence.getAofLastCowSize());
            itemRedis.setAofCurrentSize(persistence.getAofCurrentSize());
            itemRedis.setAofBaseSize(persistence.getAofBaseSize());
            itemRedis.setAofPendingRewrite(persistence.getAofPendingRewrite());
            itemRedis.setAofBufferLength(persistence.getAofBufferLength());
            itemRedis.setAofRewriteBufferLength(persistence.getAofRewriteBufferLength());
            itemRedis.setAofPendingBioFsync(persistence.getAofPendingBioFsync());
            itemRedis.setAofDelayedFsync(persistence.getAofDelayedFsync());
        }

        // Stats
        RedisInfo.Stats stats = redisInfo.getStats();
        itemRedis.setTotalConnectionsReceived(stats.getTotalConnectionsReceived());
        itemRedis.setTotalCommandsProcessed(stats.getTotalCommandsProcessed());
        itemRedis.setInstantaneousInputKbps(stats.getInstantaneousInputKbps());
        itemRedis.setInstantaneousOutputKbps(stats.getInstantaneousOutputKbps());
        itemRedis.setExpiredKeys(stats.getExpiredKeys());
        itemRedis.setEvictedKeys(stats.getEvictedKeys());
        itemRedis.setInstantaneousOpsPerSec(stats.getInstantaneousOpsPerSec());
        itemRedis.setRejectedConnections(stats.getRejectedConnections());
        itemRedis.setKeyspaceHits(stats.getKeyspaceHits());
        itemRedis.setKeyspaceMisses(stats.getKeyspaceMisses());
        itemRedis.setLatestForkUsec(stats.getLatestForkUsec());

        // Replication
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Replication replication = redisInfo.getReplication();
            itemRedis.setRole(replication.getRole());
        }

        // CPU

        // Cluster

        // Keyspace
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Keyspace keyspace = redisInfo.getKeyspace();
            List<String> dbs = keyspace.getDbs();
            int usedDb = !CollectionUtils.isEmpty(dbs) ? dbs.size() : 0;
            itemRedis.setUsedDb(usedDb);

            List<RedisInfo.Keyspace.DBInfo> dbInfoList = keyspace.getDbInfoList();
            long dbKey = 0, dbExpires = 0;
            if (!CollectionUtils.isEmpty(dbInfoList)) {
                for (RedisInfo.Keyspace.DBInfo dbInfo : dbInfoList) {
                    dbKey += dbInfo.getKeys();
                    dbExpires += dbInfo.getExpires();
                }
            }
            itemRedis.setDbKey(dbKey);
            itemRedis.setDbExpires(dbExpires);
        }

        // Sentinel
        if (redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Sentinel sentinel = redisInfo.getSentinel();

        }

        return itemRedis;
    }
}
