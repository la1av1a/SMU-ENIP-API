package com.smu.smuenip.infrastructure.util.naver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smu.smuenip.infrastructure.util.naver.ocr.ClovaOcrApiRealImpl;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto;
import com.smu.smuenip.infrastructure.util.naver.ocr.dto.OcrResponseDto.Image;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest(ClovaOcrApiRealImpl.class)
class ClovaOcrApiRealImplTest {


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClovaOcrApiRealImpl clovaOCRAPIRealImpl;

    @Test
    void test() {

        OcrResponseDto ocrResponseDTO = null;
        try {
            String jsonContent2 = new String(
                    Files.readAllBytes(Paths.get("src/test/resources/json/ocr.json")));
            ocrResponseDTO = objectMapper.readValue(jsonContent2, OcrResponseDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image expectClass2 = ocrResponseDTO.getImages()[0];
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

            public String getPrice() {
                return price;
            }
        }

        List<Test> foundValue2 = expectClass2.receipt.result.subResults.get(
                        0).items.parallelStream()
                .map(item -> new Test(item.name == null ? "null" : item.name.formatted.value,
                        item.count == null ? "null" : item.count.formatted.value,
                        item.price.formatted == null ? "null" : item.price.formatted.value))
                .collect(Collectors.toList());

        Assertions.assertThat(foundValue2.get(1).getName()).isEqualTo("라라스윗)초코파인트474ml 행사");
        Assertions.assertThat(foundValue2.get(1).getCount()).isEqualTo("1");
        Assertions.assertThat(foundValue2.get(1).getPrice()).isEqualTo("null");

        Assertions.assertThat(foundValue2).isNotEmpty();
    }
}
