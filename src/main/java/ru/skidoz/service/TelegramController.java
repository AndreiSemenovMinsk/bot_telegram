package ru.skidoz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


@RestController
@RequestMapping(path = "/")
public class TelegramController {

    @Autowired
    public TelegramBotWebhook telegramBot;
/*
    public TelegramController() {
        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        options.setMaxThreads(4);
        this.telegramBot = new TelegramBot(options);
    }*/

    @PostMapping(value = "telegram/{id}/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update, @PathVariable("id") String id) {

        System.out.println(id);

//        TelegramBotWebhook.Runner runner = telegramBot.getTelegramKey(id);

//        if (runner != null){
            return telegramBot.onUpdateReceived(update, id);
//        }
//        return null;
    }
}
