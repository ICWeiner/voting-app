package com.joker.apostas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joker.apostas.model.Contest;
import com.joker.apostas.repository.ContestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ContestScraperService {

    @Autowired
    private ContestRepository contestRepository;
    
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public ContestScraperService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    //TEST 10 seconds -> @Scheduled(cron = "*/10 * * * * ?") 
    @Scheduled(cron = "0 0 6 * * ?")
    public void fetchContestDate() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String url = "https://www.rtp.pt/EPG/json/rtp-channels-page/list-grid/tv/1/" + dateStr;

            System.out.println("Running scheduler at " + now + ", fetching URL: " + url);

            String json = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            JsonNode root = objectMapper.readTree(json);
            JsonNode scheduleRoot = root.path("result"); // <-- was "schedule"

            String[] dayParts = {"afternoon", "evening"};
            boolean found = false;

            for (String part : dayParts) {
                JsonNode programs = scheduleRoot.path(part);
                if (programs.isArray()) {
                    for (JsonNode program : programs) {
                        String name = program.path("name").asText();
                        String episodeTitle = program.path("episode").path("title").asText();

                        if (name.toLowerCase().contains("joker") || episodeTitle.toLowerCase().contains("joker")) {

                            String startDateTimeStr = program.path("date").asText();
                            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                            Contest contest = new Contest();
                            contest.setTitle(!episodeTitle.isBlank() ? episodeTitle : name); //TODO add episode number
                            contest.setStartDateTime(startDateTime);
                            contestRepository.save(contest);
                            System.out.println("New contest saved: " + contest.getTitle() + " at " + startDateTime);
                            found = true;
                        }
                    }
                }
            }

            if (!found) {
                System.out.println("No relevant contest found in afternoon or evening.");
            }


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to fetch contest data from RTP.");
        }
    }
}
