import java.util.*;

public class MazeL2
{
    public static char[][] grid;
    char target, boundary;
    public static ArrayDeque<Coordinates> des = new ArrayDeque<>();
    public static final String BG_ANSI_GREEN = "\u001B[42m";
    public static final String BG_ANSI_CYAN = "\u001B[46m";
    public static final String BG_ANSI_BLUE = "\u001B[44m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static char[] alpha;
    public static Coordinates me, exit;
    public static int moves = 0;
    
    //To Construct a Grid of specified size
    public MazeL2(int rows, int cols)
    {
        grid = new char[rows][cols];
        Random random = new Random();
        int r;
        for(int i =0; i< grid.length; i++)
            for(int j = 0; j < grid[0].length; j++)
            {
                r = random.nextInt(2);
                if(r == 0)
                    grid[i][j] = '_';
                else
                    grid[i][j] = ' ';
            }
    }
    
    //To assign random position to the player, 2 coins
    public void pose()
    {
        Random rf = new Random();
        int count = 0;
        int _count = 0;
        int index = 0;
        
        //Counts the number of positions with spaces
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j <grid[0].length; j++)
                if(grid[i][j] == ' ')
                    count++;
        
        //Counts the number of positions with underscores
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j <grid[0].length; j++)
                if(grid[i][j] == '_')
                    _count++;
        
        Coordinates[] narray = new Coordinates[count];
        Coordinates[] earray = new Coordinates[_count];
        
        //Storing coordinates of all spaced position in an array
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j <grid[0].length; j++)
                if(grid[i][j] == ' ')
                {
                    narray[index] = new Coordinates(i,j);
                    index += 1;
                }
        
        index = 0;
        //Storing coordinates of all underscored position in an array
        for(int i = 0; i < grid.length; i++)
            for(int j = 0; j <grid[0].length; j++)
                if(grid[i][j] == '_')
                {
                    earray[index] = new Coordinates(i,j);
                    index += 1;
                }
        
        int n,g;
        
        //Randomly assigns the Coordinates to the player and Exit
        n = rf.nextInt(narray.length);
        g = rf.nextInt(earray.length);
        me = narray[n];
        exit = earray[g];
    }
    
    //To display the Grid
    public void displayTheGrid()
    {
        int k = 0;
        int l = 0;
        int y = 0;
        System.out.print(" ");
        
        //Printing alphabets for upper horizontal Coordinates
        for(int x = 0; x < grid[0].length; x++)
        {
            if(l >= 25)
            {
                System.out.print(alpha[y]);
                y++;
            }
            else
                System.out.print(" ");
            l++;
        }
        
        //Printing alphabets for vertical Coordinates
        int p = 0;
        System.out.print("\n ");
        for(int x = 0; x < grid[0].length; x++)
        {
            System.out.print(alpha[p]);
            p++;
            if(p == 25)
                p = 0;
        }
        
        System.out.println();
        
        //To print the whole Grid
        for(int i = 0; i < grid.length; i++)
        {
            System.out.print(alpha[k]);
            for(int j = 0; j < grid[0].length; j++)
            {
                switch (grid[i][j]) {
                    case '_':
                        if(i == exit.row && j == exit.col)
                            System.out.print(BG_ANSI_BLUE + 'E' + ANSI_RESET);
                        else if(i == me.row && j == me.col)
                            System.out.print(BG_ANSI_BLUE + '@' + ANSI_RESET);
                        else
                            System.out.print(BG_ANSI_BLUE + '_' + ANSI_RESET);
                        break;
                    case ' ':
                        if(i == me.row && j == me.col)
                            System.out.print(BG_ANSI_GREEN + '@' + ANSI_RESET);
                        else if(i == exit.row && j == exit.col)
                            System.out.print(BG_ANSI_GREEN + 'E' + ANSI_RESET);
                        else
                            System.out.print(BG_ANSI_GREEN + ' ' + ANSI_RESET);
                        break;
                    default:
                        break;
                }
            }
        System.out.println(alpha[k]);
        k++;
        }
        
        //Printing alphabets for lower horizontal Coordinates
        int j = 0;
        System.out.print(" ");
        for(int x = 0; x < grid[0].length; x++)
        {
            System.out.print(alpha[j]);
            j++;
            if(j == 25)
                j = 0;
        }
        
        System.out.println();
    }
    
    //Creates the Alphabet Array
    public void test()
    {
        int k = 0;
        alpha = new char[26];
        for(int i = 65; i < 91; i ++)
        {
            alpha[k] = (char)i;
            k++;
        }
    }
    
    //To find character in array and return it's index
    public static int findElement(char a)
    {
        for(int i = 0; i < alpha.length; i++)
        {
            if(alpha[i] == a)
                return i;
        }
        return -1;
    }
    
    //To create number coordinates from the user's entered alphabet Coordinates
    public Coordinates fCoord()
    {
        System.out.print("Moves Left: " + moves + " Enter the Coordinates for Flood Fill (e.g. AC or ACC): ");
        Scanner scan = new Scanner(System.in);
        String flood = scan.nextLine();
        System.out.println();
        flood = flood.toUpperCase();
        char[] coor = flood.toCharArray();
        Coordinates d;
        int x,y;
        if(coor.length == 2)
        {
            x = findElement(coor[0]);
            y = findElement(coor[1]);
            moves++;
        }
        else if(coor.length == 3)
        {
            x = findElement(coor[0]);
            y = findElement(coor[1]) + 25;
            moves++;
        }
        else
            return null;
        
        d = new Coordinates(x,y);
        return d;
    }
    
    //To Flood Fill the area starting from coordinates defined by user
    public void floodFill()
    {
        Coordinates ab = fCoord();
        if(ab != null)
        {
            int row =ab.row;
            int col =ab.col;
            target = grid[row][col];
            Coordinates d = null;

            if(target == '_')
                boundary = ' ';
            else
                boundary = '_';

            grid[row][col] = '.';

            do{
                if(row != 0 && grid[row - 1][col] == target)
                    des.add(new Coordinates(row -1,col));
                if(col != 0 && grid[row][col - 1] == target)
                    des.add(new Coordinates(row,col -1));
                if(row < grid.length - 1 && grid[row + 1][col] == target)
                    des.add(new Coordinates(row +1,col));
                if(col < grid[0].length -1 && grid[row][col + 1] == target)
                    des.add(new Coordinates(row,col +1));

                if(des.isEmpty())
                    grid[row][col] = '.';
                else
                {
                    do
                    {
                        if(des.isEmpty()) return;
                        d = des.remove();
                    }while(grid[d.row][d.col] == '.');

                    row = d.row;
                    col = d.col;
                    grid[d.row][d.col] = '.';
                }
            }while(d != null && neighbour(d.row, d.col) || !des.isEmpty());
        }
        else
            System.out.print("Invalid Input");
    }
    
    //To test if the neighbours of the current position are valid for Flood Fill
    public boolean neighbour(int row, int col)
    {
        if(row != 0 && grid[row - 1][col] == target)
            return true;
        if(col != 0 && grid[row][col - 1] == target)
            return true;
        if(row < grid.length - 1 && grid[row + 1][col] == target)
            return true;
        if(col < grid[0].length -1 && grid[row][col + 1] == target)
            return true;
            
        return false;
    }
    
    //Changes the painted Grid to the Boundary
    public void ChangePaintToBoundaryColor()
    {
        for(int i =0; i <grid.length; i++)
            for(int j =0;  j<grid[0].length; j++)
                if(grid[i][j] == '.')
                    grid[i][j] = boundary;
    }
    
    //Executes Level 2
    public static void level2()
    {
        System.out.println("\nLevel 2 Begins...");
        MazeL2 g = new MazeL2(19,50);
        String go;
        g.test();
        g.pose();
        
        //Executes until the player and exit hits th paint at the same time
        do
        {
            g.displayTheGrid();
            g.floodFill();
            if(g.grid[me.row][me.col] == '.' && g.grid[exit.row][exit.col] == '.')
            {
                System.out.println("You won!!! Moves Played: " + moves);
                break;
            }
            g.ChangePaintToBoundaryColor();
        }while(true);
        
        //Starts level 3
        MazeL3.level3();
    }
}
