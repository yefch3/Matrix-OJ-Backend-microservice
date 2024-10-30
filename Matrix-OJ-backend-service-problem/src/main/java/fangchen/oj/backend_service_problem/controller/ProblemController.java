package fangchen.oj.backend_service_problem.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import fangchen.oj.backend_common.annotation.AuthCheck;
import fangchen.oj.backend_common.common.BaseResponse;
import fangchen.oj.backend_common.common.DeleteRequest;
import fangchen.oj.backend_common.common.ErrorCode;
import fangchen.oj.backend_common.common.ResultUtils;
import fangchen.oj.backend_common.constant.UserConstant;
import fangchen.oj.backend_common.exception.BusinessException;
import fangchen.oj.backend_common.exception.ThrowUtils;
import fangchen.oj.backend_model.model.dto.problem.*;
import fangchen.oj.backend_model.model.entity.Problem;
import fangchen.oj.backend_model.model.entity.User;
import fangchen.oj.backend_model.model.vo.ProblemVO;
import fangchen.oj.backend_service_client.service.UserFeignClient;
import fangchen.oj.backend_service_problem.service.ProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 题目接口
 *
 * @author <a href="https://www.linkedin.com/in/fangchen-ye/i">fangchen</a>
 * @from <a href="https://www.linkedin.com/in/fangchen-ye/">fangchen</a>
 */
@RestController
@RequestMapping("/")
@Slf4j
public class ProblemController {
    @Resource
    private ProblemService problemService;

    @Resource
    private UserFeignClient userFeignClient;

    // region 增删改查

    /**
     * 创建
     *
     */
    @PostMapping("/add")
    public BaseResponse<Long> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpServletRequest request) {
        if (problemAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemAddRequest, problem);
        List<String> tags = problemAddRequest.getTags();
        JudgeConfig judgeConfig = problemAddRequest.getJudgeConfig();
        List<JudgeCase> judgeCase = problemAddRequest.getJudgeCase();
        if (tags != null) {
            problem.setTags(JSONUtil.toJsonStr(tags));
        }
        if (judgeConfig != null) {
            problem.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        if (judgeCase != null) {
            problem.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }
        problemService.validProblem(problem, true);
        User loginUser = userFeignClient.getLoginUser(request);
        problem.setUserId(loginUser.getId());
        problem.setFavourNum(0);
        problem.setThumbNum(0);
        boolean result = problemService.save(problem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newProblemId = problem.getId();
        return ResultUtils.success(newProblemId);
    }

    /**
     * 删除
     *
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteProblem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getProblemById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldProblem.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = problemService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateProblem(@RequestBody ProblemUpdateRequest problemUpdateRequest) {
        if (problemUpdateRequest == null || problemUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemUpdateRequest, problem);
        List<String> tags = problemUpdateRequest.getTags();
        List<JudgeCase> judgeCase = problemUpdateRequest.getJudgeCase();
        Integer difficulty = problemUpdateRequest.getDifficulty();
        if (tags != null) {
            problem.setTags(JSONUtil.toJsonStr(tags));
        }
        if (judgeCase != null) {
            problem.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }
        if (difficulty != null) {
            problem.setDifficulty(difficulty);
        }
        // 参数校验
        problemService.validProblem(problem, false);
        long id = problemUpdateRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getProblemById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取所有数据
     *
     */
    // todo 优化：增加权限校验，如果是管理员那么可以看到某些字段
    @GetMapping("/get")
    public BaseResponse<Problem> getProblemById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 权限校验
        User loginUser = userFeignClient.getLoginUser(request);
        if (!userFeignClient.isAdmin(loginUser) && !problem.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(problem);
    }

    /**
     * 根据 id 获取脱敏数据
     *
     */
    @GetMapping("/get/vo")
    public BaseResponse<ProblemVO> getProblemVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getProblemById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(problemService.getProblemVO(problem, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Problem>> listProblemByPage(@RequestBody ProblemQueryRequest problemQueryRequest) {
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
        return ResultUtils.success(problemPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ProblemVO>> listProblemVOByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
                                                       HttpServletRequest request) {
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
        return ResultUtils.success(problemService.getProblemVOPage(problemPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ProblemVO>> listMyProblemVOByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
                                                         HttpServletRequest request) {
        if (problemQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        problemQueryRequest.setUserId(loginUser.getId());
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
        return ResultUtils.success(problemService.getProblemVOPage(problemPage, request));
    }

    // endregion

//    /**
//     * 分页搜索（从 ES 查询，封装类）
//     *
//     */
//    @Deprecated
//    @PostMapping("/search/page/vo")
//    public BaseResponse<Page<ProblemVO>> searchProblemVOByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
//                                                         HttpServletRequest request) {
//        long size = problemQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Problem> problemPage = problemService.searchFromEs(problemQueryRequest);
//        return ResultUtils.success(problemService.getProblemVOPage(problemPage, request));
//    }

    /**
     * 编辑（用户）
     *
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editProblem(@RequestBody ProblemEditRequest problemEditRequest, HttpServletRequest request) {
        if (problemEditRequest == null || problemEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemEditRequest, problem);
        List<String> tags = problemEditRequest.getTags();
        if (tags != null) {
            problem.setTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
        problemService.validProblem(problem, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = problemEditRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getProblemById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldProblem.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }
}
