package az.compar.fileprovider.repository;

import az.compar.fileprovider.entity.Item;
import az.compar.fileprovider.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwner(Long itemId, User owner);

    List<Item> findByOwner(User owner);
}
