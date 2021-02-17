import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class App {
/////////////////////	
	static int min(int x, int y, int z) // this function
	{
		if (x <= y && x <= z)
			return x;
		if (y <= x && y <= z)
			return y;
		else
			return z;
	}

	static int editDistDP(String str1, String str2, int m, int n) {
		// Create a table to store results of subproblems
		int dp[][] = new int[m + 1][n + 1];

		// Fill d[][] in bottom up manner
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				// If first string is empty, only option is
				// to insert all characters of second string
				if (i == 0)
					dp[i][j] = j; // Min. operations = j

				// If second string is empty, only option is
				// to remove all characters of second string
				else if (j == 0)
					dp[i][j] = i; // Min. operations = i

				// If last characters are same, ignore last
				// char and recur for remaining string
				else if (str1.charAt(i - 1) == str2.charAt(j - 1))
					dp[i][j] = dp[i - 1][j - 1];

				// If the last character is different,
				// consider all possibilities and find the
				// minimum
				else
					dp[i][j] = 1 + min(dp[i][j - 1], // Insert
							dp[i - 1][j], // Remove
							dp[i - 1][j - 1]); // Replace
			}
		}

		return dp[m][n];
	}

	static void editDistDPTwoWords(String str1, String str2, int m, int n) {// For Part2

		String[][] backtrace = new String[m + 1][n + 1];

// Create a table to store results of subproblems
		int dp[][] = new int[m + 1][n + 1];

// Fill d[][] in bottom up manner
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				// If first string is empty, only option is
				// to insert all characters of second string
				if (i == 0)
					dp[i][j] = j; // Min. operations = j

				// If second string is empty, only option is
				// to remove all characters of second string
				else if (j == 0)
					dp[i][j] = i; // Min. operations = i

				// If last characters are same, ignore last
				// char and recur for remaining string
				else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];

				}
				// If the last character is different,
				// consider all possibilities and find the
				// minimum
				else
					dp[i][j] = 1 + min(dp[i][j - 1], // Insert
							dp[i - 1][j], // Remove
							dp[i - 1][j - 1]); // Replace
			}
		}

		int len1 = 0;
		if (m > n) {
			len1 = dp[0].length + (m - n);
		} else if (m < n) {
			len1 = dp[0].length - (n - m);
		} else {
			len1 = dp[0].length;
		}

		for (int i = 0; i < len1; i++) {
			for (int j = 0; j < dp[1].length; j++) {
				backtrace[i][j] = String.valueOf(dp[i][j]);

			}
		}
//indexes
		int indexm = m;
		int indexn = n;

//operations
		int insertion = 0;
		int deletion = 0;
		int replacement = 0;
//med value
		int medValue = dp[m][n];
//operaitions
		String[] operations = new String[medValue];
		int operationNumber = medValue - 1;
		
		if(dp[1][1]==0) {
			backtrace[1][1] = "_\\|".concat(backtrace[1][1]);
		}

		while (dp[indexm][indexn] != 0) {
			if (indexm == 0) {
				backtrace[indexm][indexn] = "->".concat(backtrace[indexm][indexn]);
				insertion++;
				String operation = "";
				operation = operation.concat("\nInsert ");
				operation = operation.concat(String.valueOf(str2.charAt(indexn - 1)));
				operations[operationNumber] = operation;
				operationNumber--;
				indexn--;
			} else if (indexn == 0) {
				backtrace[indexm][indexn] = "|".concat(backtrace[indexm][indexn]);
				deletion++;
				String operation = "";
				operation = operation.concat("\nDelete ");
				operation = operation.concat(String.valueOf(str1.charAt(indexm - 1)));
				operations[operationNumber] = operation;
				operationNumber--;
				indexm--;
			} else {
				if (str1.charAt(indexm - 1) == str2.charAt(indexn - 1)) {
					backtrace[indexm][indexn] = "_\\|".concat(backtrace[indexm][indexn]);
					indexm--;
					indexn--;
				} else if (min(dp[indexm - 1][indexn], dp[indexm][indexn - 1],
						dp[indexm - 1][indexn - 1]) == dp[indexm - 1][indexn]) {
					backtrace[indexm][indexn] = "|".concat(backtrace[indexm][indexn]);
					deletion++;
					String operation = "";
					operation = operation.concat("\nDelete ");
					operation = operation.concat(String.valueOf(str1.charAt(indexm - 1)));
					operations[operationNumber] = operation;
					operationNumber--;
					indexm--;
				} else if (min(dp[indexm - 1][indexn], dp[indexm][indexn - 1],
						dp[indexm - 1][indexn - 1]) == dp[indexm][indexn - 1]) {
					backtrace[indexm][indexn] = "->".concat(backtrace[indexm][indexn]);
					insertion++;
					String operation = "";
					operation = operation.concat("\nInsert ");
					operation = operation.concat(String.valueOf(str2.charAt(indexn - 1)));
					operations[operationNumber] = operation;
					operationNumber--;
					indexn--;
				} else if (min(dp[indexm - 1][indexn], dp[indexm][indexn - 1],
						dp[indexm - 1][indexn - 1]) == dp[indexm - 1][indexn - 1]) {
					backtrace[indexm][indexn] = "_\\|".concat(backtrace[indexm][indexn]);
					replacement++;
					String operation = "";
					operation = operation.concat("Replace ");
					operation = operation.concat(String.valueOf(str1.charAt(indexm - 1)));
					operation = operation.concat(" with ");
					operation = operation.concat(String.valueOf(str2.charAt(indexn - 1)));
					operations[operationNumber] = operation;
					operationNumber--;
					indexm--;
					indexn--;
				}
			}

		}

