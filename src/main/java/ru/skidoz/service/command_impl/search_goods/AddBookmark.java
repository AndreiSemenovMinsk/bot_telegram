package ru.skidoz.service.command_impl.search_goods;

import java.util.Collections;

import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddBookmark implements Command {

    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("--------------------------------AddBookmark--------------------------------");
        System.out.println(update.getCallbackQuery().getData());

        String code = update.getCallbackQuery().getData().substring(19);

        System.out.println(code);

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(level,
                true,
                true);

        final Product product = productCacheRepository.findById(Structures.parseInt(code));

       if (product != null) {
            System.out.println("product++++++" + product);
            //product.getCategorySet().forEach(System.out::println);
            Bookmark newBookmark = new Bookmark(e -> {
                e.setRadius(50);
                e.setProduct(product.getId());
                e.setShop(product.getShop().getId());
                e.setUser(users.getId());
                e.setViberNotify(false);
            });

           System.out.println(newBookmark);

            bookmarkCacheRepository.save(newBookmark);
            //sideService.inBookmark(newBookmark, users);
        };


        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
