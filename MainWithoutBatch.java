// without batch
package dayy30;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MainWithoutBatch {
    public static final String URL = "jdbc:mysql://localhost:3306/demo_batch?useSSL=false&serverTimezone=UTC";
    public static final String DBUSER = "root";
    public static final String DBPASSWORD = "12345";

    public static void main(String[] args) {
        final int TOTAL = 100; // total number of rows to insert

        String query = "INSERT INTO bulk_users (username, email) VALUES (?, ?)";
        long start = System.nanoTime();

        try (Connection conn = DriverManager.getConnection(URL, DBUSER, DBPASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Auto-commit is true by default (every insert commits immediately)
            for (int i = 100; i < TOTAL; i++) {
                ps.setString(1, "user_" + i); // set username
                ps.setString(2, "user_" + i + "@google.com"); // set email
                ps.executeUpdate(); // insert one record at a time
            }

            long elapsed_time = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Inserted " + TOTAL + " rows in " + elapsed_time +
                    " ms (without using batch processing).");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}