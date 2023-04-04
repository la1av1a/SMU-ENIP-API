package com.smu.smuenip.Infrastructure.util.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ClovaOCRAPI;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO;
import com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult.OcrResultDTO.Formatted;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(ClovaOCRAPI.class)
class ClovaOCRAPITest {


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClovaOCRAPI clovaOCRAPI;

    @Test
    void test() {

        //given
        String jsonContent = null;
        OcrResultDTO expectClass = null;
        try {
            jsonContent = new String(Files.readAllBytes(Paths.get("json/ocr.json")));
            expectClass = objectMapper.readValue(jsonContent, OcrResultDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedValue = "2";

        Optional<String> foundValue = expectClass.getResult().getSubResults().stream()
            .flatMap(subResult -> subResult.getItems().stream())
            .map(item -> item.getCount().formatted.value)
            .filter(value -> value.equals(expectedValue))
            .findFirst();

        String jsonContent2 = null;
        OcrResultDTO expectClass2 = null;
        try {
            jsonContent2 = new String(Files.readAllBytes(Paths.get("json/ocr2.json")));
            expectClass2 = objectMapper.readValue(jsonContent2, OcrResultDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedValue2 = "1";

        class Test {

            private String name;
            private String count;
            private String price;

            public Test(String name, String count, String price) {
                this.name = name;
                this.count = count;
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public String getCount() {
                return count;
            }
        }

        List<Test> foundValue2 = expectClass2.getResult().getSubResults().stream()
            .flatMap(subResult -> subResult.getItems().stream())
            .map(item -> new Test(
                Optional.ofNullable(item.getName())
                    .flatMap(name -> Optional.ofNullable(name.formatted))
                    .map(Formatted::getValue).orElse(null),
                Optional.ofNullable(item.getCount())
                    .flatMap(count -> Optional.ofNullable(count.formatted))
                    .map(Formatted::getValue).orElse(null),
                Optional.ofNullable(item.getPriceInfo())
                    .flatMap(priceInfo -> Optional.ofNullable(priceInfo.price))
                    .flatMap(price -> Optional.ofNullable(price.formatted))
                    .map(Formatted::getValue).orElse(null)
            ))
            .filter(test -> "크래프트블랙P470ml".equals(test.getName()) && expectedValue2.equals(
                test.getCount()))
            .collect(Collectors.toList());

        List<OcrResultDTO.Item> items = expectClass.getResult().getSubResults().get(0).getItems();
        List<Test> tests = new ArrayList<>();
        for (OcrResultDTO.Item item : items) {
            if (item.getName().formatted == null) {
                continue;
            }
            String name = item.getName().formatted.value;
            String count =
                item.getCount().formatted == null ? null : item.getCount().formatted.value;
            String price = item.getPriceInfo().price.formatted == null ? null
                : item.getPriceInfo().price.formatted.value;

            tests.add(new Test(name, count, price));
        }

        Assertions.assertThat(foundValue2).isNotEmpty();
        Assertions.assertThat(tests).hasSize(4);

        String price = expectClass2.getResult().getSubResults().get(0).getItems().get(0)
            .getPriceInfo().price.formatted.value;
        Assertions.assertThat(price).isEqualTo("2500");
    }
}
