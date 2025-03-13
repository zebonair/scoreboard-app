package scoreboardapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scoreboardapp.model.Match;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardServiceTest {
    private ScoreboardService scoreboardService;

    private static final String MEXICO = "Mexico";
    private static final String CANADA = "Canada";
    private static final String SPAIN = "Spain";
    private static final String BRAZIL = "Brazil";
    private static final String GERMANY = "Germany";
    private static final String FRANCE = "France";
    private static final String URUGUAY = "Uruguay";
    private static final String ITALY = "Italy";

    @BeforeEach
    void setUp() {
        scoreboardService = new ScoreboardService();
    }

    @Test
    void testStartNewMatch() {
        scoreboardService.startNewMatch(MEXICO, CANADA);
        scoreboardService.startNewMatch(SPAIN, BRAZIL);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(2, summary.size());
        assertEquals(MEXICO, summary.get(0).getHomeTeam());
        assertEquals(CANADA, summary.get(0).getAwayTeam());
        assertEquals(SPAIN, summary.get(1).getHomeTeam());
        assertEquals(BRAZIL, summary.get(1).getAwayTeam());
    }

    @Test
    void testUpdateScore() {
        scoreboardService.startNewMatch(GERMANY, FRANCE);
        scoreboardService.updateScore(1, 2, 3);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(2, summary.get(0).getHomeScore());
        assertEquals(3, summary.get(0).getAwayScore());
    }

    @Test
    void testFinishMatch() {
        scoreboardService.startNewMatch(URUGUAY, ITALY);
        scoreboardService.finishMatch(1);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(1, summary.size());
        assertEquals(URUGUAY, summary.get(0).getHomeTeam());
        assertEquals(ITALY, summary.get(0).getAwayTeam());
    }

    @Test
    void testSummaryOrdering() {
        scoreboardService.startNewMatch(MEXICO, CANADA);
        scoreboardService.startNewMatch(SPAIN, BRAZIL);
        scoreboardService.startNewMatch(GERMANY, FRANCE);

        scoreboardService.updateScore(1, 0, 5);
        scoreboardService.updateScore(2, 10, 2);
        scoreboardService.updateScore(3, 2, 2);

        List<Match> summary = scoreboardService.getSummary();

        assertEquals(MEXICO, summary.get(0).getHomeTeam());
        assertEquals(SPAIN, summary.get(1).getHomeTeam());
        assertEquals(GERMANY, summary.get(2).getHomeTeam());
    }
}
