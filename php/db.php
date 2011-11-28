<?php

  include "config.php";

  function connect2db() //potrzebne do zalogowania do bazy danych
  {
    mysql_connect($baza, $login, $haslo) or die ("Nie można się połączyć z MySQL!");
    mysql_select_db($database_name) or die("Nie można się połączyć z bazą danych!");
  }
  
  function checkLogin($login, $pass)
		{
		  connect2db();
		  $query="SELECT login FROM `patients` WHERE login='$login' AND password='$pass'";		  
		  $wynik=mysql_query($query);
		  if($dta=mysql_fetch_array($wynik)) 
		  {
		    $_SESSION['UserID']=$login;
		    return true;
		  }
		  else return false;
		}
?>
