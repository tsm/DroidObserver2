<?php
include "db.php"; //znajduje siê tam funkcja do zalogowania siê do bazy danych

connect2db();
if($_FILES['uploadedfile'])//$_POST['login']&&$_POST['pass']) //TODO: mo¿na te¿ sprawdzac czy plik zostal wyslany
{
  connect2db();
  $login=$_POST['login'];
  $pass=$_POST['pass'];
  if (false)//!checkLogin($_POST['login'],$_POST['pass']))
  {
	print "B³¹d! Login albo has³o niepoprawne! \n";
  }
  else
  {

	$target_path  = "./upload/"; //TODO dodaæ folder pacjenta!!!
	$file_name= basename( $_FILES['uploadedfile']['name']);
	$target_path = $target_path . $file_name;
	mysql_query("INSERT INTO `files` (login, filename) VALUES ('$login','$file_name');");

	if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
	 echo "The file ".  basename( $_FILES['uploadedfile']['name']).
	 " has been uploaded";
	} else{
	 echo "There was an error uploading the file, please try again!";
	}
  }
}
else
{
?>
<html>
  <head>
    <title>.:Upload:.</title>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
    <meta http-equiv="Content-Style-Type" content="text/css" />
  </head>
  <body><h2>.:Upload:.</h2><div class="content">
  <form name ="file_form" enctype="multipart/form-data" action="sendfile.php" method="POST"><br />
  File: 
  <br /><input type="file" name="uploadedfile" value=""><br />
  Login:<br /><input type="text" name="login" value=""><br />
  Pass:<br /><input type="pass" name="pass" value=""><br />
  <input type="submit" name="wyslij" value="Send"></form></div></body>  
</html>
<?php
}
?>