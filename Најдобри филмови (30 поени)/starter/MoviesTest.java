import java.util.*;

public class MoviesTest {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    MoviesList moviesList = new MoviesList();
    int n = scanner.nextInt();
    scanner.nextLine();
    for (int i = 0; i < n; ++i) {
      String title = scanner.nextLine();
      int x = scanner.nextInt();
      int[] ratings = new int[x];
      for (int j = 0; j < x; ++j) {
        ratings[j] = scanner.nextInt();
      }
      scanner.nextLine();
      moviesList.addMovie(title, ratings);
    }
    scanner.close();
    List<Movie> movies = moviesList.top10ByAvgRating();
    System.out.println("=== TOP 10 BY AVERAGE RATING ===");
    for (Movie movie : movies) {
      System.out.println(movie);
    }
    movies = moviesList.top10ByRatingCoef();
    System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
    for (Movie movie : movies) {
      System.out.println(movie);
    }
  }
}

// vashiot kod ovde