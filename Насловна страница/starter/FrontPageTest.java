import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;

public class FrontPageTest {
	public static void main(String[] args) {
        // Reading
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		String[] parts = line.split(" ");
		Category[] categories = new Category[parts.length];
		for (int i = 0; i < categories.length; ++i) {
			categories[i] = new Category(parts[i]);
		}
		int n = scanner.nextInt();
		scanner.nextLine();
		FrontPage frontPage = new FrontPage(categories);
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < n; ++i) {
			String title = scanner.nextLine();
			cal = Calendar.getInstance();
            int min = scanner.nextInt();
			cal.add(Calendar.MINUTE, -min);
			Date date = cal.getTime();
			scanner.nextLine();
			String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
			TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
			frontPage.addNewsItem(tni);
		}
        
		n = scanner.nextInt();
		scanner.nextLine();
		for (int i = 0; i < n; ++i) {
			String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -min);
			scanner.nextLine();
			Date date = cal.getTime();
			String url = scanner.nextLine();
			int views = scanner.nextInt();
			scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
			MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
			frontPage.addNewsItem(mni);
		}
        // Execution
        String category = scanner.nextLine();
		System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
        	System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
	}
}


// Vasiot kod ovde