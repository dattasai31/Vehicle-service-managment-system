package com.vehicleservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class validations_vehicles {
	
	
	// validation method to check whether the entered string is empty or not
	static boolean isempty(String s)
	{
	    if(s == null || s.isEmpty())
	    {
	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	
	// validation method to check whether the mobile number contains 10 digits or not
	static boolean validmobilenumber(String s)
	{
		if(s==null || s.isEmpty())
		{
			return false;
		}
		return s.matches("\\d{10}");
	}
	
	
	// validation method to check whether the entered regsitration number is unique or not
	static boolean uniqueregnumber(Connection con,String s) throws SQLException
	{
		String query="select * from vehicles where reg_number=?";
		try(PreparedStatement pst=con.prepareStatement(query))
		{
			pst.setString(1,s);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}
	
	
	// validation method to check customer is registered or not
	static boolean checkcustomer(Connection conn,int cid) throws SQLException
	{
		String query="select * from customers where customer_id=?";
		try(PreparedStatement pst=conn.prepareStatement(query))
		{
			pst.setInt(1,cid);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}
	
	
	// validation method to check vehicle is registered or not
	static boolean checkvehicle(Connection conn,int vid) throws SQLException
	{
		String query="select * from vehicles where vehicle_id=?";
		try(PreparedStatement pst=conn.prepareStatement(query))
		{
			pst.setInt(1,vid);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}

	
	// validation method to check service
	static boolean checkservice(Connection conn,int sid) throws SQLException
	{
		String query="select * from servicerecords where service_id=?";
		try(PreparedStatement pst=conn.prepareStatement(query))
		{
			pst.setInt(1,sid);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}
	
	
	// validation method to check date
	static boolean validatedate(LocalDate date)
	{
		LocalDate today=LocalDate.now();
		if(date.isBefore(today))
		{
			System.out.println("You cannot book service for previous dates ");
			System.out.println("---------------------------------------------");
			return false;
		}
		return true;
	}
	
	
	// validation method to check email
	static boolean validateemail(String s)
	{
		if(s!=null && !(s.isEmpty()))
		{
			if(s.contains("@") && s.contains("."))
			{
				return true;
			}
		}
		return false;
	}
	
	
	// validation method to check the vehicles that are yet to be serviced
	static boolean haspendingservices(Connection con,int vehicleid) throws SQLException
	{
		String query="select * from servicerecords where vehicle_id=? and status='booked'";
		try(PreparedStatement pst=con.prepareStatement(query))
		{
			pst.setInt(1, vehicleid);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}
	
	
	// validation method to check whether the vehicle service is completed or not
	static boolean updatingservice(Connection con,int serviceid) throws SQLException
	{
		String query="select * from servicerecords where service_id=? and status='completed'";
		try(PreparedStatement pst=con.prepareStatement(query))
		{
			pst.setInt(1, serviceid);
			ResultSet rst=pst.executeQuery();
			return rst.next();
		}
	}
}
