package by.clevertec.commentsproject.client;

import static by.clevertec.commentsproject.util.TestConstant.ID_ONE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

import by.clevertec.commentsproject.dto.response.NewsResponseDto;
import by.clevertec.commentsproject.util.DataTestBuilder;
import by.clevertec.commentsproject.util.TestConstant.Path;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@RequiredArgsConstructor
@WireMockTest(httpPort = 8081)
class NewsClientWiremockTest {

    private final NewsClient newsClient;

    @Test
    public void testGetNewsById() throws JsonProcessingException {
        Long id = ID_ONE;

        NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                .build()
                .buildNewsResponseDto();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        stubFor(get(urlEqualTo(Path.NEWS + id))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(newsResponseDto))));

        ResponseEntity<NewsResponseDto> responseEntity = newsClient.getNewsById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(newsResponseDto, responseEntity.getBody());
    }
}
