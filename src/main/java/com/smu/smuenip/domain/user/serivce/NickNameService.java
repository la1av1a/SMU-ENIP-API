package com.smu.smuenip.domain.user.serivce;

import com.smu.smuenip.domain.user.repository.NickNameRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NickNameService {

    private final NickNameRepository nickNameRepository;
    Random random = new Random();

    public String getRandomNickName() {

        return nickNameRepository.findRandomNickDepth1() + nickNameRepository.findRandomNickDepth2()
            + random.nextInt(100);
    }
}
