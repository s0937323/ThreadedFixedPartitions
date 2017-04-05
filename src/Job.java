//Asad Arif - s0937323
public class Job
{
    private int id;
    private int memoryNeeded;
    private int runTime;
 
    public Job()
    {
        id = 0;
        memoryNeeded = 0;
        runTime = 0;
    }
 
    public Job(int id, int reqMem, int run)
    {
        this.id = id;
        memoryNeeded = reqMem;
        runTime = run;
    }
   
    //Get Job ID, MemoryNeeded, and RunTime
    public int getJobID()
    {
        return id;
    }
   
    public int getMemoryNeeded()
    {
        return memoryNeeded;
    }
    
    public int getRunTime()
    {
    	return runTime;
    }
}