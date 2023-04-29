package az.compar.fileprovider.util.dto.item;

import az.compar.fileprovider.entity.Item;

public class GetItemDto {
    protected Long id;
    protected String name;

    public GetItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
