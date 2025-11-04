package ru.skidoz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.CommandProvider;
import ru.skidoz.service.command_impl.search_goods.SearchCommand;
import ru.skidoz.util.MenuTypeEnum;

import static ru.skidoz.service.command.CommandName.CATEGORY_SUPER_GROUP;

/**
 * @author andrey.semenov
 */
@Component
public class MenuCreator {

    @Autowired
    private SearchCommand search;
    @Autowired
    private CommandProvider commandProvider;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupCacheRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupCacheRepository;

    public void createMenu(Level levelSuper, MenuTypeEnum menuTypeEnum, User user) {

        System.out.println();
        System.out.println("**********************createMenu*********************" + levelSuper.getCallName());
        System.out.println();
        System.out.println();
        System.out.println(menuTypeEnum);
        System.out.println(levelSuper);

        Message message7_1 = new Message(levelSuper, 2, Map.of(LanguageEnum.RU, "Выберите группу"));
        messageCacheRepository.save(message7_1);
        levelSuper.addMessage(message7_1);

        //если мы сетаем кнкретную группу новому товару - то важно пройт до конца, а если это только для поиска - можно искать шире по супергруппам
        if (menuTypeEnum.equals(MenuTypeEnum.LEVEL_CHOICER)/* || menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)*/) {
            ButtonRow row_super = new ButtonRow(levelSuper);
            buttonRowCacheRepository.save(row_super);

            String buttonName = "Все товары";
            /*if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)){
                buttonName = "Поиск по всем товарам группы";
            }*/

            Button button_super = new Button(row_super, Map.of(LanguageEnum.RU, buttonName), "@" + MenuTypeEnum.LEVEL_CHOICER + levelSuper.getIdString() + "@" + CATEGORY_SUPER_GROUP.name());
            buttonCacheRepository.save(button_super);
            row_super.add(button_super);
            buttonRowCacheRepository.save(row_super);
            levelSuper.addRow(row_super);
        }

//        List<CatSG> categorySuperGroupList = categorySuperGroupRepository.findAll();
//        System.out.println("categorySuperGroupList.size()***" + categorySuperGroupList.size());

        List<CategorySuperGroup> categorySuperGroupDTOList = categorySuperGroupCacheRepository.findAll();
        //System.out.println("categorySuperGroupDTOList.size()***" + categorySuperGroupDTOList.size() + categorySuperGroupDTOList);

        for (CategorySuperGroup categorySuperGroup : categorySuperGroupDTOList) {

            List<CategoryGroup> categoryGroupList = categoryGroupCacheRepository.findAllByCategorySuperGroup_Id(categorySuperGroup.getId());
            Level levelGroup = levelCacheRepository.findByParentLevelIdAndCallName(levelSuper.getId(), categorySuperGroup.getAlias());
            if (levelGroup == null) {
                String callName = levelSuper.getCallName() + "*" + categorySuperGroup.getAlias() + menuTypeEnum;
                levelGroup = new Level(user, callName, levelSuper, false);

                //System.out.println("callName---" + callName + " user.getId() " + user.getId());

                levelCacheRepository.save(levelGroup);

                if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)) {
                    commandProvider.putCommand(callName, search);
                }
            }

            ButtonRow row_super = new ButtonRow(levelSuper);
            buttonRowCacheRepository.save(row_super);

