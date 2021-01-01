package org.project.smarttrack.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = false, updatable = false)
    protected Long id;

    @Column(nullable = false, columnDefinition = "int default 1")
    protected Integer isEnabled = 1;

    @Version
    @Column(nullable = false)
    protected Long version;

    @CreatedBy
    @Column(nullable = false)
    protected String createdBy;

    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date createdAt;

    @LastModifiedBy
    protected String updatedBy;

    @LastModifiedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date updatedAt;
}
