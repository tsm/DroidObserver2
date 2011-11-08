<?php 
    function checkLogin($login, $pass)
		{
		  connect2db();
		  $query="SELECT id FROM `patients` WHERE login='$login' AND password='$pass'";
		  $wynik=mysql_query($query);
		  if($dta=mysql_fetch_array($wynik)) 
		  {
		    $_SESSION['UserID']=$_POST['login'];
		    return true;
		  }
		  else return false;
		}
    
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
						   print "<p class=\"error\">Błąd! Login albo hasło niepoprawne!</p>\n";
						}
					}					
				}
		 }

    if($_SESSION['UserID']) // sprawdza czy uczeń jest zalogowany
      {
        print 'Jesteś zalogowana/y jako '.$_SESSION['UserID'].'(<a href="index.php?page=epatient&action=logout">wyloguj</a>)';
        connect2db();
        $wynik=mysql_query("SELECT latitude, longitude, date FROM `locations` WHERE patient_id='1' ORDER BY id DESC;");
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
