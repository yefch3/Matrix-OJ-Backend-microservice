package fangchen.oj.backend_service_problem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fangchen.oj.backend_common.common.BaseResponse;
import fangchen.oj.backend_common.common.ErrorCode;
import fangchen.oj.backend_common.common.ResultUtils;
import fangchen.oj.backend_common.exception.BusinessException;
import fangchen.oj.backend_model.model.dto.problemsubmit.ProblemSubmitAddRequest;
import fangchen.oj.backend_model.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import fangchen.oj.backend_model.model.entity.ProblemSubmit;
import fangchen.oj.backend_model.model.entity.User;
import fangchen.oj.backend_model.model.vo.ProblemSubmitVO;
import fangchen.oj.backend_service_problem.service.ProblemSubmitService;
import fangchen.oj.backend_service_client.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@RestController
@RequestMapping("/submit")
@Slf4j
public class ProblemSubmitController {

    @Resource
    private ProblemSubmitService problemSubmitService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest, request
     * @return resultNum 题目提交ID
     */
    @PostMapping("/")
    public BaseResponse<Long> doProblemSubmit(@RequestBody ProblemSubmitAddRequest problemSubmitAddRequest,
                                         HttpServletRequest request) {
        if (problemSubmitAddRequest == null || problemSubmitAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userFeignClient.getLoginUser(request);
        long result = problemSubmitService.doProblemSubmit(problemSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 题目提交列表
     *
     * @param problemSubmitQueryRequest, request
     * @return Page<ProblemSubmitVO> 题目提交列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ProblemSubmitVO>> listProblemSubmitByPage(@RequestBody ProblemSubmitQueryRequest problemSubmitQueryRequest,
                                                                       HttpServletRequest request) {
        long current = problemSubmitQueryRequest.getCurrent();
        long size = problemSubmitQueryRequest.getPageSize();
        final User loginUser = userFeignClient.getLoginUser(request);
        // 从数据库中查询原始的题目提交分页信息
        Page<ProblemSubmit> problemSubmitPage = problemSubmitService.page(new Page<>(current, size),
                problemSubmitService.getQueryWrapper(problemSubmitQueryRequest));
        // filter
        return ResultUtils.success(problemSubmitService.getProblemSubmitVOPage(problemSubmitPage, loginUser));
    }

}
