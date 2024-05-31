import java.util.*;

public class TSP_BFS {

    static class Node {
        String city;
        Set<String> visited;
        int cost;
        List<String> path;

        public Node(String city, Set<String> visited, int cost, List<String> path) {
            this.city = city;
            this.visited = new HashSet<>(visited);
            this.cost = cost;
            this.path = new ArrayList<>(path);
        }
    }

    public static void main(String[] args) {
        Map<String, Map<String, Integer>> distances = new HashMap<>();


        distances.put("A", Map.of("B", 7, "E", 1, "D",1));
        distances.put("B", Map.of("A", 7, "C", 3, "E", 8));
        distances.put("C", Map.of("B", 3, "D", 6, "E", 2));
        distances.put("D", Map.of("C", 6, "E", 7,"A",1));
        distances.put("E", Map.of("A", 1, "B", 8, "C", 2, "D", 7));

        String startCity = "A";
        Result result = findShortestPath(startCity, distances);
        System.out.println("Shortest path  cost: " + result.cost);
        System.out.println("Path: " + String.join(" -> ", result.path));
    }

    static class Result {
        int cost;
        List<String> path;

        public Result(int cost, List<String> path) {
            this.cost = cost;
            this.path = path;
        }
    }

    public static Result findShortestPath(String startCity, Map<String, Map<String, Integer>> distances) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node(startCity, Set.of(startCity), 0, List.of(startCity)));

        int minCost = Integer.MAX_VALUE;
        List<String> minPath = new ArrayList<>();
        int numCities = distances.size();

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.visited.size() == numCities) {
                Integer returnCost = distances.get(currentNode.city).get(startCity);
                if (returnCost != null) {
                    int totalCost = currentNode.cost + returnCost;
                    if (totalCost < minCost) {
                        minCost = totalCost;
                        minPath = new ArrayList<>(currentNode.path);
                        minPath.add(startCity);
                    }
                }
                continue;
            }

            for (Map.Entry<String, Integer> entry : distances.get(currentNode.city).entrySet()) {
                String nextCity = entry.getKey();
                int nextCost = entry.getValue();

                if (!currentNode.visited.contains(nextCity)) {
                    Set<String> newVisited = new HashSet<>(currentNode.visited);
                    newVisited.add(nextCity);
                    List<String> newPath = new ArrayList<>(currentNode.path);
                    newPath.add(nextCity);
                    queue.add(new Node(nextCity, newVisited, currentNode.cost + nextCost, newPath));
                }
            }
        }

        return new Result(minCost, minPath);
    }
}
