package scoreboard.service;


import scoreboard.exception.MatchAlreadyFinishedException;
import scoreboard.exception.NoMatchFoundException;
import scoreboard.exception.SameTeamException;
import scoreboard.exception.TeamAlreadyInMatchException;
import scoreboard.exception.TeamNameNullOrEmptyException;
import scoreboard.model.Match;

import java.util.ArrayList;
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
        if (!ongoingMatches.containsKey(matchId)) {
            throw new NoMatchFoundException();
        }

        Match match = ongoingMatches.get(matchId);
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(int matchId) {
        if (!ongoingMatches.containsKey(matchId) && !finishedMatches.containsKey(matchId)) {
            throw new NoMatchFoundException();
        }

        if (finishedMatches.containsKey(matchId)) {
            throw new MatchAlreadyFinishedException();
        }

        Match match = ongoingMatches.remove(matchId);
        finishedMatches.put(matchId, match);
    }


    public List<Match> getSummary() {
        if (ongoingMatches.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map.Entry<Integer, Match>> sortedMatches = new ArrayList<>(ongoingMatches.entrySet());

        sortedMatches.sort((entry1, entry2) -> {
            int score1 = entry1.getValue().getHomeScore() + entry1.getValue().getAwayScore();
            int score2 = entry2.getValue().getHomeScore() + entry2.getValue().getAwayScore();

            // If total score is equal, compare by matchId (most recent first)
            if (score1 == score2) {
                return entry2.getKey() - entry1.getKey();
            }

            //Sort by total score (descending)
            return score2 - score1;
        });

        // Return only the match objects in sorted order
        List<Match> result = new ArrayList<>();
        for (Map.Entry<Integer, Match> entry : sortedMatches) {
            result.add(entry.getValue());
        }

        return result;
    }
}
