<?php
 include 'dbConnect.php';
 
 $regid = $_GET['regId'];
 $name = $_GET['name'];
 $username = $_GET['username'];
 $password = $_GET['password'];
 $usertype = $_GET['usertype'];
 
 
 if($name == '' || $username == '' || $password == '' || $usertype == ''){
 echo 'please fill all values';
 }else{
 $sql = "SELECT * FROM user WHERE userid='$username'";
 
 $check = mysqli_fetch_array(mysqli_query($con,$sql));
 
 if(isset($check)){
 echo 'username or email already exist';
 }else{ 
 $sql = "INSERT INTO user (tokenid,name,userid,password,usertype) VALUES('$regid','$name','$username','$password','$usertype')";
 if(mysqli_query($con,$sql)){
 echo 'success';
 }else{
 echo 'oops! Please try again!';
 }
 }
 mysqli_close($con);
 }
 ?>