//printings
//printing the backtrace
		System.out.println("\n*****************");
		System.out.println("\nThe Backtrace:\n");
		for (int i = 0; i < backtrace.length; i++) {
			System.out.println(Arrays.toString(backtrace[i]));
		}
		System.out.println("\n*****************");
		System.out.println("\nMed value: " + medValue);
		System.out.println("\n*****************");
		System.out.println("\nOperation Numbers:");
		System.out.println("Insertion:" + insertion);
		System.out.println("Deletion:" + deletion);
		System.out.println("Replacement:" + replacement);

		System.out.println("\n*****************");
		System.out.println("Operations:");
		for (int i = 0; i < operations.length; i++) {
			System.out.println(operations[i]);
		}
	}

	public static void main(String args[]) throws IOException {
		System.out.println("Please enter 1 to choose the Part1 or enter 2 to choose the Part2:");

		boolean isValid = false;
		while (isValid == false) {
			Scanner userChoice = new Scanner(System.in);
			String choice = userChoice.nextLine();
			if (choice.equals("1")) {//For Part1
				isValid = true;
				System.out.println("Please enter a word:");
				Scanner inputFromUser = new Scanner(System.in);
				
				String str1 = inputFromUser.nextLine();
				long startTime = System.currentTimeMillis();////Start Time
				String[] wordArray = ReadFile("sozluk.txt");
				calculateMed(str1, wordArray);
				System.out.println("time passed: " + (System.currentTimeMillis() - startTime) + " milli seconds\n");///End of the Part1 
			}

			else if (choice.equals("2")) {//For Part2
				isValid = true;
				System.out.println("Please enter the first word:");
				Scanner inputFromUser1 = new Scanner(System.in);
				String first = inputFromUser1.nextLine();
				System.out.println("Please enter the second word: ");
				Scanner inputFromUser2 = new Scanner(System.in);
				String second = inputFromUser2.nextLine();
				long startTime = System.currentTimeMillis();////Start Time
				editDistDPTwoWords(first, second, first.length(), second.length());
				System.out.println("time passed: " + (System.currentTimeMillis() - startTime) + " milli seconds\n");///End of the Part2
			} else {
				isValid = false;
				System.out.println("Please enter 1 or 2");
			}
		}

	}

	public static void calculateMed(String str1, String[] wordArray) {

		int medIndex = 0;
		int med;
		ArrayList<Med> list = new ArrayList<>();
		// Med[] medValues = new Med[wordArray.length];
		for (int k = 0; k < wordArray.length - 1; k++) {
			if ((!str1.equalsIgnoreCase(wordArray[k])) && (wordArray[k] != null)) {
				med = editDistDP(str1, wordArray[k], str1.length(), wordArray[k].length());
				Med m = new Med(wordArray[k], med);
				list.add(m);

				medIndex++;

			}

		}
		print(list);
	}

	static void print(ArrayList<Med> list) {
		Collections.sort(list);
		for (int i = 0; i < 5; i++) {
			System.out.println(list.get(i));
		}

	}

	static String[] ReadFile(String file) throws IOException {

		BufferedReader reader;
		String array[] = new String[32633];
		int i = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			array[i] = line;

			while (line != null) {
				// read next line
				i++;
				line = reader.readLine();
				array[i] = line;

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}

}

class Med implements Comparable<Med> {
	String word;

	int distance;

	public Med(String word, int distance) {
		super();
		this.word = word;
		this.distance = distance;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return (this.word + " " + this.distance);
	}

	@Override
	public int compareTo(Med m) {
		if (this.distance != m.distance) {
			return this.distance - m.distance;
		}
		return 0;
	}

}
