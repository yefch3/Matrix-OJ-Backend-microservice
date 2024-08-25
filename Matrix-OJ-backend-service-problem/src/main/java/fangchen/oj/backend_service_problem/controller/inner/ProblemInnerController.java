package fangchen.oj.backend_service_problem.controller.inner;

import fangchen.oj.backend_model.model.entity.Problem;
import fangchen.oj.backend_model.model.entity.ProblemSubmit;
import fangchen.oj.backend_service_client.service.ProblemFeignClient;
import fangchen.oj.backend_service_problem.service.ProblemService;
import fangchen.oj.backend_service_problem.service.ProblemSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class ProblemInnerController implements ProblemFeignClient {

    @Resource
    private ProblemService problemService;

    @Resource
    private ProblemSubmitService problemSubmitService;

    @Override
    @GetMapping("/get/id")
    public Problem getProblemById(@RequestParam("problemId") long problemId) {
        return problemService.getById(problemId);
    }

    @Override
    @GetMapping("/problem_submit/get/id")
    public ProblemSubmit getProblemSubmitById(@RequestParam("problemId") long problemSubmitId) {
        return problemSubmitService.getById(problemSubmitId);
    }

    @Override
    @PostMapping("/problem_submit/update")
    public boolean updateProblemSubmitById(@RequestBody ProblemSubmit problemSubmit) {
        return problemSubmitService.updateById(problemSubmit);
    }
}
