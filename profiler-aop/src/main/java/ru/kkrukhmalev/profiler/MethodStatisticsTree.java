package ru.kkrukhmalev.profiler;

import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

public class MethodStatisticsTree {
    public static class Node {
        public String nodeName;

        public Map<String, Node> children;

        public MethodStatistics methodStatistics;

        public Node(String nodeName, MethodStatistics methodStatistics) {
            this.nodeName = nodeName;
            this.children = new HashMap<>();
            this.methodStatistics = methodStatistics;
        }

        public Node(String nodeName) {
            this(nodeName, null);
        }

        public void collectToString(StringBuilder sb, int indent) {
            if (methodStatistics != null) {
                sb.append(Colors.ANSI_GREEN).append(" ".repeat(indent)).append("* ")
                        .append(nodeName).append(Colors.ANSI_RESET).append("\n");
                sb.append(Colors.ANSI_GREEN).append(" ".repeat(indent + 2)).append("Invocations: ")
                        .append(methodStatistics.getInvocationCount()).append(Colors.ANSI_RESET).append("\n");
                sb.append(Colors.ANSI_GREEN).append(" ".repeat(indent + 2)).append("Sum time: ")
                        .append(methodStatistics.getSumExecutionTime()).append(Colors.ANSI_RESET).append("\n");
                sb.append(Colors.ANSI_GREEN).append(" ".repeat(indent + 2)).append("Average time: ")
                        .append(methodStatistics.getAverageExecutionTime()).append(Colors.ANSI_RESET).append("\n");
            } else {
                sb.append(Colors.ANSI_YELLOW).append(" ".repeat(indent)).append("* ")
                        .append(nodeName).append(Colors.ANSI_RESET).append("\n");
            }
            for (Node child : children.values()) {
                child.collectToString(sb, indent + 1);
            }
        }
    }

    public Node rootNode;

    public MethodStatisticsTree(String rootName) {
        this.rootNode = new Node(rootName);
    }

    public void updateStatic(MethodSignature signature, long time) {
        String[] path = signature.getDeclaringTypeName()
                .replace(rootNode.nodeName + ".", "").split("\\.");
        Node currentNode = rootNode;
        for (String pathPart : path) {
            if (!currentNode.children.containsKey(pathPart)) {
                currentNode.children.put(pathPart, new Node(pathPart));
            }
            currentNode = currentNode.children.get(pathPart);
        }

        StringBuilder args = new StringBuilder();
        for (int i = 0; i < signature.getParameterNames().length; i++) {
            args.append(signature.getParameterTypes()[i].getCanonicalName())
                    .append(" ")
                    .append(signature.getParameterNames()[i]);
            if (i != signature.getParameterNames().length - 1) {
                args.append(",");
            }
        }
        String methodName = signature.getName() + "(" + args + ")";

        if (!currentNode.children.containsKey(methodName)) {
            currentNode.children.put(methodName, new Node(methodName,
                    new MethodStatistics(signature, 0, 0)));
        }
        currentNode.children.get(methodName).methodStatistics.countExecution(time);
    }

    public void collectToString(StringBuilder sb) {
        rootNode.collectToString(sb, 0);
    }
}
