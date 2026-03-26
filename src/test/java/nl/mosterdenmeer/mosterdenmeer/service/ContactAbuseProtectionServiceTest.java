package nl.mosterdenmeer.mosterdenmeer.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactAbuseProtectionServiceTest {

    @Test
    void shouldDetectVeryFastSubmissions() {
        ContactAbuseProtectionService service = new ContactAbuseProtectionService();
        long justNow = System.currentTimeMillis();

        assertTrue(service.isSubmittedTooFast(justNow));
    }

    @Test
    void shouldAllowNormalSubmissionTime() {
        ContactAbuseProtectionService service = new ContactAbuseProtectionService();
        long fiveSecondsAgo = System.currentTimeMillis() - 5000;

        assertFalse(service.isSubmittedTooFast(fiveSecondsAgo));
    }

    @Test
    void shouldRateLimitAfterMaxRequestsInWindow() {
        ContactAbuseProtectionService service = new ContactAbuseProtectionService();
        String key = "127.0.0.1";

        for (int i = 0; i < 4; i++) {
            assertFalse(service.isRateLimited(key));
            service.registerAttempt(key);
        }

        assertTrue(service.isRateLimited(key));
    }
}

