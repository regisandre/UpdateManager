<?php
$smIgnoreFile = fopen("umignore", 'a+');
fclose($smIgnoreFile);
$smZipCreate = fopen("umzip", 'a+');
fclose($smZipCreate);
unlink("umzip");
mkdir("files/", 0705, true);
mkdir("zips/", 0705, true);
function get_zips($path = './') {
    if (substr($path,-1) !== '/')
        $path .= '/';
    $tree = '';
    $dirs = glob($path.'*');
    foreach ($dirs as $value) {
        if(is_dir($value))
            $tree .= "<br>".get_zips($value.'/');
			$smZip = 'umzip';
			$smZipFile = fopen($smZip, 'a+');
			$za = new ZipArchive();
			$za->open($value);
			$info = $za->statname($value);
			for ($i=0; $i<$za->numFiles; $i++) {
				$stat = $za->statIndex($i);
					if($stat['size']!=0) {
						$statList = $stat['name'].PHP_EOL;
						$list = $value.PHP_EOL.$statList;
						$rep1 = str_replace("zips/", "", $list);
						$rep2 = str_replace($file, "", $rep1);
						fwrite($smZipFile, $rep2.$stat["mtime"]."\n");
					}
			}
			closedir($dir);
	    fclose($smZipFile);
	}
	return $tree;
}

function get_files($path = './') {
    if (substr($path,-1) !== '/')
        $path .= '/';
    $tree = '';
    $dirs = glob($path.'*'."/");
    foreach ($dirs as $value) {
        if(is_dir($value))
            $tree .= $value."<br>".get_files($value.'/');
			$rep1 = str_replace("files/", "", $value);
			$rep2 = str_replace("//", "/", $rep1);
			$ext = substr($rep2, -1, 4);
			if($ext == "/") {
			} else {
				echo $rep2."\n".filemtime("files/".$rep2)."\n";
			}
	}
	return $tree;
}

if (file_exists("files/index.php") || file_exists("files/index.html") || file_exists("files/index.htm")) {
	echo "Veuillez supprimer un de ces fichiers si il(s) existe(nt): index.php, index.html ou index.htm.";
} else {
	get_files('files/');
	get_zips('zips/');
}
?>