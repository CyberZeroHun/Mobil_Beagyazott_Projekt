<?php
	//jelöljük, hogy az információ, ami közlekedik json lesz
	header('Content-Type: application/json');

	//ez az osztály reprezentál egy aktivitást
	class Aktivitas {
		public $ido = "";
		public $szog  = "";
		public $tav = "";
	}

	//létrehozunk 3 teszt aktivitást
	$e1 = new Aktivitas();
		$e1->ido = "2016.05.07 - 22:58:24";
		$e1->szog = "97";
		$e1->tav = "127";
	$e2 = new Aktivitas();
		$e2->ido = "2016.05.07 - 22:58:30";
		$e2->szog = "102";
		$e2->tav = "98";
	$e3 = new Aktivitas();
		$e3->ido = "2016.05.07 - 23:00:02";
		$e3->szog = "98";
		$e3->tav = "47";	
			
	//létrehozunk egy új tömböt az aktivitásoknak
	$a=array("aktivitasok" => array	($e1,$e2,$e3));
	
	//kiiratjuk a json választ
	echo json_encode($a);
?>