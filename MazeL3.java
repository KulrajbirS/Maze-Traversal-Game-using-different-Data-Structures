import java.util.*;

public class MazeL3
{
    public static boolean[][] maze;
    public static int row;
    public static int col;
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";    
    
    public static final String BG_ANSI_BLACK = "\u001B[40m";
    public static final String BG_ANSI_RED = "\u001B[41m";
    public static final String BG_ANSI_GREEN = "\u001B[42m";
    public static final String BG_ANSI_YELLOW = "\u001B[43m";
    public static final String BG_ANSI_BLUE = "\u001B[44m";
    public static final String BG_ANSI_PURPLE = "\u001B[45m";
    public static final String BG_ANSI_CYAN = "\u001B[46m";
    public static final String BG_ANSI_WHITE = "\u001B[47m"; 
    public static UnionFind<Coordinates> uf = new UnionFind<>();
    public static Coordinates map;
    public static Coordinates me = new Coordinates(0,0);
    public static Coordinates exit;
    public static Coordinates[] zombies;
    public static int reachMap = 0;
    
    public static final String[] colors = new String[] {     
                                                            BG_ANSI_RED,
                                                            BG_ANSI_GREEN,
                                                            BG_ANSI_YELLOW,
                                                            BG_ANSI_BLUE,
                                                            BG_ANSI_PURPLE,
                                                            BG_ANSI_CYAN } ;
    
    
    //Contructor for MazeL3
    public MazeL3(int rows, int cols, double prob)
    {
        this.row = rows;
        this.col = cols;
        maze = new boolean[rows][cols];
        create(prob);
    }
    
    //Creates the grid according to the Probability of walls given by the user
    private void create(double prob)
    {
        for(int i=0; i<maze.length; i++)
            for(int j=0; j<maze[0].length; j++)
            {
                Coordinates test = new Coordinates(i,j);
                if ( Math.random()<prob)
                    maze[i][j] = true;
            }
        maze[0][0] = false;
        maze[maze.length-1][maze[0].length-1] = false;
    }
    
    //Prints the Grid without Path Coloured
    public void drawMaze()
    {
        for(int i=0; i<maze.length; i++)
        {
            for(int j=0; j<maze[0].length; j++)
            {
                Coordinates test = new Coordinates(i,j);
                if(!maze[i][j])
                {
                    if(me.equals(test))
                        System.out.print(BG_ANSI_GREEN + "@  " + ANSI_RESET);
                    else if(exit.equals(test))
                        System.out.print(BG_ANSI_RED + "E  " + ANSI_RESET);
                    else if(map.equals(test) )
                        System.out.print(BG_ANSI_YELLOW + "M  " + ANSI_RESET);
                    else if(isZombie(test))
                        System.out.print(BG_ANSI_PURPLE + "Z  " + ANSI_RESET);
                    else
                        System.out.print(BG_ANSI_WHITE + ".  " + ANSI_RESET);
                }
                else
                    System.out.print(BG_ANSI_WHITE + "#  " + ANSI_RESET);
            }
            System.out.println();
        }
        
    }
    
    //Compares the passed Coordinate with the zombies in the array
    public static boolean isZombie(Coordinates d)
    {
        for(Coordinates x: zombies)
        {
            if(x == null)
                return false;
            if(x.equals(d))
                return true;
        }
        
        return false;
    }
    
    //To move the player according to the user input
    public static void moves(UnionFind uf)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Please select a move: u, d ,r & l: ");
        String move = scan.nextLine();
        if(move.equals("u") && me.row != 0 && !maze[me.row - 1][me.col])
            me.row -= 1;
        else if(move.equals("d") && me.row != maze.length - 1 && !maze[me.row + 1][me.col])
            me.row += 1;
        else if(move.equals("l") && me.col != 0 && !maze[me.row][me.col - 1])
            me.col -= 1;
        else if(move.equals("r") && me.col != maze[0].length -1 && !maze[me.row][me.col + 1])
            me.col += 1;
        
