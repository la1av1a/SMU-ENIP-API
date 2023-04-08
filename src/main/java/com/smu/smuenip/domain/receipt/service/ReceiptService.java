package com.smu.smuenip.domain.receipt.service;

import com.smu.smuenip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final UserRepository userRepository;


}
