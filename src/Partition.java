//Asad Arif - s0937323
public class Partition implements Runnable {
Job job;
int partition;

	//Constructor
	public Partition(Job job, int partition)
	{
		this.job = job;
		this.partition = partition;
	}
	
	//Outputs what job the partition is "running" and then sleeps the thread for the amount of processing time needed
	@Override
	public void run() {
		try {
			//If the time in the text file is in milliseconds, leave it as it is
			if(job.getRunTime() >= 1000)
			{
				System.out.println("Currently processing Job " + job.getJobID() + " for " + job.getRunTime() / 1000 + " seconds on Partition " + partition + ".");
				Thread.sleep(job.getRunTime());
			}
			//If the time in the text file is in seconds, convert it to milliseconds
			else
			{
				System.out.println("Currently processing Job " + job.getJobID() + " for " + job.getRunTime() + " seconds on Partition " + partition + ".");
				Thread.sleep(job.getRunTime() * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Job " + job.getJobID() + " is complete.");
		Memory.setPartitions(partition); //Set partition as empty
	}
}
