package com.smu.smuenip.infrastructure.config.batch;

import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.repository.UserRepository;
import com.smu.smuenip.infrastructure.config.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserScoreUpdateJob extends QuartzJobBean {


    private final UserRepository userRepository;


    private final RedisService redisService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        redisService.flushRank();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            redisService.saveOrUpdateUser(user);
        }
    }
}