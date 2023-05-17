package com.smu.smuenip.domain.Rank;

import com.smu.smuenip.domain.dto.RankDto;
import com.smu.smuenip.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankService {

    private final UserRepository userRepository;

    public List<RankDto> getRanking(int size, int offset) {
        List<Object[]> userScoreList = userRepository.findUserScore(size, offset);

        return userScoreList.stream()
            .map(o -> new RankDto(String.valueOf(o[0]),
                Integer.parseInt(String.valueOf(o[1])),
                Long.parseLong(String.valueOf(o[2]))))
            .collect(Collectors.toList());
    }
}
