import java.io.*;
import java.util.*;

public class GraphEdge <V> implements Comparable<GraphEdge>

{
    /*
    Declaring UnionFind class object, priority queue and arraylist 
    Declaring objects of class GraphEdge of type V
    */
    public static UnionFind <Coordinates> uf = new UnionFind<>();
    public static PriorityQueue <GraphEdge <Coordinates>> sortEdges = new PriorityQueue <>();
    public static ArrayList<GraphEdge<Coordinates>> minEdges = new ArrayList<>();
    public static MazeL4 grid;
    public V v1;
    public V v2;
    public int e;
    
    //Constructor
    public GraphEdge (V v1, V v2 , int e)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.e = e;
    }
    
    //Executes Level 4
    public static void level4()
    { 
        grid = new MazeL4(21,75);
        System.out.println("\nLevel 4 Begins...");
        if(MazeL2.moves != 0)
            grid.zombies = new Coordinates[MazeL2.moves * 4];
        else
            grid.zombies = new Coordinates[20];
        addEdges();
        createMinTree();
        makeFalse();
        grid.pose();
        grid.drawMaze();
        do
        {
            grid.move();
            grid.moves();
            if(grid.isZombie(grid.me))
                grid.mePose();
            grid.drawMaze();
        }while(!grid.me.equals(grid.exit) && grid.moves > 0);
        
        if(grid.me.equals(grid.exit))
            System.out.print("\nCongratulations !!! You have Conquered the Supreme Level" +
                             "\nThe Game has Ended.");
        else if(grid.moves == 0)
            System.out.print("\nOh No !!! You are Out Of Moves. Game Over.");
    }
      
    //Add Edges with Random weights to the Priority Queue
    public static void addEdges()
    {
        Coordinates a, b;
        Random r = new Random();
        for(int i =0; i < grid.maze.length; i += 2)
            for(int j = 0; j < grid.maze[0].length; j +=2)
            {
                if(j+2 < grid.maze[0].length)
                {
                    a = new Coordinates(i,j);
                    b = new Coordinates(i,j+2);
                    sortEdges.add(new GraphEdge(a,b,r.nextInt(1000)));
                }
            }
        
        for(int i =0; i < grid.maze[0].length; i += 2)
            for(int j = 0; j < grid.maze.length; j +=2)
            {
                if(j + 2 < grid.maze.length)
                {
                    a = new Coordinates(j,i);
                    b = new Coordinates(j+2,i);
                    sortEdges.add(new GraphEdge(a,b,r.nextInt(1000)));
                }
            }
    }
    
    //Carves the Path using the Edges of Minimum Spanning Tree created by Kruskal's Algorithm
    public static void makeFalse()
    {
        for(GraphEdge x: minEdges)
        {
            Coordinates a = (Coordinates)x.v1;
            Coordinates b = (Coordinates)x.v2;
            if(a.row == b.row && a.col < grid.maze[0].length)
            {
                grid.maze[a.row][a.col] = false;
                grid.maze[a.row][a.col + 1] = false;
                grid.maze[b.row][b.col] = false;
            }
            else if(a.col == b.col  && a.row < grid.maze.length)
            {
                grid.maze[a.row][a.col] = false;
                grid.maze[a.row + 1][a.col] = false;
                grid.maze[b.row][b.col] = false;
            }
        }
    }
    
    //Creates Minimum Spanning Tree using Kruskal's Algorithm and stores the edges in an Array List
    public static void createMinTree()
    {
        while(!sortEdges.isEmpty())
        {
            GraphEdge h = sortEdges.remove();
            Coordinates a = (Coordinates)h.v1;
            Coordinates b = (Coordinates)h.v2;
            Integer set1 = uf.find(a);
            Integer set2 = uf.find(b);

            if(set1==null && set2 == null)
            {
                set1 = uf.add(a);
                set2 = uf.add(b);
                uf.union(set1, set2);
                minEdges.add(h);
            }
            else if(set1!= null && set2==null)
            {
                set2 = uf.add(b);
                uf.union(set1, set2);
                minEdges.add(h);
            }
            else if(set1 == null && set2!= null)
            {
                set1 = uf.add(a);
                uf.union(set1, set2);
                minEdges.add(h);
            }
            else if(set1!= null && set2!= null && !set1.equals(set2))
            {
                uf.union(set1, set2);
                minEdges.add(h);
            }
        }
    }
    
    //Compares the edges depending on the weight
    public int compareTo(GraphEdge edge)
    {
        if ( this.e<edge.e ) return -1; // e is less than edge.e
        if ( this.e>edge.e ) return  1; // e is greater than edge.e
        
        return 0; // e is equal to edge.e
    }
    
    
    
}
