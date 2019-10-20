<?PHP
$hostname="localhost";
$database="prueba";
$username="root";
$password="";
$json=array();
	if(isset($_GET["user"]) && isset($_GET["pwd"])){
		$user=$_GET['user'];
		$pwd=$_GET['pwd'];
		
		$conexion=mysqli_connect($hostname,$username,$password,$database);
		
		$consulta="SELECT * FROM usuarios WHERE user= '{$user}' AND pass = '{$pwd}'";
		$resultado=mysqli_query($conexion,$consulta);

		if($consulta){
		
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}



		else{
			$results["user"]='';
			$results["pwd"]='';
			$results["names"]='';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
		
	}
	else{
		   	$results["user"]='No';
			$results["pwd"]='Responde';
			$results["names"]='La wea';
			$json['datos'][]=$results;
			echo json_encode($json);
		}
?>