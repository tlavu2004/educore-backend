package com.tlavu.educore.auth.shared.domain.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

