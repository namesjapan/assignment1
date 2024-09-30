import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class LogProcessor {
    private Queue<String> logQueue = new LinkedList<>();
    private Stack<String> errorStack = new Stack<>();
    private int infoCount = 0;
    private int warnCount = 0;
    private int errorCount = 0;
    private int memoryWarningCount = 0;
    private LinkedList<String> recentErrors = new LinkedList<>();
    public void readLogFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                logQueue.offer(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public void processLogEntries() {
        while (!logQueue.isEmpty()) {
            String logEntry = logQueue.poll();
            if (logEntry != null) {
                analyzeLog(logEntry);
            }
        }
    }
    private void analyzeLog(String logEntry) {
        if (logEntry.contains("INFO")) {
            infoCount++;
        } else if (logEntry.contains("WARN")) {
            warnCount++;
            if (logEntry.contains("Memory")) {
                memoryWarningCount++;
            }
        } else if (logEntry.contains("ERROR")) {
            errorCount++;
            errorStack.push(logEntry);
            storeRecentError(logEntry);
        }
    }
    private void storeRecentError(String errorLog) {
        if (recentErrors.size() >= 100) {
            recentErrors.removeFirst();
        }
        recentErrors.add(errorLog);
    }
    public void displayAnalysis() {
        System.out.println("Total INFO logs: " + infoCount);
        System.out.println("Total WARN logs: " + warnCount);
        System.out.println("Total ERROR logs: " + errorCount);
        System.out.println("Memory Warnings (WARN): " + memoryWarningCount);
        System.out.println("Last 100 Errors: ");
        for (String error : recentErrors) {
            System.out.println(error);
        }
    }
    public void displayErrorStack() {
        System.out.println("Errors stored in the stack: ");
        while (!errorStack.isEmpty()) {
            System.out.println(errorStack.pop());
        }
    }
    public static void main(String[] args) {
        LogProcessor processor = new LogProcessor();
        processor.readLogFile("log-data.csv");
        processor.processLogEntries();
        processor.displayAnalysis();
        processor.displayErrorStack();
    }
}