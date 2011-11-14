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
        $wynik=mysql_query("SELECT login, filename, date FROM `files` WHERE login='$user_id' ORDER BY id;");
		print "<table>\n";
		while($rekord=mysql_fetch_array($wynik))
		{
		  print "<tr><td>$rekord[0]</td><td>$rekord[2]</td><td><img src=\"upload/$rekord[1]\" width=\"640\"></td></tr>";
		}
		print "</table>\n";
	
      }
      else
      {
        print "Aby zobaczyć informacje o położeniu i wysłane zdjęcia należy się zalogować: <br>\n";
        include "login_form.php";
      }          

?>