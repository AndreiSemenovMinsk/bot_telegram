package ru.skidoz.service.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.service.search.ShopSearchHandler;
import ru.skidoz.util.TextParser;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class ShopSearchTasklet implements Tasklet {

    @Autowired
    private ShopCacheRepository shopRepository;


    public List<String> shopNames = new ArrayList<>();


    @Override
    public RepeatStatus execute(StepContribution stepContribution,
                                ChunkContext chunkContext) {

        System.out.println("----------------------------------CacheSearchTasklet start--------------------------------------");


        shopNames = shopRepository.findAll().stream().map(Shop::getName).toList();

        ShopSearchHandler.pairs = shopNames;
        ShopSearchHandler.wordPhraseList = TextParser.init(ShopSearchHandler.pairs, ShopSearchHandler.parts);

        System.out.println("----------------------------------CacheSearchTasklet finish--------------------------------------");

        return RepeatStatus.FINISHED;
    }


}
