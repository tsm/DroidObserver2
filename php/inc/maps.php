<?php
  if($_GET['lat']&&$_GET['lon']){
?>      
        <script type="text/javascript">    
        function mapaStart()    
        {    
            var wspolrzedne = new google.maps.LatLng(<?php echo $_GET['lat']?>,<?php echo $_GET['lon']?>); 
            var opcjeMapy = { 
              zoom: 17, 
              center: wspolrzedne, 
              mapTypeId: google.maps.MapTypeId.HYBRID 
            }; 
            var mapa = new google.maps.Map(document.getElementById("mapka"), opcjeMapy);  
             
			 // stworzenie markera 
                var punkt  = new google.maps.LatLng(<?php echo $_GET['lat']?>,<?php echo $_GET['lon']?>); 
                var opcjeMarkera = 
                { 
                    position: punkt, 
                    map: mapa, 
                    title: "Po³o¿enie pacjenta"
                } 
                var marker = new google.maps.Marker(opcjeMarkera);
		}    
        </script>    
        <div id="mapka" style="width: 700px; height: 500px; border: 1px solid black; background: gray;">    
        <!-- tu bêdzie mapa -->   
        </div>  
<?php   
  }
?>