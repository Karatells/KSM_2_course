<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){
    include 'connection.php';
    insertData();
}

function insertData()
{
    global $connect;
    
    $NickName = $_POST["NickName"];
    $Pass = $_POST["Password"];
    $NameGroup = $_POST["NameGroup"];
    
    $query = "  INSERT INTO `registration` (`Code_User`, `NickName`, `Password`, `Code_Group`)
                VALUES
                (NULL, '$NickName', '$Pass', (SELECT Code_Group FROM st_groups WHERE NameGroup = '$NameGroup'))";
    
    mysqli_query($connect, $query) or die (mysqli_error($connect));
    mysqli_close($connect);
}

?>
