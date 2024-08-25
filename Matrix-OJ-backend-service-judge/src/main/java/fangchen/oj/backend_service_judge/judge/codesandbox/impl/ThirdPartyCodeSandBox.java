package fangchen.oj.backend_service_judge.judge.codesandbox.impl;

import fangchen.oj.backend_service_judge.judge.codesandbox.CodeSandBox;
import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeRequest;
import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeResponse;

public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("Executing code in third party code sandbox");
        return null;
    }
}
