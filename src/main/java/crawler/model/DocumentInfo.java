package crawler.model;

/**
 * @author Girish Saraswat
 * POJO class
 */
public class DocumentInfo {

    private String uploadImagePath;
    private String downloadImagePath;
    private String header;
    private Long created;
    private Long updated;

    public String getUploadImagePath() {
        return uploadImagePath;
    }

    public void setUploadImagePath(String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }


    public String getDownloadImagePath() {
        return downloadImagePath;
    }

    public void setDownloadImagePath(String downloadImagePath) {
        this.downloadImagePath = downloadImagePath;
    }
}
