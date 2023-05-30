package com.smu.smuenip.domain.approve;

import com.smu.smuenip.domain.approve.entity.Approve;
import com.smu.smuenip.domain.approve.entity.ApproveRepository;
import com.smu.smuenip.domain.recycledImage.RecycledImage;
import com.smu.smuenip.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApproveService {

    private final ApproveRepository approveRepository;

    @Transactional
    public void saveApprove(Approve approve) {
        approveRepository.save(approve);
    }

    @Transactional(readOnly = true)
    public boolean existsByRecycledImageId(Long recycledImageId) {
        return approveRepository.existsByRecycledImageId(recycledImageId);
    }

    public Approve createApprove(RecycledImage recycledImage, User user, boolean isApproved) {
        return new Approve(recycledImage, user, isApproved);
    }
}
