
<?php
require'conexion.php';



$json=array();


$sttm=$conexion->prepare('select * from noticia2 WHERE ocultar=1 ORDER BY id DESC');
$sttm->execute();

function strip_tags_deep($value)
{
  return is_array($value) ?
    array_map('strip_tags_deep', $value) :
    strip_tags( $value ) ;
}



while ($datos=$sttm->fetch(PDO::FETCH_ASSOC)){
	$datos=strip_tags_deep($datos);
	$json['datos'][]= $datos;
}


echo json_encode($json, JSON_UNESCAPED_UNICODE);


?>