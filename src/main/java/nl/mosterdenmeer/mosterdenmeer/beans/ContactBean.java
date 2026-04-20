package nl.mosterdenmeer.mosterdenmeer.beans;

import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import nl.mosterdenmeer.mosterdenmeer.service.ContactAbuseProtectionService;
import nl.mosterdenmeer.mosterdenmeer.service.ContactMailService;
import nl.mosterdenmeer.mosterdenmeer.service.TurnstileService;

import java.io.Serializable;
import java.util.regex.Pattern;

@Named
@ViewScoped
public class ContactBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_NAME_LENGTH = 80;
    private static final int MAX_EMAIL_LENGTH = 120;
    private static final int MAX_SUBJECT_LENGTH = 140;
    private static final int MAX_MESSAGE_LENGTH = 1000;
    private static final Pattern SIMPLE_EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Inject
    private ContactMailService contactMailService;

    @Inject
    private ContactAbuseProtectionService abuseProtectionService;

    @Inject
    private TurnstileService turnstileService;

    private String name;
    private String email;
    private String subject;
    private String message;
    private String website;
    private String captchaToken;

    private long formStartedAtMillis;

    public ContactBean() {
        formStartedAtMillis = System.currentTimeMillis();
    }

    public String send() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Bots vaak vullen verborgen velden in en verzenden te snel.
        if (website != null && !website.isBlank()) {
            return "/views/confirm?faces-redirect=true";
        }

        String rateLimitKey = abuseProtectionService.resolveClientKey(facesContext);
        if (abuseProtectionService.isRateLimited(rateLimitKey)) {
            facesContext.addMessage("contactForm:growl",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Te veel aanvragen in korte tijd. Probeer het over enkele minuten opnieuw.", null));
            return null;
        }
        abuseProtectionService.registerAttempt(rateLimitKey);

        if (abuseProtectionService.isSubmittedTooFast(formStartedAtMillis)) {
            facesContext.addMessage("contactForm:growl",
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Uw bericht is te snel verzonden. Controleer de gegevens en probeer opnieuw.", null));
            formStartedAtMillis = System.currentTimeMillis();
            return null;
        }

        normalizeFields();
        if (!validateFields(facesContext) || !validateCaptcha(facesContext)) {
            formStartedAtMillis = System.currentTimeMillis();
            return null;
        }

        try {
            contactMailService.sendContactMail(name, email, subject, message);
            return "/views/confirm?faces-redirect=true";
        } catch (Exception e) {
            // Log de volledige uitzondering en stacktrace voor diagnose
            e.printStackTrace(); // Je kunt dit vervangen door een logger als die beschikbaar is
            String errorMsg = (e.getCause() != null) ? e.getCause().toString() : e.toString();
            facesContext.addMessage("contactForm:growl",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Er is een fout opgetreden bij het versturen. Probeer het later opnieuw.", errorMsg));
            return null;
        }
    }

    private boolean validateCaptcha(FacesContext facesContext) {
        if (!turnstileService.isEnabled()) {
            facesContext.addMessage("contactForm:growl",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Captcha-configuratie ontbreekt op de server.", null));
            return false;
        }

        String tokenFromHiddenField = captchaToken;
        String tokenFromRequest = facesContext.getExternalContext().getRequestParameterMap().get("cf-turnstile-response");
        String token = (tokenFromHiddenField != null && !tokenFromHiddenField.isBlank())
                ? tokenFromHiddenField
                : tokenFromRequest;

        if (token == null || token.isBlank()) {
            facesContext.addMessage("contactForm:captcha",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Captcha-token ontbreekt. Wacht tot de captcha klaar is en probeer opnieuw.", null));
            return false;
        }

        String remoteIp = abuseProtectionService.resolveClientKey(facesContext);
        if (!turnstileService.verify(token, remoteIp)) {
            facesContext.addMessage("contactForm:captcha",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Captcha-validatie mislukt. Vernieuw de pagina en probeer opnieuw.", null));
            captchaToken = null;
            return false;
        }

        return true;
    }

    private boolean validateFields(FacesContext facesContext) {
        boolean valid = true;

        if (name == null || name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            facesContext.addMessage("contactForm:name",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Naam is verplicht en maximaal 80 tekens.", null));
            valid = false;
        }
        if (email == null || email.isBlank() || email.length() > MAX_EMAIL_LENGTH ||
                !SIMPLE_EMAIL_PATTERN.matcher(email).matches()) {
            facesContext.addMessage("contactForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vul een geldig emailadres in.", null));
            valid = false;
        }
        if (subject == null || subject.isBlank() || subject.length() > MAX_SUBJECT_LENGTH) {
            facesContext.addMessage("contactForm:subject",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Onderwerp is verplicht en maximaal 140 tekens.", null));
            valid = false;
        }
        if (message == null || message.isBlank() || message.length() > MAX_MESSAGE_LENGTH) {
            facesContext.addMessage("contactForm:message",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bericht is verplicht en maximaal 1000 tekens.", null));
            valid = false;
        }

        return valid;
    }

    private void normalizeFields() {
        name = normalize(name);
        email = normalize(email);
        subject = normalize(subject);
        message = normalizeMultiline(message);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeMultiline(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.replace("\r\n", "\n").replace('\r', '\n').trim();
        return normalized.length() > MAX_MESSAGE_LENGTH ? normalized.substring(0, MAX_MESSAGE_LENGTH) : normalized;
    }

    // --- Getters & Setters ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getCaptchaToken() { return captchaToken; }
    public void setCaptchaToken(String captchaToken) { this.captchaToken = captchaToken; }

    public String getTurnstileSiteKey() { return turnstileService.getSiteKey(); }
}
