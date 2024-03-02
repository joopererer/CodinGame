package TheLostFiles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * https://www.codingame.com/ide/puzzle/the-lost-files
 */
public class Solution {

    static class Continent {
        Set<Integer> nodes = new HashSet<>();
        public boolean isContain(int[] edge) {
            if (nodes.isEmpty())
                return false;
            return nodes.contains(edge[0]) || nodes.contains(edge[1]);
        }
        public void add(int[] edge) {
            nodes.add(edge[0]);
            nodes.add(edge[1]);
        }
    }

    public static int countContinents(List<int[]> edges){
        ArrayList<Continent> continents = new ArrayList<>();
        while(edges.size()!=0){
            int size = edges.size();
            Iterator<int[]> iter = edges.iterator();
            while(iter.hasNext()){
                int[] edge = iter.next();
                for(Continent continent : continents){
                    if (continent.isContain(edge)) {
                        continent.add(edge);
                        iter.remove();
                        break;
                    }
                }
            }
            if (size == edges.size()){
                Continent continent = new Continent();
                continent.add(edges.get(0));
                edges.remove(0);
                continents.add(continent);
            }
        }
        return continents.size();
    }

    public static int countCycles(List<int[]> edges, int n) {
        int count = 0;

        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        }

        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count += countCyclesUtil(i, -1, adjList, visited, new HashSet<>());
            }
        }

        return count;
    }

    private static int countCyclesUtil(int current, int parent, List<List<Integer>> adjList, boolean[] visited, Set<Integer> currentPath) {
        visited[current] = true;
        currentPath.add(current);

        int count = 0;

        for (int neighbor : adjList.get(current)) {
            if (!visited[neighbor]) {
                count += countCyclesUtil(neighbor, current, adjList, visited, currentPath);
            } else if (neighbor != parent && currentPath.contains(neighbor)) {
                count++;
            }
        }

        currentPath.remove(current);

        return count;
    }

    public static void main(String args[]) {
        final String file = "D:\\workspace\\IdeaProjects\\CodinGame\\out\\production\\CodinGame\\TheLostFiles\\edge.txt";
        try {
            FileInputStream fis = new FileInputStream(file);
            Scanner in = new Scanner(fis);
            int E = in.nextInt();
            System.err.println("lignes:"+E);

            ArrayList<int[]> edges = new ArrayList<>();
            int n = 0;

            for (int i = 0; i < E; i++) {
                int n1 = in.nextInt();
                int n2 = in.nextInt();
                System.err.println(n1+"-"+n2);
                edges.add(new int[]{n1, n2});
                n = Math.max(n1, Math.max(n2, n));
            }

            int T = countCycles(edges, n+1);
            int C = countContinents(edges);

            System.out.println("C:"+C+", T:"+T);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");
    }

}
