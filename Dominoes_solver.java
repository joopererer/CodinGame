import java.util.*;
import java.io.*;
import java.math.*;

public class Dominoes_solver {

    static class Position {
        int i;
        int j;
        boolean isHorizontal;
        public Position(int i, int j, boolean isHorizontal){
            this.i = i;
            this.j = j;
            this.isHorizontal = isHorizontal;
        }
    }

    static class Domino {
        char num1;
        char num2;
        int count = 0;
        List<Position> pos_list;
        public Domino(char num1, char num2){
            pos_list = new ArrayList<>();
            this.num1 = num1;
            this.num2 = num2;
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = 3;//in.nextInt();
        int h = 4;//in.nextInt();
        int w = 5;//in.nextInt();
//        if (in.hasNextLine()) {
//            in.nextLine();
//        }

        String[] lines = {"00113","22311","33020","01232"};
        //String[] lines = {"011","100"};

        char[][] grid = new char[h][w];
        for (int i = 0; i < h; i++) {
            String line = lines[i];//in.nextLine();
            grid[i] = line.toCharArray();
        }

        List<Domino> comb = new ArrayList<>();
        for(int i=0; i<=n; i++){
            for(int j=i; j<=n; j++){
                comb.add(new Domino((char)('0'+i), (char)('0'+j)));
            }
        }

        check(grid, w, h, comb, comb.size());
    }

    private static void print_solution(char[][] grid, int w, int h){
        for (int i = 0; i < h; i++) {
            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");
            for (int j = 0; j < w; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    private static boolean check(char[][] grid, int w, int h, List<Domino> comb, int count){
        if(comb.size()==0){
            print_solution(grid, w, h);
            return true;
        }
        if(count<=0){
            return false;
        }

        /**
         * Compter le nombre de des combinaisons possibles
         */
        // parcours par ligne
        for (int i = 0; i < h; i++) {
            int prev = -1;
            for (int j = 0; j < w; j++) {
                if(prev!=-1){
                    for(Domino domino : comb){
                        if((domino.num1 == prev && domino.num2 == grid[i][j])
                                || (domino.num2 == prev && domino.num1 == grid[i][j])){
                            // trouver
                            domino.count += 1;
                            domino.pos_list.add(new Position(i, j, true));
                        }
                    }
                }
                prev = grid[i][j];
            }
        }

        // parcours par colonne
        for (int j = 0; j < w; j++) {
            int prev = -1;
            for (int i = 0; i < h; i++) {
                if(prev!=-1){
                    for(Domino domino : comb){
                        if((domino.num1 == prev && domino.num2 == grid[i][j])
                                || (domino.num2 == prev && domino.num1 == grid[i][j])){
                            // trouver
                            domino.count += 1;
                            domino.pos_list.add(new Position(i, j, false));
                        }
                    }
                }
                prev = grid[i][j];
            }
        }

        comb.sort(new Comparator<Domino>(){

            @Override
            public int compare(Domino o1, Domino o2) {
                return o1.count - o2.count;
            }});

        // obtenir le premier (moins de possibilité)
        Domino domino = comb.remove(0);

        // clean
        Iterator<Domino> iter = comb.iterator();
        while(iter.hasNext()){
            Domino dom = iter.next();
            dom.count = 0;
            dom.pos_list.clear();
        }

        if(domino.count>0){
            if(domino.count==1){
                // seule solution
                Position pos = domino.pos_list.get(0);
                if(pos.isHorizontal){
                    grid[pos.i][pos.j-1] = '=';
                    grid[pos.i][pos.j] = '=';
                }else{
                    grid[pos.i-1][pos.j] = '|';
                    grid[pos.i][pos.j] = '|';
                }
                return check(grid, w, h, comb, count-1);
            }else{
                // plusieurs
                for(Position pos : domino.pos_list){
                    // faire une copie
                    char[][] cl_grid = new char[h][w];
                    for (int i = 0; i < h; i++) {
                        for (int j = 0; j < w; j++) {
                            cl_grid[i][j] = grid[i][j];
                        }
                    }
                    ArrayList<Domino> cl_comb = (ArrayList<Domino>) ((ArrayList<Domino>)comb).clone();
                    if(pos.isHorizontal){
                        cl_grid[pos.i][pos.j-1] = '=';
                        cl_grid[pos.i][pos.j] = '=';
                    }else{
                        cl_grid[pos.i-1][pos.j] = '|';
                        cl_grid[pos.i][pos.j] = '|';
                    }
                    if(check(cl_grid, w, h, cl_comb, count-1)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
