package com.example.shohojogi.historyRecyclerView;

public class HistoryObject {

    private  String time,historyId,nameHistory,jobHistory,paymentHistory,ClientOrWorker;

    public  HistoryObject(String time,String historyId,String nameHistory,String jobHistory,String paymentHistory,String ClientOrWorker){
        this.time = time;
        this.historyId = historyId;
        this.nameHistory = nameHistory;
        this.jobHistory = jobHistory;
        this.paymentHistory = paymentHistory;
        this.ClientOrWorker= ClientOrWorker;
    }

    public String getTime(){return time;}
    public void  setTime(String time){this.time = time;}

    public String getHistoryId(){return historyId;}
    public void  setHistoryId(String historyId){this.historyId = historyId;}

    public String getNameHistory(){return nameHistory;}
    public void  setNameHistory(String nameHistory){this.nameHistory = nameHistory;}

    public String getJobHistory(){return jobHistory;}
    public void  setJobHistory(String jobHistory){this.jobHistory = jobHistory;}

    public String getPaymentHistory(){return paymentHistory;}
    public void  setPaymentHistory(String paymentHistory){this.paymentHistory = paymentHistory;}

    public String getClientOrWorker(){return ClientOrWorker;}
    public void  setClientOrWorker(String ClientOrWorker){this.ClientOrWorker = ClientOrWorker;}
}
