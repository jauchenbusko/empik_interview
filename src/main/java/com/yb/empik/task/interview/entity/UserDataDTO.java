package com.yb.empik.task.interview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    int id;
    String login;
    String name;
    String type;
    String avatarUrl;
    Date createdAt;
    double calculations;
}
