package com.tlavu.educore.auth.shared.domain.event;

public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);
}
