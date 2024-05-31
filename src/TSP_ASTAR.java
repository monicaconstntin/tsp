import java.util.*;
import java.util.function.Supplier;

public class TSP_ASTAR {

    static class Node implements Comparable<Node> {
        String city;
        Set<String> visited;
        int cost;
        int estimatedCost;
        List<String> path;

        public Node(String city, Set<String> visited, int cost, int estimatedCost, List<String> path) {
            this.city = city;
            this.visited = new HashSet<>(visited);
            this.cost = cost;
            this.estimatedCost = estimatedCost;
            this.path = new ArrayList<>(path);
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost + this.estimatedCost, other.cost + other.estimatedCost);
        }
    }

   public static void main(String[] args) {
        Map<String, Map<String, Integer>> distances = new HashMap<>();

        distances.put("A", Map.of("B", 7, "E", 1, "D", 1));
        distances.put("B", Map.of("A", 7, "C", 3, "E", 8));
        distances.put("C", Map.of("B", 3, "D", 6, "E", 2));
        distances.put("D", Map.of("C", 6, "E", 7, "A", 1));
        distances.put("E", Map.of("A", 1, "B", 8, "C", 2, "D", 7));

        String startCity = "A";

        long startTime = System.nanoTime();
        Result result = findShortestPath(startCity, distances);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //convert to milliseconds
        double durationInSeconds = duration / 1_000_000_000.0;

        System.out.println("Shortest path cost: " + result.cost);
        System.out.println("Path: " + String.join(" -> ", result.path));
        System.out.printf("Execution time: %.7f seconds%n", durationInSeconds);
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
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(startCity, Set.of(startCity), 0, 0, List.of(startCity)));

        int minCost = Integer.MAX_VALUE;
        List<String> minPath = new ArrayList<>();
        int numberOfCities = distances.size();

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

            if (currentNode.visited.size() == numberOfCities) {
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
                    int estimatedCost = heuristic(nextCity, newVisited, distances);
                    queue.add(new Node(nextCity, newVisited, currentNode.cost + nextCost, estimatedCost, newPath));
                }
            }
        }

        return new Result(minCost, minPath);
    }

    //nearest neighbour heuristic
    public static int heuristic(String currentCity, Set<String> visited, Map<String, Map<String, Integer>> distances) {
        int minDistance = Integer.MAX_VALUE;
        for (String city : distances.keySet()) {
            if (!visited.contains(city)) {
                int distance = distances.get(currentCity).getOrDefault(city, Integer.MAX_VALUE);
                minDistance = Math.min(minDistance, distance);
            }
        }
        return minDistance == Integer.MAX_VALUE ? 0 : minDistance;
    }
}
