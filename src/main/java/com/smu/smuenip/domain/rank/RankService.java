package com.smu.smuenip.domain.rank;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.infrastructure.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RedisService redisService;

    public List<RankDto> getRanking() {

        return redisService.getUsers().stream()
                .map(this::userToRankDto)
                .collect(Collectors.toList());
    }

    private RankDto userToRankDto(User user) {
        return new RankDto(user.getNickName(), user.getScore());
    }
}
