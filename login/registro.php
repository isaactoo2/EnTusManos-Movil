<?php
	require 'conexion.php'; 


	$json=array();
	if (isset($_GET["name"]) && isset($_GET["lname"]) && isset($_GET["email"]) && isset($_GET["pwd"]) && isset($_GET["pwd2"]) && isset($_GET["address"]) && isset($_GET["gender"])) {
		$name=$_GET["name"];
		$lname=$_GET["lname"];
		$email=$_GET["email"];
		$pwd=$_GET["pwd"];
		$pwd2=$_GET["pwd2"];
		$address=$_GET["address"];
		$gender=$_GET["gender"];

		$pwd= password_hash($pwd, PASSWORD_DEFAULT);

		try {
			$statement = $conexion->prepare('select * from usuarios where usuario=?');
			$statement->execute(array($email));
			$datos=$statement->fetch(PDO::FETCH_ASSOC);
		} catch (PDOException $e) {
			
		}

		if ($datos!=true) {
			try {
				$statement=$conexion->prepare('insert into usuarios (usuario, password, idtipo, photo) values(?,?,?,?)');
				
				$statement->execute(array($email, $pwd, 'TU','0.png'));
				
				$statement = $conexion->prepare('select * from usuarios where usuario=?');
				$statement->execute(array($email));
				$id=$statement->fetch();
				//agregar datos
				$statement=$conexion->prepare('insert into detalle_usuarios (iduser, nombre, apellido, genero, ubicacion) values(?, ?, ?, ?, ?)');
				$statement->execute(array($id[0], $name, $lname, $gender, $address));
				

				$statement = $conexion->prepare('select usuarios.iduser, usuarios.usuario, detalle_usuarios.nombre, detalle_usuarios.apellido, detalle_usuarios.genero, detalle_usuarios.ubicacion, usuarios.photo from usuarios inner join detalle_usuarios on usuarios.iduser=detalle_usuarios.iduser and usuarios.usuario=?');
				$statement->execute(array($email));
				$datos=$statement->fetch(PDO::FETCH_ASSOC);
				if ($datos != false) {
					$json['datos'][]=$datos;
					echo json_encode($json);
				} 
			} catch (PDOException $e) {
				echo "Error " . $e->getMessage();
			}
		} else {
			
		}


		


	}else{

	}
 ?>