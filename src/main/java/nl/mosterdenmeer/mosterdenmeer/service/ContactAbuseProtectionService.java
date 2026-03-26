package nl.mosterdenmeer.mosterdenmeer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ContactAbuseProtectionService {

    private static final long MIN_SUBMIT_SECONDS = 3;
    private static final int MAX_REQUESTS_PER_WINDOW = 4;
    private static final long WINDOW_MINUTES = 10;

    private final Map<String, Deque<Instant>> attempts = new ConcurrentHashMap<>();

    public boolean isSubmittedTooFast(long formStartedAtMillis) {
        long elapsedMillis = System.currentTimeMillis() - formStartedAtMillis;
        return elapsedMillis < (MIN_SUBMIT_SECONDS * 1000);
    }

    public boolean isRateLimited(String key) {
        Deque<Instant> clientAttempts = attempts.computeIfAbsent(key, unused -> new ArrayDeque<>());
        pruneExpired(clientAttempts);
        return clientAttempts.size() >= MAX_REQUESTS_PER_WINDOW;
    }

    public void registerAttempt(String key) {
        Deque<Instant> clientAttempts = attempts.computeIfAbsent(key, unused -> new ArrayDeque<>());
        pruneExpired(clientAttempts);
        clientAttempts.addLast(Instant.now());
    }

    public String resolveClientKey(FacesContext facesContext) {
        if (facesContext == null) {
            return "unknown";
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        Object request = externalContext.getRequest();
        if (request instanceof HttpServletRequest httpServletRequest) {
            String forwarded = httpServletRequest.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
            return httpServletRequest.getRemoteAddr();
        }
        return "unknown";
    }

    private void pruneExpired(Deque<Instant> clientAttempts) {
        Instant cutoff = Instant.now().minus(WINDOW_MINUTES, ChronoUnit.MINUTES);
        while (!clientAttempts.isEmpty() && clientAttempts.peekFirst().isBefore(cutoff)) {
            clientAttempts.removeFirst();
        }
    }
}

