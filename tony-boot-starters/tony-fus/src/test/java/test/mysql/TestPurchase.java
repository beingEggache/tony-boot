/*
 * Copyright 2023-2025 Licensed under the AGPL License
 */
package test.mysql;

import com.aizuda.bpm.engine.QueryService;
import com.aizuda.bpm.engine.TaskService;
import com.aizuda.bpm.engine.entity.FlwHisTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 测试简单流程
 *
 * @author xdg
 */
public class TestPurchase extends MysqlTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestPurchase.class);

    @BeforeEach
    public void before() {
        processId = this.deployByResource("test/purchase.json", testCreator);
    }

    @Test
    public void test() {
        // 启动指定流程定义ID启动流程实例
        flowLongEngine.startInstanceById(processId, testCreator).ifPresent(instance -> {

            // 领导审批
            this.executeActiveTasks(instance.getId(), testCreator);

            // 撤回任务（领导审批）
            QueryService queryService = flowLongEngine.queryService();
            List<FlwHisTask> hisTasks = queryService.getHisTasksByInstanceId(instance.getId());
            FlwHisTask hisTask = hisTasks.stream().filter(t -> Objects.equals("领导审批", t.getTaskName())).findFirst().get();
            TaskService taskService = flowLongEngine.taskService();
            taskService.withdrawTask(hisTask.getId(), testCreator);

            // 当前任务ID 用拿回任务
            // this.executeActiveTasks(instance.getId(), t -> taskService.reclaimTask(t.getParentTaskId(), testCreator));


            // 驳回任务（领导审批驳回，任务至发起人）
            this.executeActiveTasks(instance.getId(), t ->
                    taskService.rejectTask(t, testCreator, new HashMap<String, Object>() {{
                        put("reason", "不符合要求");
                    }})
            );

            // 执行当前任务并跳到【经理确认】节点
            this.executeActiveTasks(instance.getId(), t ->
                    flowLongEngine.executeJumpTask(t.getId(), "k005", testCreator)
            );

            // 经理确认，流程结束
            this.executeActiveTasks(instance.getId(), testCreator);
        });
    }
}
