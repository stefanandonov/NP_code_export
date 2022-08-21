import java.util.Scanner;

public class SubtitlesTest {
	public static void main(String[] args) {
		Subtitles subtitles = new Subtitles();
		int n = subtitles.loadSubtitles(System.in);
		System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
		subtitles.print();
		int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
		System.out.println(String.format("SHIFT FOR %d ms", shift));
		subtitles.shift(shift);
		System.out.println("+++++ SHIFTED SUBTITLES +++++");
		subtitles.print();
	}
}

// Вашиот код овде
