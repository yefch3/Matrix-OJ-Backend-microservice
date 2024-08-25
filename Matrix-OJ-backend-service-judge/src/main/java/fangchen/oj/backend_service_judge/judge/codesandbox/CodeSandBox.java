package fangchen.oj.backend_service_judge.judge.codesandbox;

import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeRequest;
import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeResponse;


public interface CodeSandBox {
    /**
     * Execute code in CodeSandBox
     * @return ExecuteCodeResponse
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
