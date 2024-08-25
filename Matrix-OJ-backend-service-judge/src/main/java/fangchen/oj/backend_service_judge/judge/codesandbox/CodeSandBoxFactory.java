package fangchen.oj.backend_service_judge.judge.codesandbox;

import fangchen.oj.backend_service_judge.judge.codesandbox.impl.ExampleCodeSandBox;
import fangchen.oj.backend_service_judge.judge.codesandbox.impl.RemoteCodeSandBox;
import fangchen.oj.backend_service_judge.judge.codesandbox.impl.ThirdPartyCodeSandBox;

public class CodeSandBoxFactory {
    public static CodeSandBox createCodeSandBox(String type) {
        return switch (type) {
            case "example" -> new ExampleCodeSandBox();
            case "remote" -> new RemoteCodeSandBox();
            case "thirdParty" -> new ThirdPartyCodeSandBox();
            default -> null;
        };
    }
}
