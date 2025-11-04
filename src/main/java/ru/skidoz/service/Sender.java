package ru.skidoz.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ru.skidoz.model.pojo.telegram.LevelResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
@RequiredArgsConstructor
public class Sender {

    private List<Runner> runners = new ArrayList<>();
    @PostConstruct
    public void onStart() {
        for (int i = 0; i < 16; i++) {
            runners.add(new Runner());
        }
    }

    private final BasicThreadFactory factory = new BasicThreadFactory.Builder()
            .namingPattern("%d")
            .priority(Thread.MAX_PRIORITY)
            .build();
    private final ThreadPoolExecutor executorService = new MyThreadPoolExecutor(16, factory);
    private ConcurrentLinkedQueue<LevelResponse> WRITE_QUEUE = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<LevelResponse> WRITE_AFTER_SAVE_QUEUE = new ConcurrentLinkedQueue<>();

    void initExecutionAsync() {
        executorService.submit(() -> {
            LevelResponse levelChatList = null;
            while ((levelChatList = WRITE_QUEUE.poll()) != null) {
                try {
                    final int threadInd = Integer.parseInt(Thread.currentThread().getName());
                    Runner r = runners.get(threadInd);
                    r.processSend(levelChatList.getLevelChats(), levelChatList.getKey());

                } catch (Exception e) {
                    e.printStackTrace();
                    java.lang.System.out.println(e);
                }
            }
        });
    }

    public void initExecutionAfterSave() {
        executorService.submit(() -> {
            LevelResponse levelChatList = null;
            while ((levelChatList = WRITE_AFTER_SAVE_QUEUE.poll()) != null) {
                try {
                    final int threadInd = Integer.parseInt(Thread.currentThread().getName());
                    Runner r = runners.get(threadInd);
                    r.processSend(levelChatList.getLevelChatsAfterSave(), levelChatList.getKey());

                } catch (Exception e) {
                    e.printStackTrace();
                    java.lang.System.out.println(e);
                }
            }
        });
    }

    public void addAsync(LevelResponse levelResponse) {
        WRITE_QUEUE.offer(levelResponse);
        initExecutionAsync();
    }

    public void addAfterSave(LevelResponse levelResponse) {
        WRITE_AFTER_SAVE_QUEUE.offer(levelResponse);
    }

}
