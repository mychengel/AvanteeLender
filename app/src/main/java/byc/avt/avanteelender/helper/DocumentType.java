package byc.avt.avanteelender.helper;

public enum DocumentType {
    PROCURATION("internal/portofolio/download_suratkuasa"),
    AGREEMENT("internal/portofolio/download_suratperjanjian"),
    PROCURATION_LOAN("internal/portofolio/suratkuasa/"),
    AGREEMENT_LOAN("internal/portofolio/agreement/"),
    FACTSHEET_LOAN("internal/pendanaan/download_factsheet/");

    private final String endpoint;

    DocumentType(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
