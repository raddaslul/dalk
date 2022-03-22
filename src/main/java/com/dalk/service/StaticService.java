package com.dalk.service;
import com.dalk.domain.Item;
import com.dalk.domain.ItemType;
import com.dalk.domain.Ranking;
import com.dalk.domain.User;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.RankRepository;
import com.dalk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class StaticService {

    public static RankRepository rankRepository;
    public static UserRepository userRepository;
    public static ItemRepository itemRepository;

    @Autowired
    public StaticService(ItemRepository itemRepository, RankRepository rankRepository, UserRepository userRepository) {
        StaticService.itemRepository = itemRepository;
        StaticService.rankRepository = rankRepository;
        StaticService.userRepository = userRepository;
    }

    public static Long changeItem(User user, ItemType itemType) {
        Item item = itemRepository.findByUser_IdAndItemCode(user.getId(), itemType.getItemCode());
        return item.getCnt();
    }

    public static void saveRank() {
        List<User> top3rankList = userRepository.findTop3ByOrderByExDescCreatedAtDesc();

        Ranking ranking1 = new Ranking(1L, top3rankList.get(0));
        rankRepository.save(ranking1);
        Ranking ranking2 = new Ranking(2L, top3rankList.get(1));
        rankRepository.save(ranking2);
        Ranking ranking3 = new Ranking(3L, top3rankList.get(2));
        rankRepository.save(ranking3);
    }
}
