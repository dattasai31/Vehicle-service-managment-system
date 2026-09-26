package com.vehicleservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.*;

public class Vehicle_service_center_managment {
	static final String url="jdbc:mysql://localhost:3306/jdbc";
	static final String uname="root";
	static final String pwd="Dattasai@78";
	static Connection con=null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(url,uname,pwd);
			con.setAutoCommit(false);
			createtablecustomers(con);// creating customers table
			createtablevehicles(con);// creating vehicle table
			createtableservicerecords(con);// creating service records table
			Scanner sc=new Scanner(System.in);
			int choice;
			do
			{
				System.out.println("1.Customer Registration");
				System.out.println("2.Vehicle Registration");
				System.out.println("3.Book service");
				System.out.println("4.Update service status");
				System.out.println("5.Show pending vehicles");
				System.out.println("6.Show service history");
				System.out.println("0.Exit");
				System.out.println("Please enter your choice:");
				choice=sc.nextInt();
				sc.nextLine();
				switch(choice)
				{
					case 1:
						crud_operations_vehicles.customerregistration(con, sc);
						break;
					case 2:
						crud_operations_vehicles.vehicleregistration(con, sc);
						break;
					case 3:
						crud_operations_vehicles.servicebooking(con, sc);
						break;
					case 4:
						crud_operations_vehicles.updateservicestatus(con, sc);
						break;
					case 5:
						crud_operations_vehicles.pendingvehicles(con, sc);
						break;
					case 6:
						crud_operations_vehicles.servicehistory(con, sc);
						break;
					case 0:
						System.out.println("Exiting.......");
						break;
					default:
							System.out.println("Please select valid option");
							break;
				}
			}while(choice!=0);	
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(con!=null)
				{
					con.close();
				}
			}
			catch(SQLException ex)
			{
				System.out.println(ex.getMessage());
			}
		}

	}
	
	
	// method for creating customers table
	static void createtablecustomers(Connection con) throws SQLException
	{
		String query="""
				create table if not exists customers(
				customer_id int primary key auto_increment,
				name varchar(100) not null,
				mobile_number varchar(15) not null,
				email varchar(50),
				address varchar(200))""";
		try(Statement st=con.createStatement())
		{
			st.execute(query);
			System.out.println("Table created successfully for customers");
		}		
	}
	
	
	// method for creating vehicles table
	static void createtablevehicles(Connection con) throws SQLException
	{
		String query="""
				create table if not exists vehicles(
				vehicle_id int primary key auto_increment,
				customer_id int not null,
				reg_number varchar(20) unique not null,
				brand varchar(50),
				model varchar(50),
				year_of_manufacture int,
				foreign key (customer_id) references customers(customer_id))"""; 
		try(Statement st=con.createStatement())
		{
			st.execute(query);
			System.out.println("Table for vehicles created successfully");
		}			
	}
	
	
	// method for creating service records table
	static void createtableservicerecords(Connection con) throws SQLException
	{
		String query="""
				create table if not exists servicerecords(
				service_id int primary key auto_increment,
				vehicle_id int not null,
				service_date date not null,
				service_type varchar(50),
				cost decimal(10,2),
				status varchar(20) default 'Booked',
				foreign key (vehicle_id) references vehicles(vehicle_id))""";
		try(Statement st=con.createStatement())
		{
			st.execute(query);
			System.out.println("Table for service records created successfully");
		}
				
	}
	

	


}
