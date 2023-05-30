package com.smu.smuenip.domain.approve.entity;

import com.smu.smuenip.domain.recycledImage.RecycledImage;
import com.smu.smuenip.domain.user.model.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@NoArgsConstructor
public class Approve {

    @Id
    @Column(name = "approve_id")
    private Long id;

    @OneToOne
    @Column(name = "recyled_image_id")
    private RecycledImage recycledImage;

    @OneToOne
    @Column(name = "admin_id")
    private User admin;

    @Column(name = "is_approved")
    private boolean isApproved;

    @CreationTimestamp
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    public Approve(RecycledImage recycledImage, User admin, boolean isApproved) {
        this.recycledImage = recycledImage;
        this.admin = admin;
        this.isApproved = isApproved;
    }
}
