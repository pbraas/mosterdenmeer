package nl.mosterdenmeer.mosterdenmeer.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class TurnstileService {

    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";
    private static final boolean USE_JVM_OPTIONS = false;
    private static final Logger LOGGER = Logger.getLogger(TurnstileService.class.getName());

    // Temporary test keys from Cloudflare docs. Safe for local troubleshooting only.
    // Set USE_JVM_OPTIONS=true once production JVM options are correct.
    private static final String FALLBACK_SITE_KEY = "1x00000000000000000000AA";
    private static final String FALLBACK_SECRET   = "1x0000000000000000000000000000000AA";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getSiteKey() {
        if (!USE_JVM_OPTIONS) {
            return FALLBACK_SITE_KEY;
        }
        String value = System.getProperty("TURNSTILE_SITE_KEY", "").trim();
        return value.isBlank() ? FALLBACK_SITE_KEY : value;
    }

    public boolean isEnabled() {
        return !getSiteKey().isBlank() && !getSecret().isBlank();
    }

    public boolean verify(String token, String remoteIp) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String body = "secret=" + enc(getSecret())
                + "&response=" + enc(token);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(VERIFY_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.warning("Turnstile verify failed: HTTP status " + response.statusCode());
                return false;
            }

            String compactBody = response.body().replace(" ", "").replace("\n", "").replace("\r", "");
            boolean success = compactBody.contains("\"success\":true");
            if (!success) {
                LOGGER.warning("Turnstile verify rejected token. Response=" + response.body());
            }
            return success;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Turnstile verify request failed", e);
            return false;
        }
    }

    private String getSecret() {
        if (!USE_JVM_OPTIONS) {
            return FALLBACK_SECRET;
        }
        String value = System.getProperty("TURNSTILE_SECRET", "").trim();
        return value.isBlank() ? FALLBACK_SECRET : value;
    }

    private String enc(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

