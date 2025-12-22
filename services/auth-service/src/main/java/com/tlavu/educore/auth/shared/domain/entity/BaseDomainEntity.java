package com.tlavu.educore.auth.shared.domain.entity;

import com.tlavu.educore.auth.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class BaseDomainEntity<ID> {

    private ID id;
    private Instant createdAt;
    private Instant updatedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public void raiseDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BaseDomainEntity<?> that)) return false;
        if (getClass() != that.getClass()) return false;

        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
