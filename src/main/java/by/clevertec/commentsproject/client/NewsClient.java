package by.clevertec.commentsproject.client;

import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.util.Constant.BaseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Клиент для работы с микросервисом новостей.
 */
@FeignClient(name = "micro-news", url = "${news-service.url}")
public interface NewsClient {

    /**
     * Получает новость по идентификатору.
     *
     * @param id идентификатор новости
     * @return новость
     */
    @GetMapping(BaseApi.NEWS_ID)
    ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id);

}
