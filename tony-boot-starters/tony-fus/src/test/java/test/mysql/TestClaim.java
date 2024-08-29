/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package test.mysql;

import com.aizuda.bpm.engine.entity.FlwTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;

/**
 * 测试票签流程
 *
 * @author 青苗
 */
public class TestClaim extends MysqlTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestClaim.class);

    @BeforeEach
    public void before() {
        processId = this.deployByResource("test/testClaim.json", testCreator);
    }

    @Test
    public void testReclaimTask() {
        // 启动指定流程定义ID启动流程实例
        flowLongEngine.startInstanceById(processId, testCreator).ifPresent(instance -> {
            List<FlwTask> flwTaskList = flowLongEngine.queryService().getTasksByInstanceId(instance.getId());
            for (FlwTask flwTask : flwTaskList) {
                // 主管审批
                flowLongEngine.executeTask(flwTask.getId(), testCreator);

                // 拿回历史任务
                flowLongEngine.taskService().reclaimTask(flwTask.getId(), testCreator)
                        .ifPresent(t -> Assertions.assertEquals(flwTask.getTaskName(), t.getTaskName()));
            }
        });
    }
}
