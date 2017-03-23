
<?php
    include 'dbConnect.php';
 
    if($_SERVER['REQUEST_METHOD']=='POST'){
		$id = $_POST['id'];
        $name = $_POST['name'];
        $class = $_POST['class'];
		$bus = $_POST['busid'];		
		$stop = $_POST['stopid'];
		$sql="INSERT INTO student(id,name,class,stopid,busno) VALUES ('$id','$name','$class','$stop','$bus')";
      if(mysqli_query($con,$sql)){
	 echo 'success';
	 }else{
	 echo 'failed';
	}
    }else{
        echo 'error';
    }
?>