package az.compar.fileprovider.util.dto.item;

import az.compar.fileprovider.entity.Item;

public class GetItemWithDownloadDto extends GetItemDto {
    protected String link;

    public GetItemWithDownloadDto(Item item) {
        super(item);
        this.link = "http://localhost:8080/items/download/" + item.getId();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
