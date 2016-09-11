var admApp = angular.module('doadmin', []);

admApp.controller('admController', function($scope, $http) {
	$scope.MainMenu = true;
	$scope.WelcomePage = true;

	$scope.AddCompany = false;
	$scope.ShowAllCustomers = false;
	$scope.ShowAllCompanies = false;
	$scope.CompanyDetails = false;
	$scope.CustomerDetails = false;
	$scope.ShowCompany = false;
	$scope.ShowCustomer = false;
	$scope.AddCustomer = false;
	$scope.DeleteComp = false;
	$scope.UpdateComp = false;
	$scope.DeleteCust = false;
	$scope.UpdateCust = false;
	$scope.ShowContactPage = false;
	$scope.ShowAboutPage = false;
	$scope.listCoupons = false;

	$scope.hideAll = function() {

		$scope.WelcomePage = false;
		$scope.AddCompany = false;
		$scope.ShowAllCustomers = false;
		$scope.ShowAllCompanies = false;
		$scope.CompanyDetails = false;
		$scope.CustomerDetails = false;
		$scope.ShowCompany = false;
		$scope.ShowCustomer = false;
		$scope.AddCustomer = false;
		$scope.DeleteComp = false;
		$scope.UpdateComp = false;
		$scope.DeleteCust = false;
		$scope.UpdateCust = false;
		$scope.ShowContactPage = false;
		$scope.ShowAboutPage = false;
		$scope.listCoupons = false;
	}

	$scope.ShowWelcomePage = function() {

		$scope.hideAll();
		$scope.MainMenu = true;
		$scope.WelcomePage = true;

		$scope.name = '';
		$scope.password = '';
		$scope.email = '';
		$scope.method = '';
		$scope.id = '';
	}

	$scope.ShowAboutPage = function() {
		$scope.hideAll();
		$scope.MainMenu = false;
		$scope.ShowAboutPage = true;

	}
	$scope.ShowContactPage = function() {
		$scope.hideAll();
		$scope.MainMenu = false;
		$scope.ShowContactPage = true;
	}

	$scope.getAllCusts = function() {
		//alert("all custs")
		$scope.hideAll();
		$scope.ShowAllCustomers = true;

		$http.get("http://localhost:8080/EKLWS/rest/admin/getallcust").then(
				function(response) {
					$scope.customers = response.data;

				}, function(error) {
					alert('operation failed' + error.data);
				});
	}

	$scope.getAllComps = function() {
		//alert("all comps")
		$scope.hideAll();
		$scope.ShowAllCompanies = true;

		$http.get("http://localhost:8080/EKLWS/rest/admin/getallcomp").then(
				function(response) {
					$scope.companies = response.data;
				}, function(error) {
					alert('operation failed' + error.data);
				});
	}
	// ////////////////////FOR COMPANY
	$scope.company;
	$scope.FindCompById = function() {
		$scope.hideAll();
		$scope.CompanyDetails = true;
		$scope.id = '';
		$scope.method = 'ShowDetails';
		$scope.executeForCompany = function(method) {

			if ($scope.id == '') {
				window.alert("You must enter ID")
			} else {
				switch (method) {
				case ("Update"):
					$scope.UpdateComp = true;
					$scope.DeleteComp = false;
					$scope.ShowCompany = false;
					$scope.listCoupons = false;
					break;

				case ("Delete"):
					$scope.DeleteComp = true;
					$scope.UpdateComp = false;
					$scope.ShowCompany = false;
					$scope.listCoupons = false;
					break;
				case ('ShowDetails'):
					$scope.UpdateComp = false;
					$scope.DeleteComp = false;

					$scope.company;
					alert("find comp");

					$http.get(
							'http://localhost:8080/EKLWS/rest/admin/getcomp/'
									+ $scope.id).then(function(response) {

						$scope.company = response.data;
						if ($scope.company.id == 0) {
							alert('company not found');
							$scope.hideAll();
							$scope.CompanyDetails = true;
						} else {
							$scope.ShowCompany = true;
							$scope.listCoupons = false;
							$scope.showCoupons = function() {
								if ($scope.company.coupons.length==0) {
									alert('coupons not found');
								} else {
									$scope.listCoupons = true;
									$scope.coupons = response.data.coupons;

								}
							}
						}
					}, function(error) {
						alert('operation failed' + error.data);
					});

					break;

				default:
					return "You choose an Unvalid option";
					$scope.hideAll();
					$scope.WelcomePage = true;
					$scope.id = '';
				}
			}
		}

	}

	$scope.delCompany = function() {

		alert("do you want delete company?");

		$http({
			method : 'DELETE',
			url : 'http://localhost:8080/EKLWS/rest/admin/delcomp/' + $scope.id
		}).then(function(response) {
			window.alert('deleted');
			$scope.hideAll();
			$scope.WelcomePage = true;
			$scope.id = '';

		}, function(error) {
			alert('operation failed' + error.data);
		});
	}

	$scope.updCompany = function() {

		alert("do you want update company?");
		var password = $scope.password;
		var email = $scope.email;

		if (email == null && password == null) {

			window.alert("if you want update, all fields must be fill");
		} else {
			if (email == null || email == "" || email == undefined) {
				email = $scope.company.email;
				console.log("3" + $scope.email + $scope.password);

			} else if (password == null || password==undefined || password=="") {

				password = $scope.customer.password;
				console.log("4" + $scope.email + $scope.password);

			}

			$http.put(
					'http://localhost:8080/EKLWS/rest/admin/updcomp/'
							+ $scope.id + '?password=' + password + '&email='
							+ email).then(function(response) {
				window.alert('updated');
				$scope.password = '';
				$scope.email = '';
				$scope.id = '';
				$scope.hideAll();
				$scope.WelcomePage = true;

			}, function(error) {
				alert('operation failed', error.data);
			});
		}
	}

	$scope.CreateComp = function() {
		$scope.hideAll();
		$scope.AddCompany = true;

		$scope.addCompany = function() {
			alert("do you want add company?")
			if ($scope.email == null || $scope.password == null
					|| $scope.name == null) {
				window.alert("all fields must be fill")
			} else {

				$http.post(
						'http://localhost:8080/EKLWS/rest/admin/addcomp/'
								+ $scope.name + '/' + $scope.password + '/'
								+ $scope.email).then(function(response) {

					alert("added");
					$scope.WelcomePage = true;
					$scope.AddCompany = false;
				}, function(error) {
					alert('operation failed', error.data);
				});
			}
		};
	}
	// /////////////////////////for customer
	$scope.customer;
	$scope.FindCustById = function() {
		$scope.hideAll();
		$scope.CustomerDetails = true;
		$scope.id = '';
		$scope.method = 'ShowDetails';

		$scope.executeForCustomer = function(method) {
			if ($scope.id == '') {
				window.alert("You must enter ID")
			} else {

				switch (method) {

				case ("Update"):
					$scope.DeleteCust = false;
					$scope.ShowCustomer = false;
					$scope.listCoupons = false;
					$scope.UpdateCust = true;

					break;

				case ("Delete"):

					$scope.UpdateCust = false;
					$scope.ShowCustomer = false;
					$scope.listCoupons = false;
					$scope.DeleteCust = true;
					break;

				case ('ShowDetails'):

					alert("find cust")
					$scope.customer;
					$http.get(
							'http://localhost:8080/EKLWS/rest/admin/getcust/'
									+ $scope.id).then(function(response) {

						$scope.customer = response.data;
						if ($scope.customer == null) {
							alert('customer not found');
							$scope.hideAll();
							$scope.CustomerDetails = true;
							$scope.id = '';
						} else {

							$scope.ShowCustomer = true;
							$scope.listCoupons = false;
							$scope.showCoupons = function() {
								if ($scope.customer.coupons.length==0) {
									alert('coupons not found');
								} else {

									$scope.listCoupons = true;
									$scope.coupons = response.data.coupons;
								}
							}

						}
					}, function(error) {
						alert('operation failed' + error.data);
					});
					break;

				default:
					return "You choose an Unvalid option";
					$scope.hideAll();
					$scope.WelcomePage = true;
					$scope.id = '';
				}
			}

		}

	}

	$scope.delCustomer = function() {

		alert("do you want delete customer?");

		
		$http({
			method : 'DELETE',
			url : 'http://localhost:8080/EKLWS/rest/admin/delcust/' + $scope.id
		}).then(function(response) {
			window.alert('deleted');
			$scope.hideAll();
			$scope.WelcomePage = true;
			$scope.id = '';
		}, function(error) {
			alert('operation failed' , error.data);
		});
	}

	$scope.updCustomer = function() {
 
		alert("do you want update customer");
		var password = $scope.password;
		var name = $scope.name;
		console.log("1" + $scope.name + $scope.password);
		console.log("1" + $scope.customer);

		if (name == null&& password == null) {
			window.alert("if you want update, some fields must be fill");
			console.log("2" + $scope.name + $scope.password);

		} else {
			if (name == null || name == "" || name == undefined) {
				name = $scope.customer.custName;
				console.log("3" + $scope.name + $scope.password);

			} else if (password == null || password==undefined || password=="") {

				password = $scope.customer.password;
				console.log("4" + $scope.name + $scope.password);

			}
			console.log(name+password);

			$http.put(
					'http://localhost:8080/EKLWS/rest/admin/updcust/'
							+ $scope.id + '?name=' + name + '&password='
							+ password).then(function(response) {
				window.alert(response.data);
				$scope.hideAll();
				$scope.WelcomePage = true;
				$scope.id = '';
				$scope.name='';
				$scope.password='';
				
			}, function(error) {
				alert('operation failed '+ error.data);
			});
		}
	}

	$scope.CreateCust = function() {
		$scope.name = '';
		$scope.password = '';
		$scope.hideAll();
		$scope.AddCustomer = true;

		$scope.addCustomer = function() {
			alert("do you want add customer?")
			if ($scope.password == null || $scope.name == null) {
				window.alert("all fields must be fill")
			} else {

				$http.post(
						'http://localhost:8080/EKLWS/rest/admin/addcust/'
								+ $scope.name + '/' + $scope.password).then(
						function(response) {
							alert("added");
							$scope.hideAll();
							$scope.WelcomePage = true;
							$scope.name = '';
							$scope.password = '';

						}, function(error) {
							alert('operation failed '+ error.data);
						});
			}

		};
	}

	$scope.LogOut = function() {
		alert("Log Out");
		$http.get('http://localhost:8080/EKLWS/rest/login/off').then(
				function(response) {

					window.location.assign("http://localhost:8080/EKLWS");
				}, function(error) {
					alert('operation failed ' , error.data);
				});

	}
});
