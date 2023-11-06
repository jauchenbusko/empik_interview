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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserDataService {

    @Value("${github.api.userinfo.url}")
    private String GITHUB_API_USER_INFO_URL;

    private final UserDataRepository userDataRepository;
    private final RestTemplate restTemplate;

    public synchronized UserDataDTO getUserData(String userName) {
        GithubUserDataDTO userDataResponse = getGithubUserData(userName);

        if (Objects.isNull(userDataResponse)) {
            log.warn("User data for {} is null", userName);
            return UserDataDTO.builder().build();
        }

        int interactionNumber = getInteractionNumber(userName);
        double calculations = calculate(userDataResponse);
        log.info("Interaction with user {} number {}, calculations: {}", userName, interactionNumber, calculations);

        return mapToDataDto(userDataResponse, calculations);
    }

    private GithubUserDataDTO getGithubUserData(String userName) {
        try {
            ResponseEntity<GithubUserDataDTO> response =
                    restTemplate.exchange(GITHUB_API_USER_INFO_URL + userName, HttpMethod.GET, null, GithubUserDataDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException.Forbidden exception) {
            log.warn("API rate limit exceeded. Authenticated requests get a higher rate limit. Check out the documentation for more details.");
            return new GithubUserDataDTO();
        }
    }

    private int getInteractionNumber(String username) {
        UserDataJpa userRequest = userDataRepository.findById(username)
                .orElse(new UserDataJpa(username, 0));
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
