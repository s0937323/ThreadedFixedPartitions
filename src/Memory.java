//Asad Arif - s0937323
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Memory implements Runnable
{
	private static int memoryNeeded;
    private static final int TOTALPARTITIONS = 5;
    private static final int PART0 = 200;
    private static final int PART1 = 300;
    private static final int PART2 = 200;
    private static final int PART3 = 100;
    private static final int PART4 = 200;
    public static Job[] partitionsFilled = new Job[TOTALPARTITIONS];
    private static final int BIGGESTPARTITION = PART1;
	public static Thread part0; 
	public static Thread part1; 
	public static Thread part2; 
	public static Thread part3; 
	public static Thread part4; 
	static boolean spamcheck = false;
	static Job currentJob;
	static Job previousJob;
	ArrayList<Job> jobQueue;
	private final static Lock lock = new ReentrantLock(); //Lock stuff?

	//Constructor
	public Memory(ArrayList<Job> jobQueue)
	{
		this.jobQueue = jobQueue;
	}
	
	//Loads all of the jobs from the job queue to the partitions. If no partition is currently available for a certain job,
	//move the job to the back of the queue until one is ready for it. 
    public static void loadJob(ArrayList<Job> jobQueue)
    {  
    	lock.lock();
    	
    	//ANTI-SPAM (Line #77 was flooding console with output)
    	//---------------------------------------------------------------------------
    	if(jobQueue.get(0) == currentJob || jobQueue.get(0) == previousJob)
    	{
    		spamcheck = true;
    	}
    	else
    		spamcheck = false;
    	//---------------------------------------------------------------------------
    	previousJob = currentJob;
    	
    	
        currentJob = jobQueue.get(0);
        memoryNeeded = currentJob.getMemoryNeeded();
 
        if(memoryNeeded > BIGGESTPARTITION) //If memory needed for job exceeds all partition maximums, reject job
        {
            System.out.println("System partitions do not have enough memory to complete Job "+ currentJob.getJobID() + ". Removing the job from queue.");
            jobQueue.remove(0);
            jobQueue.trimToSize();
            lock.unlock();
            loadJob(jobQueue);
        }
        else if(memoryNeeded <= BIGGESTPARTITION) //If the job can actually be processed
        {          
            if(checkPartitions(currentJob.getMemoryNeeded()) >= 0) //If there is a free partition of the appropriate size available for the job (first-come-first-serve)
            {
               System.out.println("Job " + currentJob.getJobID() + " added to memory (Partition " +checkPartitions(currentJob.getMemoryNeeded()) + ").");
               Memory.loadPartitions(currentJob, checkPartitions(currentJob.getMemoryNeeded())); //Load the partition with the job
               jobQueue.remove(0);
               jobQueue.trimToSize();
               lock.unlock();
            }
            else //If there are no free partitions of the appropriate size currently available, move job to back of queue
            {
            	if (spamcheck == false)
            		System.out.println("All partitions filled. Moving Job " + currentJob.getJobID() + " to back of the queue."); //This was flooding console with text. Fixed now.
                jobQueue.remove(0);
                jobQueue.add(currentJob);
                jobQueue.trimToSize();
                lock.unlock();
            }
        }
        else //Something weird must have happened to get over here. Hopefully this fixes it.
        {
        	lock.unlock();
            loadJob(jobQueue);
        }
    }
      
    //Checks to see if there is a partition not currently occupied that can also fit the job
    private static int checkPartitions(int memory) 
    {
    	
			if(partitionsFilled[0] == null)
			{
				if(memory <= PART0)
				{
					return 0; //Partition 0 available and can handle the load
				}
			}
			
			if(partitionsFilled[1] == null)
			{
				if(memory <= PART1)
				{
					return 1; //Partition 1 available and can handle the load
				}
			}
			
			if(partitionsFilled[2] == null)
			{
				if(memory <= PART2)
				{
					return 2; //Partition 2 available and can handle the load
				}
			}
			
			if(partitionsFilled[3] == null)
			{
				if(memory <= PART3)
				{
					return 3; //Partition 3 available and can handle the load
				}
			}
			
			if(partitionsFilled[4] == null)
			{
				if(memory <= PART4)
				{
					return 4; //Partition 4 available and can handle the load
				}
			}
			
			return -1; //All partitions filled
		}


    public static void setPartitions(int partition)
    {
    	lock.lock();
    	partitionsFilled[partition] = null; //Set partition back to empty
    	lock.unlock();
    }
    
    
    //Start whatever partition thread needs to be started
    public static void startThread(Job job, int partition)
    {
    	lock.lock();
    	if(partition == 0)
    	{
    		part0 = new Thread(new Partition(job, partition));
    		partitionsFilled[0] = job;
    		part0.start();
    	}
    	if(partition == 1)
    	{
    		part1 = new Thread(new Partition(job, partition));
    		partitionsFilled[1] = job;
    		part1.start();
    	}
    	if(partition == 2)
    	{
    		part2 = new Thread(new Partition(job, partition));
    		partitionsFilled[2] = job;
    		part2.start();
    	}
    	if(partition == 3)
    	{
    		part3 = new Thread(new Partition(job, partition));
    		partitionsFilled[3] = job;
    		part3.start();
    	}
    	if(partition == 4)
    	{
    		part4 = new Thread(new Partition(job, partition));
    		partitionsFilled[4] = job;
    		part4.start();
    	}
    	lock.unlock();
    }
 
    //Load the necessary partition
    public static void loadPartitions(Job job,int partition)
    { 
    	lock.lock();
    	startThread(job, partition);
    	lock.unlock();
    }
    
    
    @Override
    public void run()
    {
    	while(jobQueue.isEmpty() == false)
    	{
    		loadJob(jobQueue);
    	}
    }
}
