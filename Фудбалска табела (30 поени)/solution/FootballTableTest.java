import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

class FootballTable {

    Map<String, Team> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = teams.computeIfAbsent(homeTeam, key -> new Team(homeTeam));
        Team away = teams.computeIfAbsent(awayTeam, key -> new Team(awayTeam));
        home.goalsScored += homeGoals;
        home.goalsConceded += awayGoals;
        away.goalsScored += awayGoals;
        away.goalsConceded += homeGoals;
        home.played++;
        away.played++;
        if (homeGoals > awayGoals) {
            home.wins++;
            away.loses++;
        } else if (homeGoals < awayGoals) {
            away.wins++;
            home.loses++;
        } else {
            home.draws++;
            away.draws++;
        }
    }

    public void printTable() {
        List<Team> result = teams.values().stream()
                .sorted(Comparator.comparing(Team::getPoints)
                        .thenComparing(Team::goalDifference).reversed()
                        .thenComparing(Team::getName))
                .collect(Collectors.toList());
        IntStream.range(0, result.size())
                .forEach(i -> System.out.printf("%2d. %s\n", i + 1, result.get(i)));
    }


}

class Team {
    String name;
    int wins;
    int loses;
    int draws;
    int played;
    int goalsScored;
    int goalsConceded;

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getDraws() {
        return draws;
    }

    public int getPlayed() {
        return played;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public int goalDifference() {
        return goalsScored - goalsConceded;
    }

    public int getPoints() {
        return wins * 3 + draws;
    }


    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, played, wins, draws, loses, getPoints());
    }
}

