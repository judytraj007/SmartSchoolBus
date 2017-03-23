<?php
   	include 'dbConnect.php';

 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 //Getting values 

 
 $lat = $_POST['lat1'];
 $lng = $_POST['lng1'];
 $speed = $_POST['speed'];
 $id = $_POST['busid']; 
 
 //Creating an sql query
 $sql = "INSERT INTO status (busid,latitude,longitude,speed) VALUES ('$id','$lat','$lng','$speed')";
 

 
 //Executing query to database
 mysqli_query($con,$sql);
 
 
 //Closing the database 
 mysqli_close($con);
 }
 ?>