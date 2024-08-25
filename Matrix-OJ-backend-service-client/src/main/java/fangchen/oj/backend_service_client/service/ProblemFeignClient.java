package fangchen.oj.backend_service_client.service;

import fangchen.oj.backend_model.model.entity.Problem;
import fangchen.oj.backend_model.model.entity.ProblemSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
* @author fangchen.ye
* @description 针对表【problem(题目表)】的数据库操作Service
* @createDate 2024-03-26 17:45:04
*/
@FeignClient(name = "matrix-oj-backend-service-problem", path = "/api/problem/inner")
public interface ProblemFeignClient {

    @GetMapping("/get/id")
    Problem getProblemById(@RequestParam("problemId") long problemId);

    @GetMapping("/problem_submit/get/id")
    ProblemSubmit getProblemSubmitById(@RequestParam("problemId") long problemSubmitId);

    @PostMapping("/problem_submit/update")
    boolean updateProblemSubmitById(@RequestBody ProblemSubmit problemSubmit);

}
