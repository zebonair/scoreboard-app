package scoreboardapp.service;


import scoreboardapp.model.Match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardService {
    private final Map<Integer, Match> ongoingMatches;
    private final List<Match> finishedMatches;
    private int matchIdCounter;

    public ScoreboardService() {
        this.ongoingMatches = new HashMap<>();
        this.finishedMatches = new ArrayList<>();
        this.matchIdCounter = 1;
    }

    public void startNewMatch(String homeTeam, String awayTeam) {
        Match match = new Match(homeTeam, awayTeam);
        ongoingMatches.put(matchIdCounter++, match);
    }

    public void updateScore(int matchId, int homeScore, int awayScore) {
        Match match = ongoingMatches.get(matchId);
        if (match != null) {
            match.updateScore(homeScore, awayScore);
        }
    }

    public void finishMatch(int matchId) {
        Match match = ongoingMatches.remove(matchId);
        if (match != null) {
            finishedMatches.add(match);
        }
    }

    public List<Match> getSummary() {
        List<Match> allMatches = new ArrayList<>(ongoingMatches.values());
        allMatches.addAll(finishedMatches);

        // Sort by matchId in ascending order (oldest first)
        allMatches.sort(Comparator.comparingInt(match -> ongoingMatches.entrySet().stream()
            .filter(entry -> entry.getValue() == match)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(-1)));
        return allMatches;
    }
}