        if(me.row == map.row && me.col == map.col || reachMap > 0)
        {
            drawMazeSetsInColor(uf);
            reachMap++;
        }
        
        moveZombie();
    }
    
    //Makes zombies move randomly in Diagonal Positions
    public static void moveDiag(Coordinates z, int done) 
    {
        Random r = new Random();
        int dpos = r.nextInt(4);
        switch(dpos)
        {
            case 0:
                if(z.row != 0 && z.col != 0 && !maze[z.row - 1][z.col - 1])
                {
                        zombies[done].row -= 1;
                        zombies[done].col -= 1;
                }
                break;
            case 1:
                if(z.row != maze.length - 1 && z.col != maze[0].length - 1 && !maze[z.row + 1][z.col + 1])
                {
                        zombies[done].row += 1;
                        zombies[done].col += 1;
                }
                break;
            case 2:
                if(z.row != maze.length - 1&& z.col != 0 && !maze[z.row + 1][z.col - 1] )
                {
                        zombies[done].row += 1;
                        zombies[done].col -= 1;
                }
                break;
            case 3:
                if(z.row != 0 && z.col != maze[0].length - 1 && !maze[z.row - 1][z.col + 1] )
                {
                        zombies[done].row -= 1;
                        zombies[done].col += 1;
                }
                break;
            default: break;
        }
        
    }
    
    //Makes zombies move randomly in Horizontal or Vertical Positions
    public static void moveZombie()
    {
        Random r = new Random();
        Coordinates z;
        int done = 0;
        do
        {
            z = zombies[done];
            int pos = r.nextInt(6);
            switch(pos)
            {
                case 0:
                    if(z.row != 0 && !maze[z.row - 1][z.col])
                            zombies[done].row -= 1;
                    break;
                case 1:
                    if(z.row != maze.length -1 && !maze[z.row + 1][z.col])
                            zombies[done].row += 1;
                    break;
                case 2:
                    if(z.col != 0 && !maze[z.row][z.col - 1])
                            zombies[done].col -= 1;
                    break;
                case 3:
                    if(z.col != maze[0].length - 1 && !maze[z.row][z.col + 1])
                            zombies[done].col += 1;
                    break;
                case 4: 
                    moveDiag(z, done);
                    break;
                default: break;
            }
            done++;
        }while(done < zombies.length);
    }
    
    //Randomly assigns position to zombies and map
    public void position()
    {
        Random r = new Random();
        int index = 0;
        int count = 0;
        
        exit = new Coordinates(maze.length -1 ,maze[0].length -1);
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j < maze[0].length; j++)
                if(!maze[i][j])
                    count++;
        
        Coordinates[] possibles = new Coordinates[count];
        
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j < maze[0].length; j++)
                if(!maze[i][j])
                {
                    possibles[index] = new Coordinates(i,j);
                    index++;
                }
        
        map = possibles[r.nextInt(possibles.length)];
        index = 0;
        
        Coordinates x = possibles[r.nextInt(possibles.length)];
        do
        {
            x = possibles[r.nextInt(possibles.length)];
            if(!map.equals(x) && !me.equals(x) && !exit.equals(x)&& !isZombie(x))
            {
                zombies[index] = x;
                index++;
            }
        }while(index < zombies.length);
    }
    
    //Add all the false coordinates in the union find
    public static void addIndex()
    {
        Coordinates a;
        for(int i =0 ; i < row;  i++)
            for(int j = 0; j< col; j++)
                if( maze[i][j] == false)
                {
                    a = new Coordinates(i,j);
                    uf.add(a); 
                }
    }
    
    //Checks percolation from player to exit through map
    public static boolean isConnected()
    {
        int m = uf.find(MazeL3.me);
        int ma = uf.find(MazeL3.map);
        int e = uf.find(MazeL3.exit);
        if(m == ma && ma == e && m ==e)
            return true;
        return false;
    }
    
    //Creates Regions with same Set IDs
    public static void findRegions()
    {
        Coordinates a,b;
        for(int i =0 ; i < row ;  i++)
            for(int j = 0; j< col; j++ )
                if( maze[i][j] == false)
                {
                    a = new Coordinates(i,j);
                    
                    //Checks Upper Position
                    int x = uf.find(a);
                    if(i != 0 && maze[i-1][j]==false)
                    {
                        b = new Coordinates(i-1,j);
                        int y = uf.find(b);
                        uf.union(x,y);
                    }
                    
                    //Checks Left Position
                    int xx = uf.find(a);
                    if (j != 0 && maze[i][j-1] == false)
                    {
                        b = new Coordinates(i,j-1);
                        int z = uf.find(b) ;
                        uf.union(xx, z);
                    }
                }  
    }
    
    //Displays the grid with paths Highlighted with different Colours
    public static void drawMazeSetsInColor(UnionFind uf)
    {
        String[] use_color = new String[ maze.length * maze[0].length];
        String color;
        Coordinates a;
        
        int color_loop = 0;
        
        for(int i=0; i<maze.length; i++)
        {
            for(int j=0; j<maze[0].length; j++)
            {   
                a = new Coordinates(i,j);
                Integer in_set = uf.find(a);
                if ( in_set != null )
                {
                    color = use_color[in_set];
                    if ( color == null )
                    {
                        color = colors[ color_loop++ % colors.length ];
                        use_color[in_set] = color;
                    }
                }
                else
                    color = BG_ANSI_WHITE;
                Coordinates test = new Coordinates(i,j);
                if(!maze[i][j])
                {
                    if(me.equals(test) && !isZombie(me))
                        System.out.print(color +"@  " + ANSI_RESET);
                    else if(exit.equals(test) && !exit.equals(me))
                        System.out.print(color + "E  " + ANSI_RESET);
                    else if(map.equals(test) && reachMap == 0)
                        System.out.print(color + "M  " + ANSI_RESET);
                    else if(isZombie(test))
                        System.out.print(color + "Z  " + ANSI_RESET);
                    else
                        System.out.print(color + ".  " + ANSI_RESET);
                }
                else
                    System.out.print(BG_ANSI_WHITE + "#  " + ANSI_RESET);
            }
            System.out.println();
        }
    }
    
    //Executes Level 3
    public static void level3()
    {
        Scanner scan = new Scanner(System.in);
        double pro = 0.25;
        MazeL3 grid;
        
        System.out.println("\nLevel 3 Begins...\n");
        
        //Asks for probabilty of walls from user until appropriate probability is entered
        do
        {
            System.out.print("Enter the Probability of the Walls (0.0 - 0.45): ");
            pro = scan.nextDouble();
            if(pro > 0.45)
                System.out.println("Invalid Input. Please Try Again");
        }while(pro > 0.45);
        
        //Creates the grid until one is made with common path from player to exit through map
        do
        {
            grid = new MazeL3(25,50, pro);
            if(MazeL2.moves != 0)
                zombies = new Coordinates[MazeL2.moves * 4];
            else
                zombies = new Coordinates[20];
            grid.position();
            uf = new UnionFind<>();
            
            addIndex();
            findRegions();
        }while(!isConnected());   
            System.out.println();
        
        //Executes until player reaches Exit or zombie
        do
        {
            if(reachMap == 0)
                grid.drawMaze();
            System.out.println();
            moves(uf);
            System.out.println();
        }while(!me.equals(exit) && !isZombie(me));
        
        if(me.equals(exit))
        {
            System.out.print("Yeah !!! You have been able to Deceive the Zombies and Reach the Exit.");
            GraphEdge.level4();
        }
        else if(isZombie(me))
            System.out.println("Game Over. Zombie has eaten you");
    }
}
