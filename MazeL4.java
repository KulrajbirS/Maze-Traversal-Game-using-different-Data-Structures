import java.util.*;

public class MazeL4
{
    public boolean[][] maze;
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BG_ANSI_GREEN = "\u001B[42m";
    public static final String BG_ANSI_CYAN = "\u001B[46m";
    public static final String BG_ANSI_YELLOW = "\u001B[43m";
    public static final String BG_ANSI_RED = "\u001B[41m";
    public static final String BG_ANSI_PURPLE = "\u001B[45m";
    public static ArrayList<Coordinates> coins = new ArrayList<>();
    public static ArrayList<Coordinates> deacCoins = new ArrayList<>();
    public static Coordinates me, exit;
    public static Coordinates[] zombies;
    public static int moves = 100;
    
    //Constructor
    public MazeL4(int rows, int cols)
    {
        maze = new boolean[rows][cols];
        for(int i=0; i<maze.length; i++)
            for(int j=0; j<maze[0].length; j++)
                maze[i][j] = true;
    }
    
    //Prints the Maze
    public void drawMaze()
    {
        Coordinates holder;
        for(int i=0; i<maze.length; i++)
        {
            for(int j=0; j<maze[0].length; j++)
            {
                holder = new Coordinates(i,j);
                if(maze[i][j])
                    System.out.print("#");
                else if(holder.equals(me))
                    System.out.print(BG_ANSI_GREEN + "@" + ANSI_RESET);
                else if(holder.equals(exit))
                    System.out.print(BG_ANSI_RED + "E" + ANSI_RESET);
                else if(isCoin(holder))
                    System.out.print(BG_ANSI_YELLOW + "C" + ANSI_RESET);
                else if(isZombie(holder))
                    System.out.print(BG_ANSI_PURPLE + "Z" + ANSI_RESET);
                else
                    System.out.print(BG_ANSI_CYAN + "." + ANSI_RESET);
            }
            
            System.out.println();
        }
    } 
    
    //Compares if player achieves the coin and removes it
    public static boolean metCoin(Coordinates c)
    {
        for(Coordinates x: coins)
            if(c.equals(x))
            {
                coins.remove(x);
                return true;
            }
        
        return false;
    }
    
    //Adds 100 moves when player achieves the coin
    public static void move()
    {
        if(metCoin(me))
            moves += 100;
        System.out.println("Moves: " + moves);
    }
    
    //Only compares if the passed coordinate is a coin
    public static boolean isCoin(Coordinates c)
    {
        for(Coordinates x: coins)
            if(c.equals(x))
                return true;
        
        return false;
    }
    
    //Moves the player according to input and reduces the moves left by 1
    public void moves()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Please select a move: u, d ,r & l: ");
        String move = scan.nextLine();
        if(move.equals("u") && me.row != 0 && !maze[me.row - 1][me.col])
        {
            me.row -= 1;
            moveZombie();
            moves--;
        }
        else if(move.equals("d") && me.row != maze.length -1 && !maze[me.row + 1][me.col])
        {
            me.row += 1;
            moveZombie();
            moves--;
        }
        else if(move.equals("l") && me.col != 0 && !maze[me.row][me.col - 1])
        {
            me.col -= 1;
            moveZombie();
            moves--;
        }
        else if(move.equals("r") && me.col != maze[0].length -1 && !maze[me.row][me.col + 1])
        {
            me.col += 1;
            moveZombie();
            moves--;
        }
    }
    
    //Randomly Assigns position to coins, zombies and exit
    public void pose()
    {
        Random rf = new Random();
        int count = 0;
        int index = 0;
        
        ArrayList<Coordinates> possible = new ArrayList<>();
        ArrayList<Coordinates> ePossible = new ArrayList<>();
        Coordinates a;
        
        //Adds coordinates of possible positions to an Array List
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j <maze[0].length; j++)
            {
                a = new Coordinates(i,j);
                if(maze[i][j] == false)
                {
                    possible.add(a);
                    index++;
                }
            }
        
        //Adds the possible exit values in last column to an Array List
        for(Coordinates b: possible)
        {
            if(b.col == maze[0].length-1)
                ePossible.add(b);
        }
        
        //Call the method to randomly assign value to player
        mePose();
        
        //Ranomly assigns position to exit in the last column
        exit = ePossible.get(rf.nextInt(ePossible.size()));
        
        Coordinates holder;
        count = 0;
        
        //Assigns Random coordinates to the coins
        for(int i = 0; i < possible.size(); i++)
        {
            holder = possible.get(rf.nextInt(possible.size()));
            if(!holder.equals(me) && !holder.equals(exit) && count < 10)
            {
                coins.add(holder);
                count++;
            }
        }
        
        index = 0;
        Coordinates c;
        
        //Assigns Random coordinates to the zombies
        do
        {
            c = possible.get(rf.nextInt(possible.size()));
            if(!me.equals(c) && !exit.equals(c)&& !isZombie(c) && !isCoin(c))
            {
                zombies[index] = c;
                index++;
            }
        }while(index < zombies.length);
    }
    
    //Assigns position to the player
    public void mePose()
    {
        int count = 0;
        int index = 0;
        Random r = new Random();
        ArrayList<Coordinates> mPossible = new ArrayList<>();
        ArrayList<Coordinates> possible = new ArrayList<>();
        
        Coordinates a;
        //Adds coordinates of possible positions to an Array List
        for(int i = 0; i < maze.length; i++)
            for(int j = 0; j <maze[0].length; j++)
            {
                a = new Coordinates(i,j);
                if(maze[i][j] == false)
                {
                    possible.add(a);
                    index++;
                }
            }
        
        //Adds the possible player values in first column to an Array List
        for(Coordinates b: possible)
        {
            if(b.col == 0)
                mPossible.add(b);
        }
        
        Coordinates holder;
        //Adds the possible player values in first column to an Array List
        do
        {
            holder = mPossible.get(r.nextInt(mPossible.size()));
            if(!isCoin(holder) && !isZombie(holder))
            {
                me = holder;
                break;
            }
        }while(true);
    }
    
    //Compares the passed coordinates with zombies in the Array
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
    
    //Moves Zombies in Horizontal or Vertical Directions
    public void moveZombie()
    {
        Random r = new Random();
        Coordinates z;
        int done = 0;
        do
        {
            z = zombies[done];
            int pos = r.nextInt(5);
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
                default: break;
            }
            done++;
        }while(done < zombies.length);
    }
}
