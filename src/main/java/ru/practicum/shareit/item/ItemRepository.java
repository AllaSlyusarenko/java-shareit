package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
//    public Item saveItem(Long userId, Item item);
//
//    public Item findItemById(Long id);
//
//    public List<Item> findAllUserItems(Long userId);
//
//    public Item updateItem(Long userId, Long id, Item item);
//
//    public List<Item> findItemByNameOrDescription(String text);
//
//    public void deleteItemById(Long id);
//@Query("select t from #{#entityName} t where t.attribute = ?1")
//List<T> findAllByAttribute(String attribute);

    List<Item> findAllByOwner(User user);

    //List<Item> findAllByNameContainingOrDescriptionContaining(String text);
    @Query(" select i from Item i " +
            "where  (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

//    @Query(nativeQuery = true, value = "select * from Items i " +
//            "where IS_AVAILABLE = true AND" +
//            " ( UPPER(NAME ) like UPPER('%:text%') or UPPER(description) like UPPER('%:text%'))")
//    List<Item> search(String text);
//    i.available='true' and (
//    List<Item> findAllByNameContaininq(String text);
//    List<Item> findAllByDescriptionContaininq(String text);
}
