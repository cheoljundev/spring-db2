package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = em.find(Item.class, itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }

        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice));
            /**
             * loe 메서드는 "Less Than Or Equal"의 약자입니다.
             * 이 메서드는 일반적으로 조건문이나 쿼리 빌더에서 사용되며, 주어진 값보다 작거나 같은 값을 가지는지를 비교하는 데 사용됩니다
             * 위의 코드에서 item.price.loe(maxPrice)는 item 객체의 price 속성이 maxPrice 변수보다 작거나 같은지를 검사하는 조건을 나타냅니다.
             */
        }

        List<Item> result = query.select(item)
                .from(item)
                .where(builder)
                .fetch();

        return result;
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return query.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
            /**
             * loe 메서드는 "Less Than Or Equal"의 약자입니다.
             * 이 메서드는 일반적으로 조건문이나 쿼리 빌더에서 사용되며, 주어진 값보다 작거나 같은 값을 가지는지를 비교하는 데 사용됩니다
             * 위의 코드에서 item.price.loe(maxPrice)는 item 객체의 price 속성이 maxPrice 변수보다 작거나 같은지를 검사하는 조건을 나타냅니다.
             */
        }
        return null;
    }
}
