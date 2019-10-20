<?php 
	try {
		$conexion = new PDO('mysql:host=127.0.0.1;dbname=prueba', 'root', '');					
	
		} catch (PDOException $e) {
				echo "Error: ". $e->getMessage();
		}

	$json=array();
	if (isset($_GET["user"]) && isset($_GET["pwd"])) {
		$user=$_GET["user"];
		$pwd=$_GET["pwd"];

		$statement = $conexion->prepare('select * from usuarios where user= ? and pass=?');
			$statement->execute(array($user,$pwd));
			$resultados = $statement->fetch(PDO::FETCH_ASSOC);
			if ($resultados != false) {

				$json['datos'][]=$resultados;
				echo json_encode($json);
			} else{
				
		
			}
	} else {
		echo "string";
	}
 ?>