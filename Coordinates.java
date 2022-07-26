public class Coordinates
{
    public int row;
    public int col;
    
    //Constructor
    public Coordinates(int r, int c)
    {
        row = r;
        col = c;
    }
    
    //Overriden Equals Method
    public boolean equals(Object t)
    {
        Coordinates a = (Coordinates)t;
        return !(this.row != a.row || this.col != a.col);
    }
    
    public int hashCode()
    {
        return 1;
    }
}
