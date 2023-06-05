package com.smu.smuenip.application.rank;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.rank.RankService;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.infrastructure.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor

public class RankController implements RankControllerSwagger {

    private final RankService rankService;
    private final RedisService redisService;

    @Override
    @GetMapping("/rank")
    public List<RankDto> getRanking(@RequestParam(value = "size", defaultValue = "20") int size,
                                    @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return rankService.getRanking(size, offset);
    }

    @GetMapping("/rank2")
    public Set<User> getRanking2() {
        return redisService.getUsers();
    }
}
