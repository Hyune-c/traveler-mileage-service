package com.example.travelermileageservice.domain.review.entity;

import com.example.travelermileageservice.domain.base.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "REVIEW_ID")
    private UUID id;

    private String content;

    @Column(columnDefinition = "BINARY(16)", updatable = false)
    private UUID placeId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "POSITION")
    @JoinColumn(name = "REVIEW_ID")
    private List<AttachedPhoto> attachedPhotos;

    private Boolean deleted = false;

    public Review(final UUID reviewId, final UUID userId, final String content, final UUID placeId, final List<AttachedPhoto> attachedPhotos) {
        this.id = reviewId;
        this.content = content;
        this.placeId = placeId;
        this.attachedPhotos = new ArrayList<>(attachedPhotos);

        this.createdBy = userId;
        this.updatedBy = userId;
    }

    public void updateAttachedPhotos(final List<AttachedPhoto> attachedPhotos) {
        this.attachedPhotos.clear();
        this.attachedPhotos.addAll(attachedPhotos);
    }

    public void updateContent(final String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }
}
