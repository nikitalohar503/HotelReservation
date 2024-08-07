package com.beginsjava;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username="root";
    private static final String password="Admin@123";

    public static void main(String[] args) throws ClassNotFoundException{
	     try {
               Class.forName("com.mysql.cj.jdbc.Driver");

           }catch (ClassNotFoundException e){
          System.out.println(e.getMessage());
          }

          try {
              Connection connection= DriverManager.getConnection(url,username,password);
              while (true){
                  System.out.println();
                  System.out.println("Hotel Management System");
                  Scanner sc=new Scanner(System.in);
                  System.out.println("1. Reserve a Room");
                  System.out.println("2. View Reservation");
                  System.out.println("3. Get Room Number");
                  System.out.println("4. Update Reservation");
                  System.out.println("5. Delete Reservation");
                  System.out.println("0. Exit");
                  System.out.println("choose an option: ");
                  int choice=sc.nextInt();
                  switch (choice){
                      case 1:
                          reserveRoom(connection,sc);
                          break;
                      case 2:
                          viewReservation(connection);
                          break;
                      case 3:
                          getRoomNumber(connection,sc);
                          break;

                      case 5:
                          deleteReservation(connection,sc);
                          break;
                      case 0:
                          exit();
                          sc.close();
                          return;
                      default:
                          System.out.println("Invalid choice. Try again");
                  }
              }

         } catch (SQLException e){
              System.out.println(e.getMessage());
         }catch (InterruptedException e){
              throw new RuntimeException(e);
          }

    }

    private static void reserveRoom(Connection connection,Scanner scanner){
        try{
            System.out.println("Enter guest name:");
            String guestName=scanner.next();
            scanner.nextLine();
            System.out.println("Enter Room Number");
            int roomNumber=scanner.nextInt();
            System.out.println("Enter Contact Nnumber");
            String contactNumber=scanner.next();
            String sql="INSERT INTO reservations (guest_name,room_number,contact_number)" +
                    "VALUES (' " + guestName + " ', " + roomNumber + ", ' "+ contactNumber + " ')";

              try (Statement statement =connection.createStatement()){
                  int affectedRows=statement.executeUpdate(sql);

                  if(affectedRows > 0){
                      System.out.println("Reservation successfull ");
                  }else{
                      System.out.println("Reservation failed");
                  }
              }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void viewReservation(Connection connection) throws SQLException {
        String sql="SELECT reservation_id, guest_name,room_number,contact number,reservation_date FROM reservations;";

        try (Statement statement=connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){

            System.out.println("current Reservations: ");
            System.out.println("+------------------------------------------------------------------");
            System.out.println("| Reservation Id | Guest | Room Number | Contact Number | Reservation Date");
            System.out.println("------------------------------------------------------------------------");

            while (resultSet.next()){
                int reservation_id=resultSet.getInt("reservation_id");
                String guestName=resultSet.getString("guest_name");
                int roomNumber =resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact number");
                String reservationDate=resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19 |\n",
                        reservation_id,guestName,roomNumber,contactNumber,reservationDate
                );
                System.out.println("--------------------------------------------------------------------------");

            }
        }


    }
    private static void getRoomNumber(Connection connection,Scanner scanner){
      try {
          System.out.println("Enter Reservation Id: ");
          int reservationId=scanner.nextInt();
          System.out.println("Enter Guest Name: ");
          String guestName=scanner.next();

          String sql ="select room_number From reservations "+
                  "where reservation_id= "+ reservationId +
                  "And guest_name= ' " + guestName + " ' ";

          try (Statement statement=connection.createStatement();
          ResultSet resultSet=statement.executeQuery(sql)){
              if(resultSet.next()){
                  int roomNumber = resultSet.getInt("room_number");
                  System.out.println("Room number for Reservation Id "+ reservationId +
                          "and Guest" + guestName + "is: " +roomNumber);
              }else{
                  System.out.println("Reservation not found for given id");
              }
          }
      }catch (SQLException e){
          e.printStackTrace();
      }
    }




  private static void deleteReservation(Connection connection,Scanner scanner){
        try {
            System.out.println("Enter reservation Id to delete: ");
            int reservationId=scanner.nextInt();

            if (!reservationExist ( connection,reservationId)){
                System.out.println("Reservation not found for given id");
                return;
            }

            String sql="Delete from Reservations where reservationId = "+ reservationId;

            try (Statement statement=connection.createStatement()){
                int affectedRows =statement.executeUpdate(sql);
                if (affectedRows > 0){
                    System.out.println("reservation deleted suceesfully");
                }else{
                    System.out.println("reservation deletion failed");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
  }


  private  static boolean reservationExist(Connection connection,int reservationId){
        try{
            String sql="SELECT reservationId from reservations where reservationId= "+reservationId;
            try(Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql)){
                return  resultSet.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
  }

public static void exit() throws InterruptedException{
    System.out.println("Existing System");
    int i=5;

    while (i != 0){
        System.out.println(".");
        Thread.sleep(450);
        i--;
    }
    System.out.println();
    System.out.println("thank you for using hotel reservation system");
}





































}
