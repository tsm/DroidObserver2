<?php
  session_start(); //sesja pamięta logowanie
  header('Content-type: text/html; charset=utf-8');  
  include "db.php"; //znajduje się tam funkcja do zalogowania się do bazy danych
  
  
  if(isset($_GET['page']))
  {
   $page=$_GET['page'];
  }
  else
  {
   $page="news";
  }
  
  $login_info="";
  
  if($_REQUEST['action'])
    {
      $akcja=$_REQUEST['action'];    
      if($akcja=='logout')
      {
        if($_SESSION['UserID'])
				{
				  unset($_SESSION['UserID']);
				  $login_info="Wylogowano! <br>";
				}
				else
				{
				  $login_info="<p class=\"error\">Błąd! Nie jesteś zalogowany!</p>\n";
				}
   			 session_destroy();
      }
      elseif($akcja=='login')
      {
        if($_POST['login']&&$_POST['haslo'])
					{
						if (($_POST['login']=='administrator') && ($_POST['haslo']=='xsw21qaz'))
						{
						   $_SESSION['UserID']=$_POST['login'];
						}
					}					
				}
		 }
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="Description" content="DroidObserver page">
    <meta name="Keywords" content="DroidObserver, android, gps">
    <meta http-equiv="Content-Language" content="pl">
    <meta name="Author" content="BullTeam">
    <meta http-equiv="Reply-To" content="tomszom@op.pl">
    <meta name="Robots" content="none">
    <link rel="Stylesheet" href="style.css" type="text/css">

    <title>DroidObserver</title>
  <?php if($_GET['gps']=="enable") {echo '	<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>'."\n</head>\n    <body onload=\"mapaStart()\">\n" ;}
  else echo'</head>
  <body>';
  ?>
  <div class="strona"> 
         
      <div class="logo">
        <h1>DroidObserver</h1>
      </div>
      
      
      <div class="menu">
      <?php        
        include "admin/menu.php";
      ?>
      </div>
      
      
      <div class="tresc"> 
       <br>      
<?php
	print $login_info;
    if($_SESSION['UserID']=='administrator') // sprawdza czy jestes adminem
      {
        print 'Jesteś zalogowana/y jako '.$_SESSION['UserID'].'(<a href="admin.php?page=news&action=logout">wyloguj</a>)';
        if(isset($page))
          {
            include "admin/".$page.".php";
          }
      }
      else
      {
        print "Każda próba nieudanego logowania będzie rejestrowana: <br>\n"; // pic na wodę, chodzi o to żeby odstraszyć wścibskich pacjentów
        include "admin/login_form.php";
      }         
       ?>
      </div>
      
      
      <div class="stopka"> 
       Copyright&copy; 2011 by BullTeam
      </div>     
      
    </div>
  </body>
</html>
