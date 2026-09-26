package com.vehicleservice;
import java.util.*;
import java.sql.*;
import java.time.LocalDate;
public class crud_operations_vehicles {
	
	
	// method for adding customer
	static void customerregistration(Connection con,Scanner sc)
	{
		try
		{
			System.out.println("Enter customer name:");
			String name=sc.nextLine();
			if(validations_vehicles.isempty(name))
			{
				System.out.println("Please enter customer name");
				System.out.println("-----------------------------");
				return;
			}
			System.out.println("Enter mobile number:");
			String number=sc.nextLine();
			if(!(validations_vehicles.validmobilenumber(number)))
			{
				System.out.println("Please enter valid 10 digit mobile number");
				System.out.println("--------------------------------------------");
				return;
			}
			System.out.println("Enter email id:");
			String email=sc.nextLine();
			if(!(validations_vehicles.validateemail(email)))
			{
				System.out.println("Enter valid email id");
				System.out.println("---------------------------");
				return;
			}
			System.out.println("Enter address:");
			String adr=sc.nextLine();
			String query="insert into customers(name,mobile_number,email,address) values(?,?,?,?)";
			try(PreparedStatement pst=con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS))
			{
				pst.setString(1, name);
				pst.setString(2, number);
				pst.setString(3, email);
				pst.setString(4, adr);
				int rows=pst.executeUpdate();
				if(rows>0)
				{
					ResultSet rst=pst.getGeneratedKeys();
					if(rst.next())
					{
						int customerid=rst.getInt(1);
						System.out.println("Customer details added successfully. Customer id is: "+customerid);
					}
					System.out.println("------------------------------------");
					con.commit();
				}
				
			}
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
				System.out.println("Transactions are rollback");
				System.out.println("-------------------------------");
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
			System.out.println(e.getMessage());
		}
		
	}
	
	
	// method for adding vehicle registration number
	static void vehicleregistration(Connection con,Scanner sc)
	{
		try
		{
			System.out.println("Enter customer ID:");
			int id=sc.nextInt();
			sc.nextLine();
			if(!(validations_vehicles.checkcustomer(con, id)))
			{
				System.out.println("No customer registered with this id");
				System.out.println("------------------------------------");
				return;
			}
			System.out.println("Enter registration number:");
			String rnum=sc.nextLine();
			if(validations_vehicles.uniqueregnumber(con, rnum))
			{
				System.out.println("This vehicle is already registred");
				System.out.println("-----------------------------------------------------");
				return;
			}
			System.out.println("Enter brand name:");
			String brand=sc.nextLine();
			System.out.println("Enter car model:");
			String cmodel=sc.nextLine();
			System.out.println("Enter year of manufacture:");
			int yom=sc.nextInt();
			sc.nextLine();
			String query="insert into vehicles(customer_id,reg_number,brand,model,year_of_manufacture) values(?,?,?,?,?)";
			try(PreparedStatement pst=con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS))
			{
				pst.setInt(1, id);
				pst.setString(2, rnum);
				pst.setString(3, brand);
				pst.setString(4, cmodel);
				pst.setInt(5, yom);
				int rows=pst.executeUpdate();
				if(rows>0)
				{
					ResultSet rst=pst.getGeneratedKeys();
					if(rst.next())
					{
						int vehicleid=rst.getInt(1);
						System.out.println("Vehicle details added successfully. Vehicle id is: "+vehicleid);
					}
					System.out.println("-------------------------------------");
					con.commit();
				}
			}
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
				System.out.println("Transactions are rollback");
				System.out.println("---------------------------------------");
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
			System.out.println(e.getMessage());
		}
	}
	
	
	// method for booking service
	static void servicebooking(Connection con,Scanner sc)
	{
		try
		{
			System.out.println("Enter vehicle ID:");
			int id=sc.nextInt();
			if(!(validations_vehicles.checkvehicle(con, id)))
			{
				System.out.println("No vehicle registered with this id");
				System.out.println("-----------------------------------------");
				return;
			}
			sc.nextLine();
			if(validations_vehicles.haspendingservices(con, id))
			{
				System.out.println("Service already booked for this vehicle. Please book after the service is completed");
				System.out.println("------------------------------------");
				return;
			}
			System.out.println("Enter service date (YYYY-MM-DD)");
			String sdate=sc.nextLine();
			LocalDate serdate=LocalDate.parse(sdate);
			if(!(validations_vehicles.validatedate(serdate)))
			{
				return;
			}
			System.out.println("Enter service type:");
			String stype=sc.nextLine();
			if(validations_vehicles.isempty(stype))
			{
				System.out.println("Service type field cannot be empty");
				System.out.println("--------------------------------------");
				return;
			}
			System.out.println("Enter cost:");
			double cost=sc.nextDouble();
			if(cost<0)
			{
				System.out.println("Cost cannot be negative");
				System.out.println("--------------------------------------");
				return;
			}
			sc.nextLine();
			String query="insert into servicerecords(vehicle_id,service_date,service_type,cost) values(?,?,?,?)";
			try(PreparedStatement pst=con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS))
			{
				pst.setInt(1, id);
				pst.setDate(2, java.sql.Date.valueOf(serdate));
				pst.setString(3, stype);
				pst.setDouble(4, cost);
				int rows=pst.executeUpdate();
				if(rows>0)
				{
					ResultSet rst=pst.getGeneratedKeys();
					if(rst.next())
					{
						int serviceid=rst.getInt(1);
						System.out.println("Vehicle service booked successfully. Service id is:"+serviceid);
					}
					con.commit();
					System.out.println("---------------------------------------");
				}
			}
			
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
				System.out.println("Transactions rollback");
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
			System.out.println(e.getMessage());
		}
		catch(java.time.format.DateTimeParseException e)
		{
		    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
		    System.out.println("-----------------------------------------------");
		}
	}
	
	
	// method for updating service status
	static void updateservicestatus(Connection con,Scanner sc)
	{
		try
		{
			
			String query="select * from servicerecords where status='booked'";
			try(Statement st=con.createStatement())
			{
				ResultSet rst=st.executeQuery(query);
				boolean found=false;
				int count=0;
				while(rst.next())
				{
					found=true;
					count++;
					if(count==1)
					{
						System.out.println("Vehicles that are yet to service");
					}
					System.out.println("Service ID:"+rst.getInt(1)+" |Vehicle ID:"+rst.getInt(2)+" |Service type:"+rst.getString(4));
				}
				if(!found)
				{
					System.out.println("No pending services found");
					System.out.println("------------------------------------");
					return;
				}
			}
			System.out.println("Enter service ID:");
			int id=sc.nextInt();
			if(!(validations_vehicles.checkservice(con, id)))
			{
				System.out.println("Please enter valid service ID");
				System.out.println("--------------------------------------");
				return;
			}
			if(validations_vehicles.updatingservice(con, id))
			{
				System.out.println("Please enter service id of vehicle that are yet to be serviced");
				System.out.println("--------------------------------------------");
				return;
			}
			
			sc.nextLine();
			String query1="update servicerecords set status='completed' where service_id=?";
			
			try(PreparedStatement pst=con.prepareStatement(query1))
			{
				pst.setInt(1, id);
				int rows=pst.executeUpdate();
				if(rows>0)
				{
					System.out.println("Vehicle status updated successfully");
					con.commit();
					System.out.println("---------------------------------------");
				}
			}
		}
		catch(SQLException e)
		{
			try
			{
				con.rollback();
				System.out.println("Transactions rolback");
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
			System.out.println(e.getMessage());
		}
	}
	
	
	// method to view pending vehicles
	static void pendingvehicles(Connection con,Scanner sc)
	{
		try
		{
			String query="select * from servicerecords where status='booked'";
			try(Statement st=con.createStatement())
			{
				ResultSet rst=st.executeQuery(query);
				boolean found=false;
				while(rst.next())
				{
					found=true;
					System.out.println("Service ID:"+rst.getInt(1)+" |Vehicle ID:"+rst.getInt(2)+" |Service type:"+rst.getString(4));
				}
				if(!found)
				{
					System.out.println("No pending services found");
				}
				System.out.println("----------------------------------------");
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	
	// method to view service history of a vahicle
	static void servicehistory(Connection con,Scanner sc)
	{
		try
		{
			System.out.println("Enter vehicle ID:");
			int id=sc.nextInt();
			if(!(validations_vehicles.checkvehicle(con, id)))
			{
				System.out.println("No vehicle registered with this id");
				System.out.println("----------------------------------------");
				return;
			}
			sc.nextLine();
			String query="""
					select c.name,c.mobile_number,v.reg_number,v.brand,v.model,s.service_id,s.service_date,s.service_type,s.cost,s.status
					from servicerecords s
					join vehicles v on s.vehicle_id=v.vehicle_id
					join customers c on v.customer_id=c.customer_id
					where v.vehicle_id=?
					order by s.service_date desc""";
			try(PreparedStatement pst=con.prepareStatement(query))
			{
				pst.setInt(1, id);
				ResultSet rst=pst.executeQuery();
				boolean found=false;
				while(rst.next()) 
				{
					found=true;
					System.out.println("Customer name:"+rst.getString("name")+" |Customer mobile number:"+rst.getString("mobile_number")+"| Vehicle registration number:"+rst.getString("reg_number")+"| Brand:"+rst.getString("brand")+"| Model:"+rst.getString("model")+"| Service ID:"+rst.getInt("service_id")+"| Service date:"+rst.getDate("service_date")+"| Service type:"+rst.getString("service_type")+"| Cost:"+rst.getDouble("cost")+"| Status:"+rst.getString("status"));
				
				}
				if(!found)
				{
					System.out.println("No service history found for this vehicle");
				}
				System.out.println("---------------------------------------------------");
			}
					
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

}
