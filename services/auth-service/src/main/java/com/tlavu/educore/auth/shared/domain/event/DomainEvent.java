package com.tlavu.educore.auth.shared.domain.event;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredAt();
}

