package com.smu.smuenip.application.rank;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.rank.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class RankController implements RankControllerSwagger {

    private final RankService rankService;

    @Override
    @GetMapping("/rank")
    public List<RankDto> getRanking(@RequestParam(value = "value") String value,
                                    @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                    @RequestParam(value = "offset", defaultValue = "1", required = false) int offset) {

        return rankService.getRanking(value, size, offset);
    }
}
