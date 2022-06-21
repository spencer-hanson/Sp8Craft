package net.sp8craft.math.expressions;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class FunctionEvaluator implements IFileUpdater {
    private List<String> initialLines;
    private List<String> lines;
    private TimerTask task;
    private Timer timer;
    private boolean timerStarted;
    private IRunOnUpdate runOnUpdate;

    private JShell js;

    private class FileWatcher extends TimerTask {
        private long timestamp;
        private File fileObj;
        private IFileUpdater fileUpdater;

        public FileWatcher(File fileObj, IFileUpdater fileUpdater) {
            this.fileObj = fileObj;
            this.timestamp = fileObj.lastModified();
            this.fileUpdater = fileUpdater;
        }

        public final void run() {
            long currentTimestamp = fileObj.lastModified();
            if (this.timestamp != currentTimestamp) {
                this.timestamp = currentTimestamp;
                fileUpdater.updateFile(fileObj);
            }
        }
    }

    public interface IRunOnUpdate {
        public void run();
    }

    public FunctionEvaluator(List<String> initialLines, File file, IRunOnUpdate runOnUpdate) {
        this.runOnUpdate = runOnUpdate;
        this.initialLines = initialLines;
        this.lines = new ArrayList<>();
        this.task = new FunctionEvaluator.FileWatcher(file, this);
        this.timer = new Timer();
        this.timerStarted = false;
        this.js = JShell.create();
//            this.startFileWatch();
    }

    public void updateFile(File fileObj) {
        System.out.println("Detected change, updating file");
        Optional<List<String>> lines = readFile(fileObj);
        lines.ifPresent(strings -> this.lines = strings);
        this.runOnUpdate.run(); // TODO might need to pass info here?
    }

    public void startFileWatch() {
        if (!this.timerStarted) {
            timer.schedule(this.task, new Date(), 1000); // Check every second
            this.timerStarted = true;
        }
    }

    public static Optional<List<String>> readFile(File fileObj) {
        ArrayList<String> newLines = new ArrayList<>();

        List<String> lines;
        try {
            lines = Files.readAllLines(fileObj.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        for (String line : lines) {
            if (line.strip().equals("") || line.startsWith("//")) {
                continue;
            }
            newLines.add(line);
        }
        return Optional.of(newLines);
    }

    public static Optional<String> getValueFromEvents(List<SnippetEvent> events) {
        for (SnippetEvent e : events) {
            StringBuilder sb = new StringBuilder();
            switch (e.status()) {
                case RECOVERABLE_DEFINED -> sb.append("With unresolved references ");
                case RECOVERABLE_NOT_DEFINED -> sb.append("Possibly reparable, failed  ");
                case REJECTED -> sb.append("Failed ");
            }
            if (e.previousStatus() == Snippet.Status.NONEXISTENT) {
                sb.append("addition");
            } else {
                sb.append("modification");
            }

            sb.append(" of ");
            String val = e.value();
            if (val != null) {
                return Optional.of(val);
            } else {
                System.out.println(sb);
            }
        }
        return Optional.empty();
    }

    public Optional<String> runFile() {
        for (String s : initialLines) {
            js.eval(s);
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Optional<String> val = getValueFromEvents(js.eval(line));
            String doneVal = js.eval("done").get(0).value();
            if (doneVal.equals("true")) {
                return Optional.of(js.eval("result").get(0).value());
            }

            if (val.isEmpty()) {
                System.out.println("Error line #" + i + " src: '" + line + "'");
                return Optional.empty();
            }
        }
        System.out.println("Didn't set done = true!");
        return Optional.empty();
    }
}