            String callback1;
            if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)) {
                callback1 = "@" + MenuTypeEnum.LEVEL_CHOICER + levelGroup.getIdString() + "@" + categorySuperGroup.getId();
            } else {
                callback1 = levelGroup.getIdString();
            }

            Button button_super = new Button(row_super, Map.of(LanguageEnum.RU, categorySuperGroup.getAlias()), callback1);
            buttonCacheRepository.save(button_super);
            row_super.add(button_super);
            buttonRowCacheRepository.save(row_super);
            levelSuper.addRow(row_super);

            //System.out.println("categorySuperGroup***" + categorySuperGroup.getName(LanguageEnum.RU) + "***" + levelGroup.getIdString());

            if (menuTypeEnum.equals(MenuTypeEnum.LEVEL_CHOICER)/* || menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)*/) {
                ButtonRow row_group = new ButtonRow(levelGroup);
                buttonRowCacheRepository.save(row_group);

                String buttonName = "Все товары группы";
                /*if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)){
                    buttonName = "Поиск по всем товарам группы";
                }*/

                Button button_group = new Button(row_group, Map.of(LanguageEnum.RU, buttonName), "@" + MenuTypeEnum.LEVEL_CHOICER + levelSuper.getIdString() + "@" + categorySuperGroup.getId());
                buttonCacheRepository.save(button_group);
                row_group.add(button_group);
                buttonRowCacheRepository.save(row_group);
                levelGroup.addRow(row_group);
            }

            Message message12_1 = new Message(levelGroup, Map.of(LanguageEnum.RU, "Выберите тип"));
            messageCacheRepository.save(message12_1);
            levelGroup.addMessage(message12_1);
            for (CategoryGroup categoryGroup : categoryGroupList) {

                List<Category> categoryList = categoryCacheRepository.findByCategoryGroup_Id(categoryGroup.getId());
                Level levelCategory = levelCacheRepository.findByParentLevelIdAndCallName(levelGroup.getId(), categorySuperGroup.getAlias());
                if (levelCategory == null) {
                    String callName = levelSuper.getCallName() + "*" + categorySuperGroup.getName(LanguageEnum.RU) + "*" + categoryGroup.getAlias() + menuTypeEnum;
                    levelCategory = new Level(user, callName, levelGroup, false);
                    levelCacheRepository.save(levelCategory);

                    if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)) {
                        commandProvider.putCommand(callName, search);
                    }
                }

                ButtonRow row_group = new ButtonRow(levelGroup);
                buttonRowCacheRepository.save(row_group);

                String callback2;
                if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)) {
                    callback2 = "@" + MenuTypeEnum.LEVEL_CHOICER + levelCategory.getIdString() + "@" + categoryGroup.getId();
                } else {
                    callback2 = levelCategory.getIdString();
                }

                Button button_group = new Button(row_group, Map.of(LanguageEnum.RU, categoryGroup.getAlias()), callback2);
                buttonCacheRepository.save(button_group);
                row_group.add(button_group);
                buttonRowCacheRepository.save(row_group);
                levelGroup.addRow(row_group);

                if (menuTypeEnum.equals(MenuTypeEnum.LEVEL_CHOICER) /*|| menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)*/) {
                    ButtonRow row = new ButtonRow(levelCategory);
                    buttonRowCacheRepository.save(row);

                    String buttonName = "Все товары категории";
                    /*if (menuTypeEnum.equals(MenuTypeEnum.SEARCH_LEVEL_CHOICER)){
                        buttonName = "Поиск по всем товарам категории";
                    }*/

                    Button button = new Button(row, Map.of(LanguageEnum.RU, buttonName), "@" + MenuTypeEnum.LEVEL_CHOICER + levelSuper.getIdString() + "@" + categoryGroup.getId());
                    buttonCacheRepository.save(button);
                    row.add(button);
                    buttonRowCacheRepository.save(row);
                    levelCategory.addRow(row);
                }

                Message message13_1 = new Message(levelCategory, Map.of(LanguageEnum.RU, "Выберите подкатегорию"));
                messageCacheRepository.save(message13_1);
                levelCategory.addMessage(message13_1);
                for (Category category : categoryList) {

                    ButtonRow row = new ButtonRow(levelCategory);
                    buttonRowCacheRepository.save(row);
                    Button button = new Button(row, Map.of(LanguageEnum.RU, category.getAlias()), "@" + MenuTypeEnum.LEVEL_CHOICER + levelSuper.getIdString() + "@" + category.getId());
                    buttonCacheRepository.save(button);
                    row.add(button);
                    buttonRowCacheRepository.save(row);
                    levelCategory.addRow(row);
                }
                levelCacheRepository.save(levelCategory);
            }
            levelCacheRepository.save(levelGroup);
        }
        levelCacheRepository.save(levelSuper);
    }
}
