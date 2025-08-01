🔐 Remote Linux Management using Java (SSH)
This Java project allows you to remotely log in to a Linux machine, check for security patches, apply those patches, and display the system uptime using the JSCH library.

📌 Project Tasks
Login to a Linux machine using SSH

List available security patches

Apply the security patches

Display the system uptime

🛠️ Requirements
Java Development Kit (JDK)

JSCH Library
👉 Download jsch-0.1.55.jar

Make sure to add the downloaded JAR file to your project’s build path.

✅ How It Works
1️⃣ Login to the Linux Machine
We use JSCH to open an SSH connection (port 22) to the remote Linux machine.

java
Copy
Edit
JSch jsch = new JSch();
Session session = jsch.getSession(user, host, 22);
session.setPassword(password);
session.setConfig("StrictHostKeyChecking", "no");
session.connect();
Here:

user is your Linux username.

host is the IP address of the remote Linux machine.

We disable strict host checking for easier connection (for testing purposes).

2️⃣ List Security Patches
We check for available security updates using this Linux command:

bash
Copy
Edit
apt update && apt-get -s dist-upgrade | grep "^Inst" | grep -i security
In Java, we use:

java
Copy
Edit
runCommand(session, "echo '" + password + "' | sudo -S -p '' bash -c 'apt update && apt-get -s dist-upgrade | grep \"^Inst\" | grep -i securi'");
3️⃣ Apply Security Patches
To apply the updates, we use the unattended-upgrades command:

bash
Copy
Edit
sudo apt unattended-upgrades -v
In Java, we execute:

java
Copy
Edit
runCommand(session, "echo '" + password + "' | sudo -S -p '' apt unattended-upgrades -v");
This runs the upgrade process non-interactively.

4️⃣ Display System Uptime
To check how long the system has been running, we use:

bash
Copy
Edit
uptime
In Java:

java
Copy
Edit
runCommand(session, "uptime");
📥 Reading and Displaying Output
We capture the command output from the Linux machine using an InputStream, then convert it into readable text using a buffer:

java
Copy
Edit
InputStream input = channel.getInputStream();
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
🧪 Example Output
lua
Copy
Edit
karnan
Exit status: 0
Hit:1 http://ports.ubuntu.com/ubuntu-ports noble InRelease
...
All packages are up to date.
Exit status: 1
Starting unattended upgrades script
...
No packages found that can be upgraded unattended
Exit status: 0
12:15:47 up 7:54, 2 users, load average: 0.08, 0.02, 0.01
Exit status: 0
All commands executed and session closed.
