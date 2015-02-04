/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle.vehiclefunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.commonclasses.requests.Request;
import v2gcommunication.commonclasses.requests.RequestType;
import v2gcommunication.commonclasses.tasks.ParameterSet;
import v2gcommunication.commonclasses.tasks.Task;
import v2gcommunication.commonclasses.requests.AddRequest;
import v2gcommunication.commonclasses.tasks.StartTaskTransmit;
import v2gcommunication.commoninterfaces.TaskWorker;
import v2gcommunication.vehicle.VehicleSimulator;

/**
 * Vehicle side implementation answer a data request 
 * 
 * Class implements Taskworker which extends Runnable.
 * 
 * The run performs the vehicle actions
 * 
 * @author Alexander Forell
 */
public class RequestVehicleData implements TaskWorker{
    private Request request;
    private StartTaskTransmit taskReply;
    
    /**
     * Run mehthod must be overridden as it is defined in TaskWorker.
     * 
     * It evaluates the request, creates a task and adds the data as specified 
     * to it.
     * 
     * Once all data is transmitted it closes the task.
     * 
     */
    @Override
    public void run() {
        if (request.getRequestType()==RequestType.REQUEST){
            ArrayList<ParameterSet> parameters = new ArrayList<ParameterSet>();
            parameters.addAll(request.getParameterSet());
            ArrayList<String> functionNames = new ArrayList<String>();
            int intervall = 1000;
            int iterations = 0;
            for (ParameterSet para:parameters){
                if (para.parameterName.equals("functionName") && para.parameterType.equals(String.class.getName())){
                    functionNames.add(para.parameterValue);
                }
                if (para.parameterName.equals("intervall") && para.parameterType.equals(int.class.getName())){
                    intervall = Integer.parseInt(para.parameterValue);
                }
                if (para.parameterName.equals("iterations") && para.parameterType.equals(int.class.getName())){
                    iterations = Integer.parseInt(para.parameterValue);
                }
            }
            VehicleSimulator vehSim = VehicleSimulator.getInstance();
            Task myTask = taskReply.startTaskTransmit(request.getUserNameVIN(), request.getRequestID());
            Date date;
            if (iterations == -1){
                while (true){
                    switch (functionNames.get(0)){
                        case "Speed":
                            date = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd-hh:mm:ss.SSS");
                            double speed = vehSim.getSpeed();
                            myTask.addDataElement("Speed", ""+speed, double.class.getSimpleName() ,ft.format(date));
                            System.out.println("TaskID: "+ myTask.getTaskID() +" Speed: "+vehSim.getSpeed()+" TimeStamp: " + ft.format(date));
                            break;
                    }
                    try {
                        Thread.sleep(intervall);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(v2gcommunication.vehicle.TaskWorker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else {
                for (int i = 0; i < iterations; i++){
                    switch (functionNames.get(0)){
                        case "Speed":
                            date = new Date();
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd-hh:mm:ss.SSS");
                            double speed = vehSim.getSpeed();
                            myTask.addDataElement("speed", ""+speed, double.class.getSimpleName(), ft.format(date));
                            break;
                    }
                    try {
                        Thread.sleep(intervall);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(v2gcommunication.vehicle.TaskWorker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                myTask.endTask();
            }
        }
    }

    /**
     * Method defined in Taskworker is used for a the runMethod to reply using
     * Requests or Tasks.
     * 
     * @param requestProcessed  The Request which is currently handled
     * @param requestReply      The Request used to reply
     * @param taskReply         The Task used to reply.
     */
    @Override
    public void inputForRunnable(Request requestProcessed, AddRequest requestReply, StartTaskTransmit taskReply) {
        this.request = requestProcessed;
        this.taskReply = taskReply;
    }
}
