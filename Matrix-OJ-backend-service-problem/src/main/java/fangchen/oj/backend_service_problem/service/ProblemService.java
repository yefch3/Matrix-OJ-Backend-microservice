package fangchen.oj.backend_service_problem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import fangchen.oj.backend_model.model.dto.problem.ProblemQueryRequest;
import fangchen.oj.backend_model.model.entity.Problem;
import fangchen.oj.backend_model.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author fangchen.ye
* @description 针对表【problem(题目表)】的数据库操作Service
* @createDate 2024-03-26 17:45:04
*/
public interface ProblemService extends IService<Problem> {
    /**
     * 校验
     *
     */
    void validProblem(Problem problem, boolean add);

    /**
     * 获取查询条件
     *
     */
    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

//    /**
//     * 从 ES 查询
//     *
//     * @param problemQueryRequest
//     * @return
//     */
//    Page<Problem> searchFromEs(ProblemQueryRequest problemQueryRequest);

    /**
     * 获取题目封装
     *
     */
    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     */
    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);
}
