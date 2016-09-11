angular.module('login',[]);

angular.module('login').controller('dologin',function($rootScope,$scope,$http){
	
	
	
	$scope.submit=function(){
	$http.get("http://localhost:8080/EKLWS/rest/login/" + $scope.username +"/" + $scope.password + "/" + $scope.cType).then(
		function(response){
			$scope.login = response.data;
			switch ($scope.login){
			
				case "COMPANY":
					window.location.assign("http://localhost:8080/EKLWS/company.html")
					break;
				
				case "CUSTOMER":
					window.location.assign("http://localhost:8080/EKLWS/customer.html")
					break;
				
				case "ADMIN":	
					window.location.assign("http://localhost:8080/EKLWS/admin.html")
					break;
				default:
					window.location.assign("http://localhost:8080/EKLWS")
		}

	},function(error){
		alert('operation failed' + error.data);
	});
	};

});



