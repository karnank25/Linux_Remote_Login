package Tasks;
import java.io.InputStream;
import com.jcraft.jsch.*;

public class Login {
    public static void main(String[] args) {
        String user = "karnan";
        String host = "192.168.64.3";
        String password = "****";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Execute `df -h`
           //runCommand(session, "df -h");

            // Execute `whoami`
            runCommand(session, "whoami");

            // Check for security updates
            runCommand(session,
                    "echo '" + password + "' | sudo -S -p '' bash -c 'apt update && apt-get -s dist-upgrade | grep \"^Inst\" | grep -i securi'");

       
            runCommand(session, "echo '" + password + "' | sudo -S -p ''apt unattended-upgrades -v ");

            
            // Execute `uptime`
            runCommand(session, "uptime");

            // Disconnect session
            session.disconnect();
            System.out.println("All commands executed and session closed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void runCommand(Session session, String command) throws Exception {
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setInputStream(null);
        channel.setErrStream(System.err);

        InputStream input = channel.getInputStream();
        channel.connect();

        byte[] buffer = new byte[1024];
        while (true) {
            while (input.available() > 0) {
                int i = input.read(buffer, 0, buffer.length);
                if (i < 0) break;
                System.out.print(new String(buffer, 0, i));
            }

            if (channel.isClosed()) {
                System.out.println("Exit status: " + channel.getExitStatus());
                break;
            }
            Thread.sleep(100);
        }
        channel.disconnect();
    }
}