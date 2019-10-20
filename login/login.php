<?php
	require 'conexion.php'; 


	$json=array();
	if (isset($_GET["email"]) && isset($_GET["pwd"])) {
		
		$email=$_GET["email"];
		$pwd=$_GET["pwd"];


		try {
				$statement = $conexion->prepare('select usuarios.iduser, usuarios.usuario, usuarios.password, detalle_usuarios.nombre, detalle_usuarios.apellido, detalle_usuarios.genero, detalle_usuarios.ubicacion, usuarios.photo from usuarios inner join detalle_usuarios on usuarios.iduser=detalle_usuarios.iduser and usuarios.usuario=?');
				$statement->execute(array($email));
				$datos=$statement->fetch(PDO::FETCH_ASSOC);	
						
				if (password_verify($pwd, $datos["password"])) {
					$json['datos'][]=$datos;
					echo json_encode($json);
				} 

			} catch (PDOException $e) {
				echo "Error " . $e->getMessage();
			}

	}
 ?>