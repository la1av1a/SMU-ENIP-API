package com.smu.smuenip.application.rank;

import com.smu.smuenip.domain.Rank.RankService;
import com.smu.smuenip.domain.dto.RankDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class RankController implements RankControllerSwagger {

    private final RankService rankService;

    @Override
    @GetMapping("/ranking")
    public List<RankDto> getRanking(@RequestParam(value = "size", defaultValue = "100") int size,
        @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return rankService.getRanking(size, offset);
    }
}
