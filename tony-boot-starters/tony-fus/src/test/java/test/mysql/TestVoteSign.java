/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package test.mysql;

import com.aizuda.bpm.engine.core.FlowCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

/**
 * 测试票签流程
 *
 * @author 青苗
 */
public class TestVoteSign extends MysqlTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestVoteSign.class);

    @BeforeEach
    public void before() {
        processId = this.deployByResource("test/voteSign.json", testCreator);
    }

    @Test
    public void test() {
        // 启动指定流程定义ID启动流程实例
        flowLongEngine.startInstanceById(processId, testCreator).ifPresent(instance -> {

            // test1 领导审批同意
            this.executeTask(instance.getId(), FlowCreator.of(testUser1, "青苗"));

            // test3 领导审批同意
            this.executeTask(instance.getId(), FlowCreator.of(testUser3, "聂秋秋"));

            // test2 不在执行达到票签值
            // 抄送人力资源，流程自动结束

        });
    }
}
