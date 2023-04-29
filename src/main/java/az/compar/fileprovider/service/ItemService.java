package az.compar.fileprovider.service;

import az.compar.fileprovider.entity.Item;
import az.compar.fileprovider.entity.User;
import az.compar.fileprovider.repository.ItemRepository;
import az.compar.fileprovider.util.dto.item.PostItemDto;
import az.compar.fileprovider.util.exceptions.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
public class ItemService {
    private final ItemRepository itemRepository;
    private final FileService fileService;
    private final UserService userService;

    public ItemService(ItemRepository itemRepository, FileService fileService, UserService userService) {
        this.itemRepository = itemRepository;
        this.fileService = fileService;
        this.userService = userService;
    }

    public Item getItem(User owner, Long itemId) throws ItemNotFoundException {
        return this.itemRepository
                .findByIdAndOwner(itemId, owner)
                .orElseThrow(() -> new ItemNotFoundException("You don't have item with id = " + itemId));
    }

    public List<Item> getItems(User owner) {
        return itemRepository.findByOwner(owner);
    }

    public Item postItem(User owner, PostItemDto postItemDto) throws IOException {
        List<Item> items = getItems(owner);
        Item item = new Item();
        item.setOwner(owner);
        item.setType(postItemDto.getFile().getContentType());
        String originalFileName = postItemDto.getName() != null && !postItemDto.getName().isBlank() ?
                postItemDto.getName() : postItemDto.getFile().getOriginalFilename();
        String fileName = items.stream().anyMatch(i -> i.getName().equals(originalFileName))
                ? constructAltFileName(originalFileName)
                : originalFileName;
        item.setName(fileName);
        String filePath = this.fileService.saveFile(owner, fileName, postItemDto.getFile());
        item.setFilePath(filePath);
        this.itemRepository.save(item);
        return item;
    }

    public void deleteItem(User owner, Long itemId) throws ItemNotFoundException {
        Item item = getItem(owner, itemId);
        this.fileService.deleteFile(item.getFilePath());
        this.itemRepository.delete(item);
    }

    private String constructAltFileName(String originalFileName) {
        if (!originalFileName.contains(".")) {
            return originalFileName + "_" + LocalDateTime.now();
        }
        String[] elems = originalFileName.split("\\.");
        String pureFileName = elems[0] + "_" + LocalDateTime.now();
        elems[0] = pureFileName;
        return String.join(".", elems);
    }
}
