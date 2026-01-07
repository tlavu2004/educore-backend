package com.tlavu.educore.auth.shared.domain.port;

import java.time.Clock;

public interface ClockProvider {

    Clock clock();
}
