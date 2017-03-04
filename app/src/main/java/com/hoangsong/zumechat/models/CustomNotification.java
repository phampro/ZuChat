package com.hoangsong.zumechat.models;

import java.io.Serializable;

public class CustomNotification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1729706563791032406L;
	private int id;
	private String bookingId;
	private String driverId;
	private String from;
	private String timestamp;
	private String type;
	/* Type
	 * 1: Driver found (got bids and assiged the job to nearest driver). Call /api/Customer/getDriverInfo API with the booking_id and driver_id to get driver information.
	 * -1: No one bid the job
	 * -3: Booking expired
	 * 
	 */
	private String message;
	private String sound;
	private String vibrate;
	private String collapseKey;
	
	public CustomNotification(){
		this.id=0;
		this.bookingId = "";
		this.driverId = "";
		this.from = "";
		this.timestamp = "";
		this.type = "";
		this.message = "";
		this.sound = "";
		this.vibrate = "";
		this.collapseKey = "";
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setBookingId(String bookingId){
		this.bookingId = bookingId;
	}
	
	public void setDriverId(String driverId){
		this.driverId = driverId;
	}
	
	public void setFrom(String from){
		this.from = from;
	}
	
	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void setSound(String sound){
		this.sound = sound;
	}
	
	public void setCollapseKey(String collapseKey){
		this.collapseKey = collapseKey;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getBookingId(){
		return this.bookingId;
	}
	
	public String getDriverId(){
		return this.driverId;
	}
	
	public String getFrom(){
		return this.from;
	}
	
	public String getTimestamp(){
		return this.timestamp;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getSound(){
		return this.sound;
	}

	public String getVibrate() {
		return vibrate;
	}

	public void setVibrate(String vibrate) {
		this.vibrate = vibrate;
	}

	public String getCollapseKey(){
		return this.collapseKey;
	}
	
	public String toString(){
		return "id: " + this.getId() + ", "
				+"bookingId: " + this.getBookingId() + ", "
				+"driverId: " + this.getDriverId() + ", "
				+"from: " + this.getFrom() + ", "
				+"timestamp: " + this.getTimestamp() + ", "
				+"type: " + this.getType() + ", "
				+"message: " + this.getMessage() + ", "
				+"sound: " + this.getSound() + ", "
				+"collapseKey: " + this.getCollapseKey()
				;
				
	}
}
