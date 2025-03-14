package scoreboardapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scoreboardapp.exception.MatchAlreadyFinishedException;
import scoreboardapp.exception.NoMatchFoundException;
import scoreboardapp.exception.SameTeamException;
import scoreboardapp.exception.TeamAlreadyInMatchException;
import scoreboardapp.exception.TeamNameNullOrEmptyException;
import scoreboardapp.model.Match;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private static final String EMPTY_STRING = "";
    private static final String NULL_TEAM = null;

    @BeforeEach
    void setUp() {
        scoreboardService = new ScoreboardService();
    }

    @Test
    void startNewMatch() {
        scoreboardService.startNewMatch(MEXICO, CANADA);
        scoreboardService.startNewMatch(SPAIN, BRAZIL);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(2, summary.size());
        assertEquals(SPAIN, summary.get(0).getHomeTeam());
        assertEquals(BRAZIL, summary.get(0).getAwayTeam());
        assertEquals(MEXICO, summary.get(1).getHomeTeam());
        assertEquals(CANADA, summary.get(1).getAwayTeam());
    }

    @Test
    void updateScore() {
        scoreboardService.startNewMatch(GERMANY, FRANCE);
        scoreboardService.updateScore(1, 2, 3);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(2, summary.get(0).getHomeScore());
        assertEquals(3, summary.get(0).getAwayScore());
    }

    @Test
    void finishMatch() {
        scoreboardService.startNewMatch(URUGUAY, ITALY);
        scoreboardService.finishMatch(1);

        List<Match> summary = scoreboardService.getSummary();
        assertEquals(0, summary.size());
    }

    @Test
    void summaryOrdering() {
        scoreboardService.startNewMatch(MEXICO, CANADA);
        scoreboardService.startNewMatch(SPAIN, BRAZIL);
        scoreboardService.startNewMatch(GERMANY, FRANCE);
        scoreboardService.startNewMatch(URUGUAY, ITALY);

        scoreboardService.updateScore(1, 0, 5);
        scoreboardService.updateScore(2, 10, 2);
        scoreboardService.updateScore(3, 2, 2);
        scoreboardService.updateScore(4, 7, 9);

        List<Match> summary = scoreboardService.getSummary();

        assertEquals(URUGUAY, summary.get(0).getHomeTeam());
        assertEquals(SPAIN, summary.get(1).getHomeTeam());
        assertEquals(MEXICO, summary.get(2).getHomeTeam());
        assertEquals(GERMANY, summary.get(3).getHomeTeam());
    }

    @Test
    void summaryOrderingWithSameResults() {
        scoreboardService.startNewMatch(MEXICO, CANADA);
        scoreboardService.startNewMatch(SPAIN, BRAZIL);

        scoreboardService.updateScore(1, 0, 5);
        scoreboardService.updateScore(2, 2, 3);

        List<Match> summary = scoreboardService.getSummary();

        assertEquals(SPAIN, summary.get(0).getHomeTeam());
        assertEquals(MEXICO, summary.get(1).getHomeTeam());
    }

    @Test
    void startNewMatchWithInvalidTeamNames() {
        assertThrows(TeamNameNullOrEmptyException.class, () -> scoreboardService.startNewMatch(NULL_TEAM, CANADA));
        assertThrows(TeamNameNullOrEmptyException.class, () -> scoreboardService.startNewMatch(MEXICO, NULL_TEAM));

        assertThrows(TeamNameNullOrEmptyException.class, () -> scoreboardService.startNewMatch(EMPTY_STRING, CANADA));
        assertThrows(TeamNameNullOrEmptyException.class, () -> scoreboardService.startNewMatch(MEXICO, EMPTY_STRING));
    }

    @Test
    void startNewMatchWithSameTeams() {
        assertThrows(SameTeamException.class, () ->
            scoreboardService.startNewMatch(MEXICO, MEXICO));
    }

    @Test
    void updateScoreForNonExistingMatch() {
        assertThrows(NoMatchFoundException.class, () ->
            scoreboardService.updateScore(99, 2, 3));
    }

    @Test
    void finishAlreadyFinishedMatch() {
        scoreboardService.startNewMatch(URUGUAY, ITALY);
        scoreboardService.finishMatch(1);

        assertThrows(MatchAlreadyFinishedException.class, () ->
            scoreboardService.finishMatch(1));
    }

    @Test
    void getSummaryWhenNoMatches() {
        List<Match> summary = scoreboardService.getSummary();
        assertEquals(0, summary.size());
    }

    @Test
    void startNewMatchWithSameTeamInOngoingMatch() {
        scoreboardService.startNewMatch(MEXICO, CANADA);

        assertThrows(TeamAlreadyInMatchException.class, () ->
            scoreboardService.startNewMatch(MEXICO, ITALY));

        assertThrows(TeamAlreadyInMatchException.class, () ->
            scoreboardService.startNewMatch(FRANCE, MEXICO));

        assertThrows(TeamAlreadyInMatchException.class, () ->
            scoreboardService.startNewMatch(CANADA, GERMANY));

        assertThrows(TeamAlreadyInMatchException.class, () ->
            scoreboardService.startNewMatch(BRAZIL, CANADA));
    }

    @Test
    void finishMatchWhenMatchNotFound() {
        scoreboardService.startNewMatch(MEXICO, CANADA);

        assertThrows(NoMatchFoundException.class, () ->
            scoreboardService.finishMatch(99));
    }
}
