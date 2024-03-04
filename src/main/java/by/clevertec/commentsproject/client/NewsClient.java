package by.clevertec.commentsproject.client;

import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.util.Constant.BaseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-news", url = "${news-service.url}")
public interface NewsClient {

    @GetMapping(BaseApi.NEWS_ID)
    ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id);

}
