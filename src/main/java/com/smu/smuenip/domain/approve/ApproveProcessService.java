package com.smu.smuenip.domain.approve;

import com.smu.smuenip.domain.approve.entity.Approve;
import com.smu.smuenip.domain.recycledImage.RecycledImage;
import com.smu.smuenip.domain.recycledImage.RecycledImageService;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApproveProcessService {

    private final ApproveService approveService;
    private final RecycledImageService recycledImageService;
    private final UserService userService;

    public void processRecycledImage(Long purchasedItemId, Long adminId, boolean isApproved) {

        if (approveService.existsByRecycledImageId(purchasedItemId)) {
            throw new BadRequestException("이미 승인된 이미지입니다.");
        }

        RecycledImage recycledImage = recycledImageService.findRecycledById(purchasedItemId);
        User admin = userService.findUserById(adminId);

        Approve approve = approveService.createApprove(recycledImage, admin, isApproved);
        approveService.saveApprove(approve);
    }

    public void changeApprove(Long approveId, boolean isApproved) {
        Approve approve = approveService.findById(approveId);

        if (approve.isApproved() == isApproved) {
            throw new BadRequestException(MessagesFail.ALREADY_APPROVED_OR_REJECTED.getMessage());
        }

        approve.setApproved(isApproved);
    }
}
