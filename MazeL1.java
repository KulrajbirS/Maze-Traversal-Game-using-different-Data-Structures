import java.util.*;

public class MazeL1
{
    private boolean[][] maze;
    
    //Colour Variables
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BG_ANSI_GREEN = "\u001B[42m";
    public static final String BG_ANSI_WHITE = "\u001B[47m";
    public static final String BG_ANSI_YELLOW = "\u001B[43m";
    public static final String BG_ANSI_BLUE = "\u001B[44m";
    public static final String BG_ANSI_CYAN = "\u001B[46m";
    public static Coordinates coin1, coin2, me, holder;
    public static Coordinates exit = new Coordinates(0,0);
    public static int reveal = 0;
    public int destroyCoin = 0;
    
    //Method to construct a boolean maze
    public MazeL1(int rows, int col)
    {
        maze = new boolean[rows][col];
        for (int i = 0; i < maze.length; i++)
            for (int j = 0; j < maze[0].length; j++)
                maze[i][j] = true;
    }
    
    //To move the player according to the user input
    public void moves()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Please select a move: u, d ,r & l: ");
        String move = scan.nextLine();
        if(move.equals("u") && me.row != 0 && !maze[me.row - 1][me.col])
            me.row -= 1;
        else if(move.equals("d") && me.row != maze.length -1 && !maze[me.row + 1][me.col])
            me.row += 1;
        else if(move.equals("l") && me.col != 0 && !maze[me.row][me.col - 1])
            me.col -= 1;
        else if(move.equals("r") && me.col != maze[0].length -1 && !maze[me.row][me.col + 1])
            me.col += 1;
        
        if((me.equals(coin1) || me.equals(coin2)) && reveal < 1)
        {
            reveal++;
            if(me.equals(coin1))
            {
                coin1 = new Coordinates(-1,-1);
                destroyCoin = 1;
            }
            else if(me.equals(coin2))
            {
                coin2 = new Coordinates(-1,-1);
                destroyCoin = 2;
            }
        }
        if((me.equals(coin1) || me.equals(coin2)) && reveal == 1)
        {
            reveal++;
            if(destroyCoin == 1)
                coin2 = new Coordinates(-1,-1);
            else if(destroyCoin == 2)
                coin1 = new Coordinates(-1,-1);
        }
        
        if(reveal == 2)
            setExit();
    }
    
    //To assign random position to the player, 2 coins
    public void pose()
    {
        Random rf = new Random();
        int count = 0;
        int index = 0;
        
        //Counts the number of possible positions
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j <maze[0].length; j++)
                if(maze[i][j] == false)
                    count++;
        
        Coordinates[] narray = new Coordinates[count];
        
        //Stores the coordinates of possible positions in an array
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j <maze[0].length; j++)
                if(maze[i][j] == false)
                {
                    narray[index] = new Coordinates(i,j);
                    index += 1;
                }
        
        int k,l,n,g;
        
        //Randomly selects 4 non-repeated indexes of the array
        do
        {
            k = rf.nextInt(narray.length);
            l = rf.nextInt(narray.length);
            n = rf.nextInt(narray.length);
            g = rf.nextInt(narray.length);
        }while(k == l || l == n || k == n || k == g || l == g || n == g);
        
        //assigns coordinates to the player and coins
        coin1 = narray[k];
        coin2 = narray[l];
        me = narray[n];
        
        //Passes the coordinates of the exit to the holder
        makeHolder(narray[g]);
    }
    
    public void makeHolder(Coordinates c)
    {
        holder = c;
    }
    
    //Sets the coordinates of exit
    public void setExit()
    {
        exit = holder;
    }
    
    //Method to fill up the boolean maze with characters
    public void drawMaze()
    {
        Random r = new Random();
        int c_row = r.nextInt(maze.length - 4) + 2;
        int c_col = r.nextInt(maze[0].length - 4) + 2;
        Stack<Coordinates> moves = new Stack<>();
        Coordinates f1,f2,f3,f4;
        Coordinates[] m = new Coordinates[4];
        
        //The loop to carve the path in the boolean maze
        do
        {
            //Creates an array containing possible move options
            if(maze[c_row + 2][c_col])
            {
                f1 = new Coordinates(c_row +2, c_col);
                m[0] = f1;
            }
            if(maze[c_row - 2][c_col])
            {
                f2 = new Coordinates(c_row -2, c_col);
                m[1] = f2;
            }
            if(maze[c_row][c_col + 2])
            {
                f3 = new Coordinates(c_row, c_col + 2);
                m[2] = f3;
            }
            if(maze[c_row][c_col - 2])
            {
                f4 = new Coordinates(c_row, c_col - 2);
                m[3] = f4;
            }
            
            //Randomly Shuffling the array
            for (int i=0; i<4; i++) 
            {
                int i1 = r.nextInt(m.length);
                int i2 = r.nextInt(m.length);

                Coordinates tmp = m[i1];
                m[i1] = m[i2];
                m[i2] = tmp;
            }
            
            //Pushing valid values onto the Stack from array
            for(Coordinates mv: m)
                if(mv != null)
                    moves.push(mv);
            
            //Retrieving values from the Stack
            Coordinates move = null;
            do
            {
                move = moves.pop();
            }while(!moves.empty() && maze[move.row][move.col]==false);
            
            //Checking the validity of the move and making them false
            maze[move.row][move.col] = false;
            if(move.row < c_row)
                maze[move.row+1][move.col] = false;
            if(move.row > c_row)
                maze[move.row-1][move.col] = false;
            if(move.col < c_col)
                maze[move.row][move.col+1] = false;
            if(move.col > c_col)
                maze[move.row][move.col-1] = false;
            
            //Changing the current position to the new move's destination
            if(move.row >1 && move.col>1 && move.row <maze.length-2 && move.col<maze[0].length-2)
            {
            c_row = move.row;
            c_col = move.col;
            }
                
        }while(!moves.empty());
    }
    
    public void print()
    {
        Coordinates a;
        //Print out the Maze with the carved path
        for(int i = 0; i < maze.length; i++)
        {
            for(int j = 0; j < maze[0].length; j++)
            {
                a = new Coordinates(i,j);
                if(maze[i][j])
                    System.out.print(BG_ANSI_WHITE + "#" + ANSI_RESET);
                else if (!maze[i][j])
                        if(a.equals(coin1) || a.equals(coin2) && a != new Coordinates(-1,-1))
                            System.out.print(BG_ANSI_YELLOW + "C" + ANSI_RESET );
                        else if (a.equals(me))
                            System.out.print(BG_ANSI_BLUE + "@" + ANSI_RESET );
                        else if (reveal == 2 && a.equals(exit))
                            System.out.print(BG_ANSI_BLUE + "E" + ANSI_RESET );
                        else
                            System.out.print(BG_ANSI_GREEN + "." + ANSI_RESET );
            }
        System.out.println();
        }
    }
    
    //Executes Level 1
    public static void level1()
    {
        MazeL1 maze = new MazeL1(21,70);
        System.out.println("\nLevel 1 Begins....\n");
        maze.drawMaze();
        maze.pose();
        maze.print();
        
        //Executes level 1 until player reaches Exit
        do
        {
            maze.moves();
            maze.print();
        }while(!me.equals(exit));
        
        System.out.println("By Conquring the World of Programmers, you have reached the End\n");
        
        MazeL2.level2();
    }
}
