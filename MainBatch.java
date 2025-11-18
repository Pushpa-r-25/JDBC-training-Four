// intersting the data in bulk using batch statement
package dayy30;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MainBatch {
    public static final String URL="jdbc:mysql://localhost:3306/demo_batch?useSSL=false&serverTimezone=UTC";
    public static final String DBUSER="root";
    public static final String DBPASSWORD="12345";
    public static void main(String[] args) {
final int TOTAL =100;
final int BATCH_SIZE=25;
String query="INSERT INTO bulk_users (username, email) VALUES (?, ?)";
long start = System.nanoTime();
try(Connection conn = DriverManager.getConnection(URL, DBUSER, DBPASSWORD);
PreparedStatement ps = conn.prepareStatement(query);)
{
conn.setAutoCommit(false);
int count=0;
for(int i=0;i<TOTAL;i++){
    ps.setString(1,"user_"+i); //set username
    ps.setString(2,"user_"+i+"@google.com"); //  set email
    ps.addBatch();
    count++; // to maintain the count of records inserted keep on incrementing until we reach 25
    if(count%BATCH_SIZE==0){
        ps.executeBatch(); // send the cunk we have created tp db
        ps.clearBatch();// clear the client side buffer to prepare for next batch
        conn.commit();
    }
}
if(count%BATCH_SIZE!=0){
    ps.executeBatch();

}
conn.commit();
long elapsed_time = (System.nanoTime() - start) / 1_000_000;
System.out.println(" insert "+TOTAL+" rows in "+elapsed_time + "ms using jdbc batch processing of batch size" + BATCH_SIZE+").");
}
catch (Exception e) {
   System.out.println(e.getMessage());
}
    }
}
