package com.wiensquare.user.cronjob;

import com.wiensquare.user.domain.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedDelay = 60 * 60 * 1000) // every hour
    @Transactional
    public void cleanup() {
        log.info("Cleaning up expired refresh tokens");

        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

}
