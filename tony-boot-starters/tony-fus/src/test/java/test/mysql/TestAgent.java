/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package test.mysql;

import com.aizuda.bpm.engine.TaskService;
import com.aizuda.bpm.engine.core.FlowCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * 测试审批代理
 *
 * @author xdg
 */
public class TestAgent extends MysqlTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestAgent.class);

    @BeforeEach
    public void before() {
        processId = this.deployByResource("test/purchase.json", testCreator);
    }

    @Test
    public void test() {
        flowLongEngine.startInstanceById(processId, testCreator).ifPresent(instance -> {

            final TaskService taskService = flowLongEngine.taskService();

            // 领导审批指定代理人 test002 test003
            List<FlowCreator> agentFlowCreators = Arrays.asList(test2Creator, test3Creator);
            executeActiveTasks(instance.getId(), flwTask -> taskService.agentTask(flwTask.getId(), testCreator, agentFlowCreators));

            // 代理人 test002 完成任务
            executeActiveTasks(instance.getId(), test2Creator);

            // 领导审批，代理人历史任务清理，进入下一个节点
            executeActiveTasks(instance.getId(), testCreator);

        });
    }
}
