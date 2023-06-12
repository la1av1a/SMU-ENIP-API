package com.smu.smuenip.domain.approve;

import com.smu.smuenip.domain.approve.entity.Approve;
import com.smu.smuenip.domain.recycledImage.entity.RecycledImage;
import com.smu.smuenip.domain.recycledImage.service.RecycledImageProcessService;
import com.smu.smuenip.domain.user.model.User;
import com.smu.smuenip.domain.user.serivce.UserService;
import com.smu.smuenip.enums.message.meesagesDetail.MessagesFail;
import com.smu.smuenip.infrastructure.config.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApproveProcessService {

    private final ApproveService approveService;
    private final RecycledImageProcessService recycledImageProcessService;
    private final UserService userService;

    @Transactional
    public void processRecycledImage(Long purchasedItemId, Long adminId, boolean isApproved) {

        if (approveService.existsByRecycledImageId(purchasedItemId)) {
            throw new BadRequestException("이미 확인된 이미지입니다.");
        }

        RecycledImage recycledImage = recycledImageProcessService.findRecycledById(purchasedItemId);
        User admin = userService.findUserById(adminId);

        recycledImage.setApprovedBy(admin);
        recycledImage.setApproved(isApproved);
        Approve approve = approveService.createApprove(recycledImage, admin, isApproved);
        approveService.saveApprove(approve);
    }

    @Transactional
    public void changeApprove(Long approveId, boolean isApproved) {
        Approve approve = approveService.findById(approveId);

        if (approve.isApproved() == isApproved) {
            log.info(String.valueOf(approve.isApproved()));
            log.info(String.valueOf(isApproved));
            throw new BadRequestException(MessagesFail.ALREADY_APPROVED_OR_REJECTED.getMessage());
        }

        approve.setApproved(isApproved);
    }

    public boolean checkIsNotApproved(Long recycledImageId) {

        if (approveService.existsByRecycledImageId(recycledImageId)) {
            throw new BadRequestException(MessagesFail.ALREADY_APPROVED_OR_REJECTED.getMessage());
        }

        return false;
    }
}