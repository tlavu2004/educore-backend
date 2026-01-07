package com.tlavu.educore.auth.shared.infrastructure.clock;

import com.tlavu.educore.auth.shared.domain.port.ClockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class SystemClockProvider implements ClockProvider {

    @Override
    public Clock clock() {
        return Clock.systemUTC();
    }
}