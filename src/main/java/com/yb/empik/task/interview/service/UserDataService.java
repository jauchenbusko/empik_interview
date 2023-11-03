package com.yb.empik.task.interview.service;

import com.yb.empik.task.interview.entity.GithubUserDataDTO;
import com.yb.empik.task.interview.entity.UserDataDTO;
import com.yb.empik.task.interview.entity.UserDataJpa;
import com.yb.empik.task.interview.jpa.UserDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDataService {

    @Value("${github.api.url}")
    private String GITHUB_USER_URL;

    private final UserDataRepository userDataRepository;
    private final RestTemplate restTemplate;

    public UserDataDTO getUserData(String login) {
        ResponseEntity<GithubUserDataDTO> response =
                restTemplate.exchange(GITHUB_USER_URL + login, HttpMethod.GET, null, GithubUserDataDTO.class);
        GithubUserDataDTO userDataResponse = response.getBody();

        if (Objects.isNull(userDataResponse)) {
            log.warn("User data for {} is null", login);
            return UserDataDTO.builder().build();
        }

        int interactionNumber = getInteractionNumber(login);
        double calculations = calculate(userDataResponse);
        log.info("Interaction with user {} number {}, calculations: {}", login, interactionNumber, calculations);

        return mapToDataDto(userDataResponse, calculations);
    }

    private int getInteractionNumber(String login) {
        UserDataJpa userRequest = userDataRepository.findById(login).orElse(new UserDataJpa(login, 0));
        int interactionsWithUser = userRequest.getRequestCount() + 1;
        userRequest.setRequestCount(interactionsWithUser);
        userDataRepository.save(userRequest);
        return interactionsWithUser;
    }

    private double calculate(GithubUserDataDTO userDataDTO) {
        int followers = userDataDTO.getFollowers();
        int publicRepos = userDataDTO.getPublic_repos();

        if (followers == 0 || publicRepos == 0) {
            log.warn("User {} has {} followers and {} public repos, result of calculations are uncountable",
                    userDataDTO.getLogin(), followers, publicRepos);
            return 0.0;
        }

        return 6.0 / followers * 2 + publicRepos;
    }

    private UserDataDTO mapToDataDto(GithubUserDataDTO githubUserDataDTO, double calculations) {
        return UserDataDTO.builder().id(githubUserDataDTO.getId())
                .name(githubUserDataDTO.getName())
                .type(githubUserDataDTO.getType())
                .login(githubUserDataDTO.getLogin())
                .createdAt(githubUserDataDTO.getCreated_at())
                .avatarUrl(githubUserDataDTO.getAvatar_url())
                .calculations(calculations)
                .build();
    }

}
