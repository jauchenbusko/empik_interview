package com.yb.empik.task.interview.controller;

import com.yb.empik.task.interview.entity.UserDataDTO;
import com.yb.empik.task.interview.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserDataController {

    private final UserDataService userDataService;

    @GetMapping("/{login}")
    public ResponseEntity<UserDataDTO> getUserInfo(@PathVariable String login) {
        UserDataDTO userData = userDataService.getUserData(login);
        return ResponseEntity.ok(userData);
    }
}
