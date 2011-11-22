<?php 
 $trace=$_GET['trace'];
   
if($_SESSION['UserID']=='administrator') // sprawdza czy admin jest zalogowany
    {
      if($trace){
	    connect2db();
		$user_id=$_GET['user'];
        $wynik=mysql_query("SELECT latitude, longitude, date FROM `locations` WHERE login='$user_id' AND trace='$trace' ORDER BY id DESC;");
		if ($record=mysql_fetch_array($wynik)){						
?>      
        <script type="text/javascript">    
        var mapa;
		var rozmiar = new google.maps.Size(32,32); 
                var rozmiar_cien = new google.maps.Size(59,32); 
                var punkt_startowy = new google.maps.Point(0,0); 
                var punkt_zaczepienia = new google.maps.Point(16,16); 
				
				var greenIcon = google.maps.MarkerImage("http://www.google.com/intl/en_us/mapfiles/ms/micons/green-dot.png",rozmiar, punkt_startowy, punkt_zaczepienia);
		
		function dodajMarker(lat,lon,opcjeMarkera) 
            { 
                // tworzymy marker z współrzędnymi i opcjami z argumentów funkcji dodajMarker 
                opcjeMarkera.position = new google.maps.LatLng(lat,lon); 
                  
                opcjeMarkera.map = mapa; // obiekt mapa jest obiektem globalnym! 
                var marker = new google.maps.Marker(opcjeMarkera); 
            } 
		
		function mapaStart()    
        {   
            var opcjeMapy =  
                { 
                    center:  new google.maps.LatLng(<?php echo $record[0]?>,<?php echo $record[1]?>), 
                    zoom: 17, 
                    mapTypeId: google.maps.MapTypeId.HYBRID 
                }; 
                mapa = new google.maps.Map(document.getElementById("mapka"), opcjeMapy);
             
			
			    
		
			 <?php
			   
			     print "dodajMarker(".$record[0].",".$record[1].",{title: 'Ostatnia pozycja pacjenta z ".$record[2]."', icon: greenIcon}); ";
		}
			   $i=1;
			   while($record=mysql_fetch_array($wynik))
				  {			
					$i++;
					print "dodajMarker(".$record[0].",".$record[1].",{title: '".$record[2]."'}); ";
			    }
			?>
		}    
        </script>    
        <div id="mapka" style="width: 700px; height: 500px; border: 1px solid black; background: gray;">    
        <!-- tu będzie mapa -->   
        </div>  
<?php   
      }
   }
   else
   {
     print "Aby zobaczyć informacje o położeniu i wysłane zdjęcia należy się zalogować: <br>\n";
     include "login_form.php";
   }
?>