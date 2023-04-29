package az.compar.fileprovider.controller;

import az.compar.fileprovider.entity.Item;
import az.compar.fileprovider.entity.User;
import az.compar.fileprovider.service.FileService;
import az.compar.fileprovider.service.ItemService;
import az.compar.fileprovider.util.WebFileUtils;
import az.compar.fileprovider.util.dto.item.GetItemDto;
import az.compar.fileprovider.util.dto.item.GetItemWithDownloadDto;
import az.compar.fileprovider.util.dto.item.PostItemDto;
import az.compar.fileprovider.util.exceptions.ItemNotFoundException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController extends BaseController {

    private final ItemService itemService;
    private final FileService fileService;

    public ItemController(ItemService itemService, FileService fileService) {
        this.itemService = itemService;
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@AuthenticationPrincipal User user,
                                           @RequestParam(value = "wd", defaultValue = "false") boolean withDownloads) {
        if (withDownloads) {
            return ResponseEntity.ok(
                    Collections.singletonMap(
                            "items",
                            this.itemService.getItems(user).stream().map(GetItemWithDownloadDto::new).collect(Collectors.toList()))
            );
        }
        return ResponseEntity.ok(
                Collections.singletonMap(
                        "items",
                        this.itemService.getItems(user).stream().map(GetItemDto::new).collect(Collectors.toList()))
        );
    }


    @GetMapping("/download/{itemId}")
    public HttpEntity<ByteArrayResource> downloadItem(@AuthenticationPrincipal User user,
                                                      @PathVariable("itemId") long itemId) throws ItemNotFoundException, IOException {
        Item item = this.itemService.getItem(user, itemId);
        File file = this.fileService.getFile(item.getFilePath());
        return WebFileUtils.downloadFile(item.getName(), item.getType(), file);

    }

    @GetMapping("/view/{itemId}")
    public HttpEntity<ByteArrayResource> viewItem(@AuthenticationPrincipal User user,
                                                  @PathVariable("itemId") long itemId) throws ItemNotFoundException, IOException {
        Item item = this.itemService.getItem(user, itemId);
        File file = this.fileService.getFile(item.getFilePath());
        return WebFileUtils.viewFile(item.getName(), item.getType(), file);
    }

    @PostMapping
    public ResponseEntity<Object> postItem(@AuthenticationPrincipal User user, PostItemDto postItemDto) throws IOException {
        this.itemService.postItem(user, postItemDto);
        return ResponseEntity.ok("Item added");
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<Object> deleteItem(@AuthenticationPrincipal User user, @PathVariable("itemId") long itemId) throws ItemNotFoundException {
        this.itemService.deleteItem(user, itemId);
        return ResponseEntity.ok("Item deleted");
    }
}
