/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import v2gcommunication.commonclasses.requests.RequestManagement;
import v2gcommunication.commonclasses.tasks.Task;
import v2gcommunication.commonclasses.tasks.TaskManagement;
import v2gcommunication.vehicle.vehiclefunctions.RequestVehicleData;

/**
 * Test class to simulate vehicles transmitting data. 
 * 
 * @author Alexander Forell
 */
public class Vehicle {
    private final String VIN;
    private final TaskManagement taskManagement;
    private final RequestManagement requestManagement;
    private final SessionManagementVehicle sessionManagementVehicle;
    private VehicleSimulator vehicleSimulator;
    private ArrayList<TaskWorker> tasks;
    Socket socket = null;
    
    
    Vehicle(String VIN){
        this.taskManagement = new TaskManagement();
        this.requestManagement = new RequestManagement();
        this.requestManagement.addTaskWorker(RequestVehicleData.class);
        this.vehicleSimulator = VehicleSimulator.getInstance();
        this.VIN = VIN;
        this.tasks = new ArrayList<TaskWorker>();
        this.requestManagement.setStartTaskTransmit(taskManagement);
        this.sessionManagementVehicle = new SessionManagementVehicle();
        this.sessionManagementVehicle.setNewReceiveTaskListener(taskManagement);
        this.sessionManagementVehicle.setNewSessionGetTasksListener(taskManagement);
        this.sessionManagementVehicle.setTaskTransmittedListener(taskManagement);
        this.sessionManagementVehicle.setNewSessionGetRequestsListener(requestManagement);
        this.sessionManagementVehicle.setRequestReceivedListener(requestManagement);
        this.sessionManagementVehicle.setRequestTransmittedListener(requestManagement);
        this.taskManagement.setNewTransmitTask(sessionManagementVehicle);
        this.requestManagement.setAddRequest(sessionManagementVehicle);


    }
    
    public void connectToSever() throws IOException{
        this.socket = new Socket("localhost",25001);
        sessionManagementVehicle.startSession(socket, VIN);

    }
    
    public void disconnectFromSever() throws IOException{
        this.socket.close();
        System.out.println("Closing connection");
    }
    
    public String getVIN(){
        return this.VIN;
    }
    
    public void addTask(String function, int iterations, int intervall){
        Task thisTask = taskManagement.startTaskTransmit(this.VIN);
        TaskWorker worker = new TaskWorker(function, iterations, intervall, vehicleSimulator, thisTask);
        tasks.add(worker);
        worker.start();
    }
    
    public ArrayList<TaskWorker> getTasks(){
        return tasks;
    }
    
    public boolean connectionStatus(){
        return true;
    }
    
}
