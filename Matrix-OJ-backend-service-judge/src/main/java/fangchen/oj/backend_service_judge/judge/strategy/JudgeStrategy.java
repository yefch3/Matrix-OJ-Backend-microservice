package fangchen.oj.backend_service_judge.judge.strategy;

import fangchen.oj.backend_service_judge.judge.strategy.model.JudgeContext;
import fangchen.oj.backend_model.model.dto.problemsubmit.JudgeResult;

public interface JudgeStrategy {
    JudgeResult executeStrategy(JudgeContext judgeContext);
}
