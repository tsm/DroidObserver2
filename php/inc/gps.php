<?php 
    if($_REQUEST['action'])
    {
      $akcja=$_REQUEST['action'];    
      if($akcja=='logout')
      {
        if($_SESSION['UserID'])
				{
				  unset($_SESSION['UserID']);
				  print "Wylogowano! <br>";
				}
				else
				{
				  print "<p class=\"error\">Błąd! Nie jesteś zalogowany!</p>\n";
				}
   			 session_destroy();
      }
      elseif($akcja=='login')
      {
        if($_POST['login']&&$_POST['haslo'])
					{					    
						if (!checkLogin($_POST['login'],$_POST['haslo']))
						{
						   print "<p class=\"error\">Błąd! Login albo hasło niepoprawne! </p>\n";
						}
					}					
				}
		 }

    if($_SESSION['UserID']) // sprawdza czy pacjent jest zalogowany
      {
        print 'Jesteś zalogowana/y jako '.$_SESSION['UserID'].'(<a href="index.php?page=gps&action=logout">wyloguj</a>)';
        connect2db();
		$user_id=$_SESSION['UserID'];
        $wynik=mysql_query("SELECT latitude, longitude, date FROM `locations` WHERE login='$user_id' ORDER BY id DESC;");
								while($record=mysql_fetch_array($wynik))
								{								  
								  print "<p>$record[2], szerokość: $record[0]  długość: $record[1]</p>\n";								  
								}
      }
      else
      {
        print "Aby zobaczyć informacje o położeniu i wysłane zdjęcia należy się zalogować: <br>\n";
        include "login_form.php";
      }
?>
