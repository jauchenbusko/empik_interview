package com.yb.empik.task.interview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserDataDTO {
    int id;
    String login;
    String name;
    String type;
    String avatar_url;
    Date created_at;
    int followers;
    int public_repos;
}
