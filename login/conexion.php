<?php 
	try {
		$conexion = new PDO('mysql:host=127.0.0.1;dbname=inyourhandf', 'root', '');					
	
		} catch (PDOException $e) {
				echo "Error: ". $e->getMessage();
		}
 ?>