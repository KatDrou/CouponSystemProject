var custApp = angular.module('docust', []);

custApp.controller('custController',function($scope,$http){
	
/////////////////
	$scope.selectedRow = 0;
	$scope.customer;
	$scope.couponTypes=['RESTAURANTS', 'ELECTRICITY', 'FOOD', 'HEALTH', 'SPORTS', 'CAMPING', 'TRAVELLING', 'TOYS', 'CLOTHES'];
	
	$scope.hideAll=function(){
		$scope.WelcomePage = false;
		$scope.MainMenu = true;
		$scope.UpdateCustomer = false;
		$scope.ShowAllCoupons =false;
		$scope.ShowMyCoupons = false;
		$scope.ChoozePrice=false;
		$scope.ChoozeType=false;
		
	}
	
	$http.get('http://localhost:8080/EKLWS/rest/customer/customerInfo').then(function(response) {
		$scope.customer = response.data;
	},function(error){
		alert('operation failed',error.data);
	})
	
	$scope.hideAll();
	$scope.MainMenu = true;
	$scope.WelcomePage = true;
	
	
	$scope.updateMyDetails=function(){
		
		
		$scope.hideAll();
		$scope.UpdateCustomer = true;
		$scope.doUpdate=function(){
				
			var username=$scope.username;
			var password = $scope.password;
			
			if(username == '' && password == ''){
				 window.alert("if you want update, some fields must be fill");
			 }else{
				 if (username == null || username == "" || username == undefined) {
						username = $scope.customer.custName;
						console.log("3" + $scope.username + $scope.password);

					} else if (password == null || password==undefined || password=="") {

						password = $scope.customer.password;
						console.log("4" + $scope.username + $scope.password);

					}
	
			$http.put(
					'http://localhost:8080/EKLWS/rest/customer/update/?username=' + username+ '&password=' + password ).then(
					function(response) {
						window.alert(response.data);
						$scope.hideAll();
						$scope.WelcomePage = true;
						$scope.password='';
						$scope.customer.custName = $scope.username;
						$scope.username='';
			
					},function(error){
						alert('operation failed',error.data);
					});
			}
		}
	}

	$scope.getMyCoupons=function(){
		$scope.hideAll();
		$http.get('http://localhost:8080/EKLWS/rest/customer/purchased').then(function(response){
			$scope.coupons=response.data;
			if($scope.coupons == null){
				alert('not found');
				$scope.hideAll();
				$scope.WelcomePage = true;
				
			}else{
				$scope.hideAll();
				$scope.ShowMyCoupons=true;
			}
		},function(error){
			alert('operation failed',error.data);
		});
		
	}
	
	//////////////////////// FOR SALE
//////////////////////////////////////////////selectRow
	$scope.setClickedRow = function(index){
		$scope.selectedRow = index;
	}
	
	$scope.$watch('selectedRow', function() {
		console.log('Do Some processing');
	});
	////////////////////////////////////////
	$scope.getAllCoupons=function(){
		
		$scope.hideAll();
		$scope.ShowAllCoupons=true;
		$http.get('http://localhost:8080/EKLWS/rest/customer/entire').then(function(response){
			
			$scope.coupons=response.data;
			if($scope.coupons == null){
				alert('not found');
				$scope.hideAll();
				$scope.WelcomePage = true;
				
			}else{
				$scope.hideAll();
				$scope.ShowAllCoupons=true;
			}
		},function(error){
			alert('operation failed ' + error.data);
		});
		
	}
	
	
	$scope.purchaseCoupon=function(){
		
		$scope.id=$scope.coupons[$scope.selectedRow].id;
		
		alert("purchase coupon"+$scope.id);

				$http.get('http://localhost:8080/EKLWS/rest/customer/purchase/'+ $scope.id)
				
				.then(function(response){
					alert(response.data);
					$scope.hideAll();
					$scope.WelcomePage = true;
				},function(error){
					alert('operation failed ' + error.data);
				});

	
		
	}
	
	
		$scope.ShowCouponsByPrice=function(){
		$scope.price='';	
		$scope.hideAll();
		$scope.ChoozePrice=true;
		$scope.getCouponsByPrice=function(){
		$http.get('http://localhost:8080/EKLWS/rest/customer/entire/'+$scope.price).then(function(response){
			$scope.coupons=response.data;
			if(response.data == null){
				alert('not found');
				$scope.hideAll();
				$scope.WelcomePage = true;
				
			}else{
				$scope.hideAll();
				$scope.ShowAllCoupons=true;
				$scope.price='';
			}
		},function(error){
			alert('operation failed ',error.data);
		});
		}
		}
	
		$scope.ShowCouponsByType=function(){
			$scope.hideAll();
			$scope.ChoozeType=true;
			$scope.getCouponsByType=function(){
				$http.get('http://localhost:8080/EKLWS/rest/customer/entirebytype/'+$scope.type)
				.then(function(response){
					$scope.coupons=response.data;
					if($scope.coupons == null){
						alert('not found');
						$scope.hideAll();
						$scope.WelcomePage = true;
						$scope.type='';	

					}else{
						$scope.hideAll();
						$scope.ShowAllCoupons=true;
						$scope.type='';	
					}
				
			},function(error){
				alert('operation failed ',error.data);
			});
			}

				
				}
			
		
//////////////////////////////////////////////selectRow
		$scope.setClickedRow=function(index){
			$scope.selectedRow = index;
		}
		///////////////////////////////////////
		
		
		$scope.LogOut=function(){
			alert("Log Out");
			$http.get('http://localhost:8080/EKLWS/rest/login/off').then(
							function(response){
								
								window.location.assign("http://localhost:8080/EKLWS"); 				
							},function(error){
								alert('operation failed ',error.data);
							});
			
		}
		
});
//////////////////////////////////////////////selectRow
custApp.directive('arrowSelector',['$document',function($document){
	    return{
	        restrict:'A',
	        link:function(scope,elem,attrs,ctrl){
	            var elemFocus = false;             
	            elem.on('mouseenter',function(){
	                elemFocus = true;
	            });
	            elem.on('mouseleave',function(){
	                elemFocus = false;
	            });
	            $document.bind('keydown',function(e){
	                if(elemFocus){
	                    if(e.keyCode == 38){
	                        console.log(scope.selectedRow);
	                        if(scope.selectedRow == 0){
	                            return;
	                        }
	                        scope.selectedRow--;
	                        scope.$apply();
	                        e.preventDefault();
	                    }
	                    if(e.keyCode == 40){
	                        if(scope.selectedRow == scope.coupons.length - 1){
	                            return;
	                        }
	                        scope.selectedRow++;
	                        scope.$apply();
	                        e.preventDefault();
	                    }
	                }
	            });
	        }
	    };
	}]);



