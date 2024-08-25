package fangchen.oj.backend_service_client.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fangchen.oj.backend_model.model.dto.problemsubmit.ProblemSubmitAddRequest;
import fangchen.oj.backend_model.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import fangchen.oj.backend_model.model.entity.ProblemSubmit;
import fangchen.oj.backend_model.model.entity.User;
import fangchen.oj.backend_model.model.vo.ProblemSubmitVO;


/**
* @author fangchen.ye
* @description 针对表【problem_submit(题目提交表)】的数据库操作Service
* @createDate 2024-03-26 17:48:43
*/
public interface ProblemSubmitService extends IService<ProblemSubmit> {
    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest, loginUser
     * @return problemSubmitId
     */
    long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId, problemId
     * @return success
     */
    int doProblemSubmitInner(long userId, long problemId);

    /**
     * 获取查询条件
     *
     * @param problemSubmitQueryRequest
     * @return queryWrapper
     */
    QueryWrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest);

    /**
     * 获取问题提交视图
     *
     * @param problemSubmit, loginUser
     * @return problemSubmitVO
     */
    ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser);

    /**
     * 获取问题提交视图分页
     *
     * @param problemSubmitPage, loginUser
     * @return problemSubmitVOPage
     */
    Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser);
}
