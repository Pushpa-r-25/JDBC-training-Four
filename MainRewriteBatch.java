//this method takes less time thn batch
//Batch + rewriteBatchedStatements=true
package dayy30;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MainRewriteBatch {
    public static final String URL = "jdbc:mysql://localhost:3306/demo_batch?useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
    public static final String DBUSER = "root";
    public static final String DBPASSWORD = "12345";

    public static void main(String[] args) {
        final int TOTAL = 100;
        final int BATCH_SIZE = 25;

        String query = "INSERT INTO bulk_users (username, email) VALUES (?, ?)";
        long start = System.nanoTime();

        try (Connection conn = DriverManager.getConnection(URL, DBUSER, DBPASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            conn.setAutoCommit(false);
            int count = 0;

            for (int i = 0; i < TOTAL; i++) {
                ps.setString(1, "user_" + i);
                ps.setString(2, "user_" + i + "@google.com");
                ps.addBatch();
                count++;

                if (count % BATCH_SIZE == 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                    conn.commit();
                }
            }

            if (count % BATCH_SIZE != 0) {
                ps.executeBatch();
            }

            conn.commit();

            long elapsed_time = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Inserted " + TOTAL + " rows in " + elapsed_time +
                    " ms using JDBC batch processing (rewriteBatchedStatements=true).");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
