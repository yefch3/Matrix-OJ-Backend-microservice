package fangchen.oj.backend_service_judge.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import fangchen.oj.backend_service_judge.judge.codesandbox.CodeSandBox;
import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeRequest;
import fangchen.oj.backend_model.model.codesandbox.ExecuteCodeResponse;

public class RemoteCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String url = "3.128.50.111:8080/execute";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String result = HttpUtil.post(url, json);
        return JSONUtil.toBean(result, ExecuteCodeResponse.class);
    }
}
