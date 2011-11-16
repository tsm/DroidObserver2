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
        print 'Jesteś zalogowana/y jako '.$_SESSION['UserID'].'(<a href="index.php?page=epatient&action=logout">wyloguj</a>)';
        connect2db();
		$user_id=$_SESSION['UserID'];
        print "\n<table>\n<caption align=\"center\">Informacje o pacjencie:</caption>\n<tr class=\"naglowek\"><th>Login/ID</th><th>Nazwisko</th><th>Imię</th><th>Telefon</th><th>e-mail</th><th>Choroba</th></tr>\n";
            $wynik=mysql_query("SELECT login, surname, name, mobile, email, disease, password, last_ip FROM `patients` WHERE login='$user_id' ORDER BY surname;");
						while($rekord=mysql_fetch_array($wynik))
						{
						  print "<tr><td>$rekord[0]</td><td>$rekord[1]</td><td>$rekord[2]</td><td>$rekord[3]</td><td>$rekord[4]</td><td>$rekord[5]</td></tr>";
						}
						print "</table>\n<br>\n";
      }
      else
      {
        print "Aby zobaczyć informacje o położeniu i wysłane zdjęcia należy się zalogować: <br>\n";
        include "login_form.php";
      }
?>
