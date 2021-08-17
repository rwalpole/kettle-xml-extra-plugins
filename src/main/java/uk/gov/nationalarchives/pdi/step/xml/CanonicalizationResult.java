package uk.gov.nationalarchives.pdi.step.xml;

public class CanonicalizationResult {

    private String canonicalXml;
    private StringBuilder errorMessageBuilder = new StringBuilder();
    private Boolean hasErrors = false;
    private Long errorCount = 0L;

    public CanonicalizationResult(final String canonicalXml) {
        this.canonicalXml = canonicalXml;
    };

    public String getCanonicalXml() {
        return canonicalXml;
    }

    public void setCanonicalXml(String canonicalXml) {
        this.canonicalXml = canonicalXml;
    }

    public Boolean hasErrors() {
        return hasErrors;
    }

    public void setHasErrors(final Boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void incrementErrorCount() {
        errorCount++;
    }

    public String getAllErrors() {
        return errorMessageBuilder.toString();
    }

    public void appendError(String errorMessage) {
        errorMessageBuilder.append(errorMessage);
    }

}
