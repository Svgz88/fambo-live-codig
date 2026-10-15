package com.example.demo.health;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WayPointController {

    private static final Logger logger = LoggerFactory.getLogger(WayPointController.class);
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    private final LinkStore store;
    private String commitSha = null;

    public WayPointController(LinkStore store){
        this.store = store;
    }

    @PostConstruct
    public void init(){
        try {
            ClassPathResource resource = new ClassPathResource("commit.txt");
            if(resource.exists()) {
                this.commitSha = resource.getContentAsString(StandardCharsets.UTF_8).trim();
            } else {
                logger.warn("commit.txt does not exist i resources");
            }
        } catch (IOException e) {
            logger.warn("Could not read commit.txt", e);
        }

        logger.info("Waypoint is using ConcurrentHashMap (db)");
        logger.info("Waypoint is listening on http://localhost:8080");
    }

    private String generateCode(){
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++){
            code.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    private boolean isAbsoluteUrl(String url){
        if(url == null) return false;
        return url.startsWith("http://") || url.startsWith("https://");
    }

    @ModelAttribute
    public void logRequest(HttpServletRequest request, HttpServletResponse response){
        response.addHeader("Request-Logged", "true");
    }

    @GetMapping("/health")
    public Map<String, Object> health(){
        Map<String, Object> res = new HashMap<>();
        res.put("status","healthy");
        res.put("version", "1.0.0");
        if (commitSha != null){
            res.put("commit", commitSha);
        }
        return ResponseEntity.ok(res).getBody();
    }

    @GetMapping("/")
    public Map<String, String> index(){
        Map<String, String> res = new HashMap<>();
        res.put("app", "Waypoint-java");
        res.put("status", "running");
        return res;
    }

    @GetMapping("links")
    public ResponseEntity<Object> createLink(@RequestBody Map<String, String> body){
        String targetUrl = body != null ? body.get("targetUrl") : null;

        if (!isAbsoluteUrl(targetUrl)){
            Map<String, String> err = new HashMap<>();
            err.put("error", "targetUrl must be an absolute URL");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }

        Link link = new Link(generateCode(), targetUrl);
        store.create(link);

        return ResponseEntity.status(HttpStatus.CREATED).header("Location","/links" + link.getCode())
                .body(link);
    }

    @GetMapping("/link/{code}")
    public ResponseEntity<Object> getLink(@PathVariable String code){
        Link link = store.findByCode(code);
        if (link == null){
            Map<String,String> err = new HashMap<>();
            err.put("error", "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(link);
        }
        return ResponseEntity.ok(link);
    }

    @GetMapping("/{code}")
    public Object redirect(@PathVariable String code){
        Link link = store.findByCode(code);
        if (link == null){
            Map<String, String> err = new HashMap<>();
            err.put("error", "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
        store.addClick(code);
        return new RedirectView(link.getTargetUrl());
    }


































}
