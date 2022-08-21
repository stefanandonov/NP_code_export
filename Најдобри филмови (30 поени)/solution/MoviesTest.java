import java.util.*;

/**
 * Created by tdelev on 24.12.15.
 */
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

class MoviesList {
  List<Movie> movies;

  public MoviesList() {
    movies = new ArrayList<>();
  }

  public void addMovie(String title, int[] ratings) {
    Movie movie = new Movie(title, ratings);
    movies.add(movie);
  }

  public List<Movie> top10ByAvgRating() {
    Collections.sort(movies, new AvgRatingComparator());
    if (movies.size() >= 10) {
      return movies.subList(0, 10);
    } else return movies;
  }

  public List<Movie> top10ByRatingCoef() {
    int maxRatings = 0;
    for (Movie movie : movies) {
      maxRatings = Math.max(maxRatings, movie.ratings.size());
    }
    Collections.sort(movies, new CoefRatingComparator(maxRatings));
    if (movies.size() >= 10) {
      return movies.subList(0, 10);
    } else return movies;
  }
}

class AvgRatingComparator implements Comparator<Movie> {
  @Override
  public int compare(Movie o1, Movie o2) {
    int ar = Float.compare(o2.avgRating, o1.avgRating);
    if (ar == 0) {
      return o1.title.compareTo(o2.title);
    }
    return ar;
  }
}

class CoefRatingComparator implements Comparator<Movie> {
  int maxRatings;

  public CoefRatingComparator(int maxRatings) {
    this.maxRatings = maxRatings;
  }

  @Override
  public int compare(Movie o1, Movie o2) {
    int ar = Float.compare(o1.avgRating * o1.ratings.size() / maxRatings, o2.avgRating * o2.ratings.size() / maxRatings);
    if (ar == 0) {
      return o1.title.compareTo(o2.title);
    }
    return -ar;
  }
}

class Movie {
  String title;
  List<Integer> ratings;
  float avgRating;

  public Movie(String title, int[] ratings) {
    this.title = title;
    this.ratings = new ArrayList<>(ratings.length);
    for (int r : ratings) {
      this.ratings.add(r);
    }
    avgRating = 0;
    for (Integer r : ratings) {
      avgRating += r;
    }
    avgRating /= ratings.length;
  }

  @Override
  public String toString() {
    return String.format("%s (%.2f) of %d ratings", title, avgRating, ratings.size());
  }
}
