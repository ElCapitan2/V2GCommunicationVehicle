/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.commonclasses.encryption.DESDecode;
import v2gcommunication.commonclasses.encryption.DESEncode;
import v2gcommunication.commonclasses.requests.Request;
import v2gcommunication.commonclasses.sessions.Session;
import v2gcommunication.commonclasses.tasks.Task;
import v2gcommunication.commonclasses.transmission.ConvertJSON;
import v2gcommunication.commonclasses.transmission.ReceiveProtocol;
import v2gcommunication.commonclasses.transmission.TransmitProtocol;
import v2gcommunication.commonclasses.requests.AddRequest;
import v2gcommunication.commonclasses.encryption.Decode;
import v2gcommunication.commonclasses.encryption.Encode;
import v2gcommunication.commonclasses.transmission.MessageBuilder;
import v2gcommunication.commonclasses.tasks.NewReceiveTask;
import v2gcommunication.commonclasses.tasks.NewSessionGetTasks;
import v2gcommunication.commonclasses.sessions.NewTransmitTask;
import v2gcommunication.commonclasses.transmission.ReadMessage;
import v2gcommunication.commonclasses.tasks.DataTransmitted;
import v2gcommunication.commonclasses.requests.NewSessionGetRequests;
import v2gcommunication.commonclasses.requests.RequestReceived;
import v2gcommunication.commonclasses.requests.RequestTransmitted;
import v2gcommunication.commonclasses.transmission.WriteMessage;

/**
 *
 * @author Alexander Forell
 */
public class SessionManagementVehicle implements NewTransmitTask, AddRequest{
    private final ExecutorService executor;
    private Session session;
    private final Encode enc; 
    private final Decode dec;
    private final MessageBuilder messageBuilder;
    private final ReadMessage readMessage;
    private final WriteMessage writeMessage;
    private NewReceiveTask newReceiveTask;
    private DataTransmitted taskTransmitted;
    private RequestTransmitted requestTransmitted;
    private RequestReceived requestReceived;
    private NewSessionGetTasks newSessionGetTasks;
    private NewSessionGetRequests newSessionGetRequests;
    
    SessionManagementVehicle(){
        this.executor = Executors.newCachedThreadPool();
        this.enc = new DESEncode();
        this.dec = new DESDecode();
        this.messageBuilder = new ConvertJSON();
        this.readMessage = new ReceiveProtocol();
        this.writeMessage = new TransmitProtocol();
    }
    
    public void startSession(Socket socket, String VIN){
        try {
            this.session = new Session(socket, executor, enc, dec, messageBuilder,
                    readMessage, writeMessage, newReceiveTask, taskTransmitted, 
                    requestTransmitted, requestReceived, newSessionGetTasks, 
                    newSessionGetRequests, VIN, null);
        } catch (IOException ex) {
            Logger.getLogger(SessionManagementVehicle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setNewReceiveTaskListener(NewReceiveTask newReceiveTask){
        this.newReceiveTask = newReceiveTask;
    }
    
    public void setTaskTransmittedListener(DataTransmitted taskTransmitted){
        this.taskTransmitted = taskTransmitted;
    }
    
    public void setNewSessionGetTasksListener(NewSessionGetTasks newSessionGetTasks){
        this.newSessionGetTasks = newSessionGetTasks;
    }
    
    public void setNewSessionGetRequestsListener(NewSessionGetRequests newSession){
        this.newSessionGetRequests = newSession;
    }
    public void setRequestTransmittedListener(RequestTransmitted requestTransmitted){
        this.requestTransmitted = requestTransmitted;
    }
    public void setRequestReceivedListener(RequestReceived requestReceived){
        this.requestReceived = requestReceived;
    }

    @Override
    public void addTransmitTask(Task ta) {
        if (session != null){
            session.addTransmitTask(ta);
        }
    }

    @Override
    public void addRequest(Request request) {
        if (session != null){
            session.addRequest(request);
        }
    }
    
    
    
    
    
    
}
