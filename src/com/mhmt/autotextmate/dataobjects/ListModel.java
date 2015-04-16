package com.mhmt.autotextmate.dataobjects;

public class ListModel {
    
    private String name="";
    private String description="";
    private String text="";
     
    public void setName(String ruleName)
    {
        this.name = ruleName;
    }
     
    public void setDescription(String ruleDescription)
    {
        this.description = ruleDescription;
    }
     

	public void setText(String ruleText)
    {
        this.text = ruleText;
    }
        
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getText() {
		return text;
	}
}