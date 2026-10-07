package com.spring.soap.platform.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GitHubMcpTools {

    private final RestClient restClient;

    public GitHubMcpTools(RestClient.Builder builder,
                          @Value("${github.token:}") String token) {
        RestClient.Builder b = builder
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28");
        // Optional: without a token you get 60 requests/hour and public repos only
        if (!token.isBlank()) {
            b.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        this.restClient = b.build();
    }

    @McpTool(name = "listGitHubRepos", description = "Lists public GitHub repositories of a user, most recently updated first")
    public List<GitHubRepo> listRepos(
            @McpToolParam(description = "GitHub username", required = true) String username) {
        return restClient.get()
                .uri("/users/{username}/repos?sort=updated&per_page=30", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public record GitHubRepo(
            String name,
            @JsonProperty("full_name") String fullName,
            String description,
            String language,
            @JsonProperty("html_url") String htmlUrl,
            @JsonProperty("stargazers_count") int stars,
            @JsonProperty("updated_at") String updatedAt) {
    }
}
