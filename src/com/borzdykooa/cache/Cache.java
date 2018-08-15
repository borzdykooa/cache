package com.borzdykooa.cache;

import com.borzdykooa.entity.Trainer;
import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;

/*
Класс, который сохраняет и возвращает результаты последних MAX_SIZE запросов, а при модификации данных таблицы
(вставка, удаление, обновление) очищается
 */
public class Cache {

    private static final Logger log = Logger.getLogger(Cache.class);

    private LinkedHashMap<String, List<Trainer>> map;
    private ArrayDeque<String> queue;
    private final int MAX_SIZE = 5;
    private int actualSize;

    {
        map = new LinkedHashMap<String, List<Trainer>>();
        queue = new ArrayDeque<String>();
    }

    public boolean ifCacheContains(String method) {
        log.info("Method ifCacheContains " + method + " is called in Cache");
        return map.containsKey(method);
    }

    public void add(String method, List<Trainer> list) {
        log.info("Method add " + method + " is called in Cache");
        if (map.size() == MAX_SIZE) {
            map.remove(queue.getFirst());
            queue.removeFirst();
            actualSize--;
        }
        map.put(method, list);
        queue.add(method);
        actualSize++;
    }

    public List<Trainer> returnFromCache(String method) {
        log.info("Method returnFromCache " + method + " is called in Cache");
        List<Trainer> list = map.get(method);
        if (list.size() > 0) {
            log.info("List of Trainers: " + list.toString());
        } else {
            log.info("List of Trainers is empty");
        }
        return list;
    }

    public void clear() {
        map.clear();
        queue.clear();
        actualSize = 0;
        log.info("Cache is empty");
    }

    public void viewInfo() {
        log.info("Cache: " + map.keySet().toString());
    }
}
