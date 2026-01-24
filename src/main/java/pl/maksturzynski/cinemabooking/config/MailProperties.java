package pl.maksturzynski.cinemabooking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String apiKey;
    private String baseUrl;
    private String sender;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
}
