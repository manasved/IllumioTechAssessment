**Flow Log Parser**

**Overview**
This Java program parses flow log data from a file and maps each row to a tag based on a lookup table provided in a CSV file. The program generates an output file containing the count of matches for each tag and for each port/protocol combination.

**Assumptions Made**
1. Log Format: The program only supports the default AWS VPC Flow Log format (version 2). Any custom formats are not supported.
2. Protocol Handling: The protocol numbers are mapped as follows:
    6 → TCP
    17 → UDP
    1 → ICMP
3. Tagging: If a port/protocol combination is not found in the lookup table, it is marked as "Untagged."
4. Input Constraints: The flow log file size can be up to 10 MB, and the lookup table can contain up to 10,000 mappings.
5. Operating Environment: The program is designed to run on any standard Java environment without requiring additional libraries or        
   dependencies.


**How to Compile and Run the Program**
1. Prerequisites
    Java Development Kit (JDK) version 8 or higher.
    A text editor or an IDE like IntelliJ IDEA.
    Steps to Compile and Run
    Open the Project in IntelliJ IDEA:

2. Open IntelliJ IDEA.
    Click on "File" > "Open" and select the directory containing the project.

3. Compile the Program:
    Ensure that the FlowLogParser.java file is selected in the Project view.
    Click on the Build menu and select Build Project.

4. Run the Program:
    Click on Run > Edit Configurations.
    Set up a new application configuration with the following parameters:
    Main class: FlowLogParser
    Program arguments: src/flow_logs.txt src/lookup_table.csv
    Click Run to execute the program.
   
5. Check the Output:
    The output will be written to src/output.txt.
    Review the output.txt file for the results.
