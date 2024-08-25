package fangchen.oj.backend_service_judge.judge.strategy.model;

import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeResponse;
import fangchen.oj.backend_model.model.dto.problem.JudgeCase;
import fangchen.oj.backend_model.model.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {

    private ExecuteCodeResponse executeCodeResponse;

    private List<JudgeCase> judgeCaseList;

    private Problem problem;
}
