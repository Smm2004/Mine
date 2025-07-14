package DTO;

public class Email_DTO {
    private String To;
    private String Message;
    private String Title;
    private String CC;



    public Email_DTO(){};

    public Email_DTO(String to, String message, String title, String cc){
        this.To = to;
        this.Message = message;
        this.Title = title;
        this.CC = cc;

    }


    public void setTo(String to){
        this.To = to;
    }
    public String getTo(){
        return To;
    }

    public void setMessage(String message){
        this.Message = message;
    }
    public String getMessage(){
        return Message;
    }

    public void setTitle(String title){
        this.Title = title;
    }
    public String getTitle(){
        return Title;
    }

    public void setCC(String cc){
        this.CC = cc;
    }
    public String getCC(String cc){
        return CC;
    }
}