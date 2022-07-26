import java.util.*;

public class Game
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Which Level Would you like to Play?" +
                         "\n1: Level 1" +
                         "\n2: Level 2" +
                         "\n3: Level 3" +
                         "\n4: Level 4" +
                         "\nPlease Enter 1, 2, 3 or 4 as per the Menu: ");
        int level = scan.nextInt();
        switch(level)
        {
            case 1:
                MazeL1.level1();
                break;
            case 2:
                MazeL2.level2();
                break;
            case 3:
                MazeL3.level3();
                break;
            case 4:
                GraphEdge.level4();
                break;
            default:
                break;
        }
    }
}
