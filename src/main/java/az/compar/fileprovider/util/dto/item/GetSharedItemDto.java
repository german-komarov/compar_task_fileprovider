package az.compar.fileprovider.util.dto.item;

public class GetSharedItemDto {
    private String viewLink;
    private String downloadLink;

    public GetSharedItemDto(String viewLink, String downloadLink) {
        this.viewLink = viewLink;
        this.downloadLink = downloadLink;
    }

    public String getViewLink() {
        return viewLink;
    }

    public void setViewLink(String viewLink) {
        this.viewLink = viewLink;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
