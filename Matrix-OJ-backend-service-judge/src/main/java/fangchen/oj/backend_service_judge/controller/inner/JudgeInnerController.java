package fangchen.oj.backend_service_judge.controller.inner;

import fangchen.oj.backend_service_client.service.JudgeFeignClient;
import fangchen.oj.backend_service_judge.judge.JudgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    @GetMapping("/do")
    public void doJudge(long problemSubmitId) {
        judgeService.doJudge(problemSubmitId);
    }
}
