package competnion.global.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        createdAt = now;
        modifiedAt = now;
    }

    @PreUpdate
    protected void preUpdate() {
        modifiedAt = LocalDateTime.now().withNano(0);
    }
}
