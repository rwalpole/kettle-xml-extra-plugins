package uk.gov.nationalarchives.pdi.step.xml;

public class CanonicalizationResult {

    private final String canonicalXml;
    private String errorMessage = "";
    private Boolean hasErrors = false;
    private Long errorCount = 0L;

    public CanonicalizationResult(final String canonicalXml) {
        this.canonicalXml = canonicalXml;
    };

    public String getCanonicalXml() {
        return canonicalXml;
    }

//    public void setCanonicalXml(String canonicalXml) {
//        this.canonicalXml = canonicalXml;
//    }

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
