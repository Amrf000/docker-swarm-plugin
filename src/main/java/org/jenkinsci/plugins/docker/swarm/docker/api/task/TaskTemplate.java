package org.jenkinsci.plugins.docker.swarm.docker.api.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jenkinsci.plugins.docker.swarm.docker.api.containers.ContainerSpec;

public class TaskTemplate {
    public org.jenkinsci.plugins.docker.swarm.docker.api.containers.ContainerSpec ContainerSpec;
    public RestartPolicy RestartPolicy = new RestartPolicy();
    public Resources Resources = new Resources();
    public Placement Placement;

    public TaskTemplate() {
        // for reading from api
    }

    public TaskTemplate(String image, String[] cmd, String[] args, String[] env, String dir, String user, String[] hosts) {
        this.ContainerSpec = new ContainerSpec(image, cmd, args, env, dir, user, hosts);
    }

    public void setPlacement(String[] placementConstraints, String architecture, String operatingSystem) {
        this.Placement = new Placement(placementConstraints, architecture, operatingSystem);
    }

    public void setRestartAttemptCount(int restartAttemptCount) {
        this.RestartPolicy.MaxAttempts = restartAttemptCount;
    }

    public static class Placement {
        public Placement() {
            // for deseriliztion
        }


        public Placement(String[] constraints, String architecture, String operatingSystem) {
            Constraints = constraints;
            Platforms = new Platform[1]; 
            Platforms[0] = new Platform(architecture, operatingSystem);
        }

        public String[] Constraints;
        public Platform[] Platforms;
    }

    public static class Platform {
        public Platform(){
           //for deseriliztion
        }

        public Platform(String architecture, String operatingSystem) {
            Architecture = architecture;
            OS = operatingSystem;
        }

        public String Architecture;
        public String OS;
    }

    public static class Resources {
        public Resources.Resource Limits = new Resources.Resource();
        public Resources.Resource Reservations = new Resources.Resource();

        public static class Resource {
            public long NanoCPUs;
            public long MemoryBytes;
        }
    }

    public static class RestartPolicy {
        // One-shot container: https://blog.alexellis.io/containers-on-swarm/
        public String Condition = "none";
        // public int Delay = 10;
        public int MaxAttempts = 0;
    }

    private static Pattern computerName = Pattern.compile("(agent-)(\\d+)");

    public String getComputerName() {
        String[] commands = ContainerSpec.Command;
        if (commands != null && commands.length == 3) {
            Matcher m = computerName.matcher(commands[2]);
            if (m.find()) {
                return m.group(2);
            }
        }
        return null;
    }

}
