package com.example.demo.health;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class LinkStore {

    private final ConcurrentHashMap<String, Link> db = new ConcurrentHashMap<>();

    public Link create(Link link){
        db.put(link.getCode(), link);
        return link;
    }

    public Link findByCode(String code){
        return db.get(code);
    }

    public void addClick(String code){
        Link link = db.get(code);
        if (link != null) {
            link.setClicks(link.getClicks()+1);
        }
    }
}
