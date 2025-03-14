package scoreboardapp.service;


import scoreboardapp.exception.MatchAlreadyFinishedException;
import scoreboardapp.exception.NoMatchFoundException;
import scoreboardapp.exception.SameTeamException;
import scoreboardapp.exception.TeamAlreadyInMatchException;
import scoreboardapp.exception.TeamNameNullOrEmptyException;
import scoreboardapp.model.Match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardService {
    private final Map<Integer, Match> ongoingMatches;
    private final Map<Integer, Match> finishedMatches;
    private int matchIdCounter;

    public ScoreboardService() {
        this.ongoingMatches = new HashMap<>();
        this.finishedMatches = new HashMap<>();
        this.matchIdCounter = 1;
    }

    public void startNewMatch(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isEmpty() || awayTeam == null || awayTeam.isEmpty()) {
            throw new TeamNameNullOrEmptyException();
        }
        if (homeTeam.equals(awayTeam)) {
            throw new SameTeamException();
        }

        // Prevent the same team from playing in more than one ongoing match
        for (Match match : ongoingMatches.values()) {
            if (match.getHomeTeam().equals(homeTeam) || match.getAwayTeam().equals(homeTeam)) {
                throw new TeamAlreadyInMatchException(homeTeam);
            }

            if (match.getHomeTeam().equals(awayTeam) || match.getAwayTeam().equals(awayTeam)) {
                throw new TeamAlreadyInMatchException(awayTeam);
            }
        }

        Match match = new Match(homeTeam, awayTeam);
        ongoingMatches.put(matchIdCounter++, match);
    }


    public void updateScore(int matchId, int homeScore, int awayScore) {
        Match match = ongoingMatches.get(matchId);
        if (match == null) {
            throw new NoMatchFoundException();
        }

        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(int matchId) {
        Match match = ongoingMatches.remove(matchId);
        if (match == null) {
            match = finishedMatches.get(matchId);
            if (match != null) {
                throw new MatchAlreadyFinishedException();
            }
            throw new NoMatchFoundException();
        }

        finishedMatches.put(matchId, match);
    }

    public List<Match> getSummary() {
        if (ongoingMatches.isEmpty() && finishedMatches.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map.Entry<Integer, Match>> sortedMatches = new ArrayList<>();
        sortedMatches.addAll(ongoingMatches.entrySet());
        sortedMatches.addAll(finishedMatches.entrySet());

        // Sort by matchId (oldest first)
        sortedMatches.sort(Comparator.comparingInt(Map.Entry::getKey));

        // Return only the match objects in sorted order
        List<Match> result = new ArrayList<>();
        for (Map.Entry<Integer, Match> entry : sortedMatches) {
            result.add(entry.getValue());
        }
        return result;
    }
}
