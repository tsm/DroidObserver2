<?php
include "db.php"; //znajduje si� tam funkcja do zalogowania si� do bazy danych

connect2db();
if($_POST['login']&&$_POST['pass']) //TODO: mo�na te� sprawdzac czy plik zostal wyslany
{
  connect2db();
  $login=$_POST['login'];
  $pass=$_POST['pass'];
  if (!checkLogin($_POST['login'],$_POST['pass']))
  {
	print "B��d! Login albo has�o niepoprawne! \n";
  }
  else
  {

	$target_path  = "./upload/"; //TODO doda� folder pacjenta!!!
	$file_name= basename( $_FILES['uploadedfile']['name']);
	$target_path = $target_path . $file_name;
	mysql_query("INSERT INTO `files` (login, filename) VALUES ($login,$file_name);");

	if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
	 echo "The file ".  basename( $_FILES['uploadedfile']['name']).
	 " has been uploaded";
	} else{
	 echo "There was an error uploading the file, please try again!";
	}
  }
}
?>