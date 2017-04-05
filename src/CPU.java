//Asad Arif - s0937323
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
public class CPU
{
    private static String jobList = "D:/ListOfJobs.txt";
    private static ArrayList<Job> jobQueue = null;
    private static BufferedReader fileReader = null;
    private static String line = "";
    private static Thread memory;
    private final static Lock lock = new ReentrantLock();

    
    //Read data from text file and populate jobQueue with contents
    public static void readFromFile()
    {
        try
        {
            jobQueue = new ArrayList<Job>();
            jobQueue.ensureCapacity(5);
            fileReader = new BufferedReader(new FileReader(jobList));
            while ((line = fileReader.readLine()) != null)
            {
                String[] values = line.split(",", 3);
                jobQueue.add(new Job(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2])));
            }             
            
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Cannot find file.");
        }
        catch (IOException e)
        {
            System.out.println("Cannot read file.");
        }
        finally
        {
            if (fileReader != null)
            {
                try
                {
                    fileReader.close();
                }
                catch (IOException e)
                {
                	System.out.println("Something went wrong.");
                }
            }
        }
    }
   
    
    //Main method
    public static void main(String[] args)
    {
        readFromFile();
        memory = new Thread(new Memory(jobQueue));
        memory.start();
       // while(jobQueue.isEmpty() == false)
      //  {
       // 	Memory.loadJob(jobQueue);
      //  }
    }
}