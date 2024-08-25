package fangchen.oj.backend_service_client.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "matrix-oj-backend-service-judge", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     */
    @GetMapping("/do")
    void doJudge(long problemSubmitId);
}
