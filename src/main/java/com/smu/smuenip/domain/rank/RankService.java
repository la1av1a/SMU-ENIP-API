package com.smu.smuenip.domain.rank;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import com.smu.smuenip.infrastructure.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RedisService redisService;

    public List<RankDto> getRanking(String value, int size, int offest) {
        if (value.equals("weight"))
            return redisService.getUsersWeight(size, offest).stream()
                    .map(this::userToRankDto)
                    .collect(Collectors.toList());
        else if (value.equals("score")) {
            return redisService.getUsersScore(size, offest).stream()
                    .map(this::userToRankDto)
                    .collect(Collectors.toList());
        }

        throw new BadRequestException("잘못된 요청입니다. 파라미터로 score 또는 weight를 입력해주세요.");
    }

    private RankDto userToRankDto(User user) {
        return new RankDto(user.getNickName(), user.getScore(), user.getWeight());
    }
}
