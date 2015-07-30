package com.github.autotest.sapient.demo.mailReport;

import java.util.List;

public class TestMethod {
	private String name;
	private String status;
	private String signature;
	private String reason="";
	private List<String> test_messages = null;
	//
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getSignature(){
		return signature;
	}
	public void setSignature(String signature){
		this.signature=signature;
	}
	public String getReason(){
		return reason;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public List<String> getTest_messages(){
		return test_messages;
	}
	public void setTest_messages(List test_messages){
		this.test_messages=test_messages;
	}
}
