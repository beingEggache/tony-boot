package test.mysql.config;

import com.aizuda.bpm.engine.entity.FlwTaskActor;
import com.aizuda.bpm.engine.impl.GeneralAccessStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * 测试任务访问策略类
 */
public class TestTaskAccessStrategy extends GeneralAccessStrategy {

    @Override
    public @Nullable FlwTaskActor isAllowed(@Nullable String userId, @NotNull List<? extends FlwTaskActor> taskActors) {
        if (null != taskActors) {
            // 角色部门情况测试不做验证直接允许认领，实际生产环境必须验证
            if (taskActors.stream().anyMatch(t -> !Objects.equals(0, t.getActorType()))) {
                return taskActors.get(0);
            }
        }
        return super.isAllowed(userId, taskActors);
    }
}
