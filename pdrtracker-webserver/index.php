<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 *
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $isActive = $db->isUserActive($email);
            if($user["is_active"] == 1){
	            $response["success"] = 1;
	            $response["uid"] = $user["unique_id"];
	            $response["user"]["name"] = $user["name"];
	            $response["user"]["email"] = $user["email"];
	            $response["user"]["created_at"] = $user["created_at"];
	            $response["user"]["active"] = $user["is_active"];
	            
	            echo json_encode($response);
            }
            else{
	            // user not active
	            // echo json with error = 1
	            $response["error"] = 1;
	            $response["error_msg"] = "The User Is Not Active";
	            echo json_encode($response);

            }
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
 
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } else if ($tag == 'reset_users_password'){
    // Request type is change user password
	        $user_name = $_POST['user_name'];
	 		$new_password = $_POST['new_password'];

	        $user = $db->changeUserPassword($user_name, $new_password);
			if($user != false){
	            // echo json with success = 1
	            $response["success"] = 1;
	            echo json_encode($response);
	        } else {
	            // user not found
	            // echo json with error = 1
	            $response["error"] = 1;
	            $response["error_msg"] = "Error Changing Password!";
	            echo json_encode($response);
	        }

 	} else if ($tag == 'change_user_password') {
        
        	// Request type is check Login
	        $user_name = $_POST['user_name'];
	        $current_password = $_POST['current_password'];
	 		$new_password = $_POST['new_password'];

	        // check for user
	        $user = $db->getUserByEmailAndPassword($user_name, $current_password);
	        if ($user != false) {
	            // user found
	            $user = $db->changeUserPassword($user_name, $new_password);

	            // echo json with success = 1
	            $response["success"] = 1;
	            echo json_encode($response);
	        } else {
	            // user not found
	            // echo json with error = 1
	            $response["error"] = 1;
	            $response["error_msg"] = "Incorrect email or password!";
	            echo json_encode($response);
	        }


	} else if ($tag == 'new_user_request') {
        // Request type is request for new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $phone = $_POST['phone'];
 
            // store new user request
            $user = $db->storeNewUserRequest($name, $email, $phone);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["id"] = $user["id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["timestamp"] = $user["timestamp"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in storing new user request";
                echo json_encode($response);
            }
          
        } else if ($tag == 'get_new_user_requests') {
	        // Request type is get requests for new users
	 
	        	            // get new user requests
	            $user = $db->getNewUserRequests();
	            if ($user ) {
	                // user stored successfully
	                $response["success"] = 1;
	                $response["results"] = $user;
	                //$response["user"]["name"] = $user["name"];
	               // $response["user"]["email"] = $user["email"];
	               // $response["user"]["phone"] = $user["phone"];
	               // $response["user"]["timestamp"] = $user["timestamp"];
	                echo json_encode($response);
	            } else {
	                // failed to get new requests
	                $response["error"] = 1;
	                $response["error_msg"] = "Error occured in retrieving new requests";
	                echo json_encode($response);
	            }
        
	} else if ($tag == 'update_contacted_clients') {
        // Request type update contacted users
        $ids = $_POST['ids'];
 
            // store new user request
            $user = $db->updateContactedUsers($ids);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["query_string"] = "Update requests_new_user set has_been_contacted = true where id = ".$ids;
                $response["id"] = $user["id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["timestamp"] = $user["timestamp"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in storing new user request";
                echo json_encode($response);
            }       
	} else if ($tag == 'deactivate_user') {
        // Request type update contacted users
        $user_name = $_POST['user_name'];
 
            // store new user request
            $user = $db->deactivateUser($user_name);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in deactivating user account";
                echo json_encode($response);
            } 
            } else if ($tag == 'reactivate_user') {
        // Request type update contacted users
        $user_name = $_POST['user_name'];
 
            // store new user request
            $user = $db->reactivateUser($user_name);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in reactivating user account";
                echo json_encode($response);
            }         

    } else {
        echo "Invalid Request";
    }
} else {
    echo "Access Has Been Denied";
}
?>