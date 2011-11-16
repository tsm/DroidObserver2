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
   $page="news"; // domyslna podstrona
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
        
        include "inc/menu.php";
      ?>
      </div>
      
      
      <div class="tresc"> 
       <br>      
       <?php
         if(isset($page))
          {
            include "inc/".$page.".php";
          }
       ?>
      </div>
      
      
      <div class="stopka"> 
       Copyright&copy; 2011 by BullTeam
      </div>     
      
    </div>
  </body>
</html>
