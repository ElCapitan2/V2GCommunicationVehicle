/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.commonclasses.tasks.Task;

/**
 * Class Taskworker Implements runnable. Executes Tasks, i.e. fills data into 
 * the buffers. 
 * 
 * @author Alexander Forell
 */
public class TaskWorker extends Thread{
    /**
     * Field for taskname
     */
    private final String taskName;
    /**
     * field for number of iterations
     */
    private final int iterations;
    /**
     * Intervall for transmission
     */
    private final int intervall;
    /**
     * vehicleSimulation Object
     */
    private final VehicleSimulator vehSim;
    /**
     * task Object
     */
    private final Task task;
    
    public TaskWorker(String taskName, int iterations, int intervall, VehicleSimulator vehSim, Task task){
        /**
         * get Field variables from constructor
         */
        super(task.getTaskID());
        this.taskName = taskName;
        this.task = task;
        this.iterations = iterations;
        this.intervall = intervall;
        this.vehSim = vehSim;
    }
    
    /**
     * Reads Task in specified intervalls and adds data to the task
     */
    @Override
    public void run() {
        Date date;
        if (iterations == -1){
            while (true){
                switch (taskName){
                    case "Speed":
                        date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd-HH:mm:ss.SSS");
                        double speed = vehSim.getSpeed();
                        task.addDataElement("Speed", ""+speed, double.class.getSimpleName() ,ft.format(date));
                        System.out.println("TaskID: "+ task.getTaskID() +" Speed: "+vehSim.getSpeed()+" TimeStamp: " + ft.format(date));
                        break;
                }
                try {
                    Thread.sleep(intervall);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TaskWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else {
            for (int i = 0; i < iterations; i++){
                switch (taskName){
                    case "Speed":
                        date = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd-HH:mm:ss.SSS");
                        double speed = vehSim.getSpeed();
                        task.addDataElement("Speed", ""+speed, double.class.getSimpleName(), ft.format(date));
                        //System.out.println("TaskID: "+ task.getTaskID() +" Speed: "+vehSim.getSpeed()+" TimeStamp: " + ft.format(date));
                        break;
                }
                try {
                    Thread.sleep(intervall);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TaskWorker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            task.endTask();
        }
    }
    
    public String getFunctionName(){
        return taskName;
    }
    
    public int getIterations(){
        return iterations;
    }
    
    public int getIntervall(){
        return intervall;
    }
    
}
