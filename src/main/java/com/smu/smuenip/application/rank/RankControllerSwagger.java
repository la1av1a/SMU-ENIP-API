package com.smu.smuenip.application.rank;

import com.smu.smuenip.domain.dto.RankDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "유저 랭킹")
public interface RankControllerSwagger {

    @ApiOperation("유저 랭킹 리스트")
    @GetMapping("/ranking")
    public List<RankDto> getRanking(@RequestParam(value = "size", defaultValue = "100") int size,
        @RequestParam(value = "offset", defaultValue = "0") int offset);

}
