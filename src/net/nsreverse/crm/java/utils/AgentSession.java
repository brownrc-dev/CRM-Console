package net.nsreverse.crm.java.utils;

public class AgentSession {
    private static String username;
    private static int role;
    private static String name;

    public static void with(int agentRole, String agentUsername, String agentName) {
        role = agentRole;
        username = agentUsername;
        name = agentName;
    }


    public static String getUsername() {
        return username;
    }

    private static void setUsername(String username) {
        AgentSession.username = username;
    }

    public static int getRole() {
        return role;
    }

    private static void setRole(int role) {
        AgentSession.role = role;
    }

    public static String getName() {
        return name;
    }

    private static void setName(String name) {
        AgentSession.name = name;
    }
}
