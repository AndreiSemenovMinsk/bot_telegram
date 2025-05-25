package ru.skidoz.service.command_impl.search_goods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import ru.skidoz.model.pojo.search.search.Search;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.service.search.SearchService;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import ru.skidoz.util.MenuTypeEnum;
import ru.skidoz.util.Structures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;


/**
 * @author andrey.semenov
 */
@Component
public class SearchCommand implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private SearchService searchService;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = null;/* = initialLevel.convertToLevel(level,
                true,
                true);*/

        Long chatId = users.getChatId();

        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++Search+++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println(update.getCallbackQuery() != null);

        System.out.println(update.getMessage() != null);

        List<Product> products = new ArrayList<>();
        List<Product> productDTOs = new ArrayList<>();
        boolean searchProducts = false;

        if (update.getCallbackQuery() != null) {

            String code = "";
            Integer abstractGroupEntityId = null;
            if (update.getCallbackQuery().getData().startsWith("@" + MenuTypeEnum.LEVEL_CHOICER)) {

                resultLevel = initialLevel.convertToLevel(level,
                        true,//false,
                        true);//false);

                code = update.getCallbackQuery().getData().substring(("@" + MenuTypeEnum.LEVEL_CHOICER).length() + 20);
                System.out.println("code***" + code);

                /*resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_RESULT,
                        true,
                        false);*/

                Message message = new Message(null, Map.of(RU, "Введите название для уточнения поиска"));
                resultLevel.addMessage(message);

                searchProducts = true;

                abstractGroupEntityId = Structures.parseInt(code);
                products = productCacheRepository.findAllByCategory_IdAndActiveIsTrue(abstractGroupEntityId);

            } else {
                resultLevel = initialLevel.convertToLevel(level,
                        true,
                        true);
                searchProducts = false;

                System.out.println("update.getCallbackQuery().getData().equals(initialLevel.level_SEARCH.getIdString())*"
                        + update.getCallbackQuery().getData().equals(initialLevel.level_SEARCH.getIdString()));

                if (update.getCallbackQuery().getData().equals(initialLevel.level_SEARCH.getIdString())) {

                    abstractGroupEntityId = null;
                } else {
                    code = update.getCallbackQuery().getData();
                    /*resultLevel = initialLevel.convertToLevel(level,
                            true,
                            true);*/
                    abstractGroupEntityId = Structures.parseInt(code);
                }
                Message message = new Message(null, Map.of(RU, "Вы можете ввести название для поиска или уточнить сегмент"));
                resultLevel.addMessage(message);
            }

            System.out.println("-------code" + code);

            //Cat category = categoryRepository.findById(abstractGroupEntityId);
            //AbstractGroupEntity abstractGroupEntity = abstractGroupEntityRepository.findById(abstractGroupEntityId);
            users.setCurrentSearchAbstractGroupEntity(abstractGroupEntityId);
            userCacheRepository.save(users);
            //resultLevel = level;
        } else if (update.getMessage() != null) {

            resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH,
                    true,
                    false);
            //resultLevel.setMessages(new ArrayList<>());

            String inputText = update.getMessage().getText();

            System.out.println();
            System.out.println("**************++++++++++++++**************");
            System.out.println();
            System.out.println(inputText);

            Search search = new Search();
            Integer abstractGroupEntityId = users.getCurrentSearchAbstractGroupEntity();

            if (abstractGroupEntityId != null) {
                search.setCategorySuperGroupId(abstractGroupEntityId);
            }

            if (inputText != null
                    && !inputText.equals("")) {

                search.setSearch(inputText);
            }

            searchProducts = true;

            System.out.println();
            System.out.println("productDTOs.size()+++++" + productDTOs.size());

            List<Integer> valuesPointList = searchService.getProducts(search);
            int maxSize = 100;
            if (valuesPointList.size() < maxSize) {
                maxSize = valuesPointList.size();
            }

            List<Integer> finalProductIdList = valuesPointList.subList(0,  maxSize);

            productDTOs = finalProductIdList.stream()
                    .map(id -> productCacheRepository.findById(id)).filter(Objects::nonNull).collect(Collectors.toList());
        }


        if (searchProducts) {

            if (products.isEmpty()
                    && productDTOs.isEmpty()) {
                Message message = new Message(null, Map.of(RU, "Ничего не найдено"));
                resultLevel.addMessage(message);
            } else if (!products.isEmpty()) {
                int i = 0;
                for (Product product : products) {

                    String description = product.getName(users.getLanguage()) + " " + product.getPrice() + "р.";

                    System.out.println("description------------" + description);

                    Message message = new Message(null, i, null, product.getImage(), description);
                    resultLevel.addMessage(message);

                    ButtonRow row = new ButtonRow();
                    Button button = new Button(row, Map.of(RU, description), initialLevel.level_SEARCH_RESULT.getIdString() + product.getId()); //+ CATEGORY_GROUP.name()
                    row.add(button);
                    resultLevel.addRow(row);
                }
            } if (!productDTOs.isEmpty()) {
                int i = 0;
                for (Product product : productDTOs) {

                    String description = product.getName(RU) + " " + product.getPrice() + "р.";

                    System.out.println("description------------" + description);

                    Message message = new Message(null, i, null, product.getImage(), description);
                    resultLevel.addMessage(message);

                    ButtonRow row = new ButtonRow();
                    Button button = new Button(row, Map.of(RU, description), initialLevel.level_SEARCH_RESULT.getIdString() + product.getId()); //+ CATEGORY_GROUP.name()
                    row.add(button);
                    resultLevel.addRow(row);
                }
            }

            ButtonRow backRow = new ButtonRow();
            Button backButton = new Button(backRow, Map.of(RU, "В начало"), initialLevel.level_INITIALIZE.getIdString());
            backRow.add(backButton);
            resultLevel.addRow(backRow);
        }

//        users.setCurrentSearchAbstractGroupEntity(null);
//        userRepository.saveByMap(users);

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}