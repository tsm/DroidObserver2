<?php
include "db.php"; //znajduje siê tam funkcja do zalogowania siê do bazy danych

if($_POST['longitude']&&$_POST['latitude']&&$_POST['login']&&$_POST['pass'])
{
  connect2db();
  $long=$_POST['longitude'];
  $lati=$_POST['latitude'];
  $login=$_POST['login'];
  $pass=$_POST['pass'];
  if (!checkLogin($_POST['login'],$_POST['pass']))
  {
	print "B³¹d! Login albo has³o niepoprawne! \n";
  }
  else
  {
    if(mysql_query("INSERT INTO `locations` (login, longitude, latitude) VALUES ('$login',$long,$lati);"))
	{
      echo "ok";
	}
	else
	{
	  print "Blad! Nie udalo sie dodac lokalizacji GPS! \n";
	}
  }
}
else
{ 
  print_r($_POST);
?>
  <form action="sendgps.php" method="POST">
  <table>
    <tr><td>login:</td><td><input class="tekst" type="text" name="login"></td></tr>
	<tr><td>password:</td><td><input class="tekst" type="password" name="pass"></td></tr>
	<tr><td>Longnitude:</td><td><input class="tekst" type="text" name="longitude"></td></tr>
	<tr><td>Latitude:</td><td><input class="tekst" type="text" name="latitude"></td></tr>
    <tr><td colspan="2"><input type="submit" value="Wyslij"</td></tr>
  </table>
</form>

<?php
}
?>