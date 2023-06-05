package com.smu.smuenip.application.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/item/list")
    public void getRecycledImageList() {
        
    }
}
