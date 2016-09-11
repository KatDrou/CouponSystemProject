var compApp = angular.module('docompany', []);
compApp.controller('cmpControllers',function($rootScope, $scope, $http) {

					$scope.hideAllTables = function() {
						$scope.welcomePage = false;
						$scope.createCoupon = false;
						$scope.showTableAllCoupons = false;
						$scope.showTableCompany = false;
						$scope.showTableActiveCoupon = false;
						$scope.showTableInactCoupons = false;
						$scope.updateCompany = false;
						$scope.addCoupon = false;
						$scope.showFindCoupon = false;
						$scope.showUpdateCoupon = false;
						$scope.showCouponByPrice = false;
						$scope.showCouponByPriceTable = false;
						$scope.showCouponByType = false;
						$scope.showCouponByTypeTable = false;
						$scope.showCouponByDate = false;
						$scope.showCouponByDateTable = false;
						$scope.showRemoveCoupon = false;
						$scope.showImage = false;
					}

					$scope.hideAllTables();
					$scope.welcomePage = true;
					$http.get('http://localhost:8080/EKLWS/rest/company/getcompany').then(function(response) {
								$scope.company = response.data;
							}, function(error) {
								alert('operation failed ', error.data);
							});

					// companyDetails
					$scope.companyDetails = function() {
						$scope.hideAllTables();
						$scope.showTableCompany = true;
						$http.get('http://localhost:8080/EKLWS/rest/company/getcompany').then(function(response) {
									$scope.company = response.data;
								}, function(error) {
									alert('operation failed ', error.data);
								});
					}

					// getAllCoups
					$scope.getAllCoups = function() {
						$scope.hideAllTables();
						$scope.showTableActiveCoupon = true;
						$scope.showTableInactCoupons = true;
						/*
						 * $http.get("http://localhost:8080/EKLWS/rest/company/allcoupons").then(
						 * function(response) {
						 * //console.log(response.data.coupon); $scope.coupons =
						 * response.data; });
						 */

						$http.get('http://localhost:8080/EKLWS/rest/company/activecoupons').then(
										function(response) {
											$scope.couponsAct = response.data;

											// remove selected coupon
											$scope.removeCouopn = function(id) {
												$scope.submitRemove = function() {
													$http.put('http://localhost:8080/EKLWS/rest/company/remove/'+ id).then(
																	function(response) {
																		$scope.resRemove = response.data;
																		$scope.getAllCoups();
																	},function(error) {
																		alert('operation failed ',error.data);
																	})
												}
											}
										},
										function(error) {
											alert('operation failed ',error.data);
										});
						$http.get('http://localhost:8080/EKLWS/rest/company/inactcoupons').then(
										function(response) {
											$scope.couponsInact = response.data;

											// reactivate selected coupon
											$scope.reActCouopn = function(id) {
												$scope.submitReAct = function() {
													$http.put('http://localhost:8080/EKLWS/rest/company/activatecoupon/'+ id).then(
																	function(response) {
																		$scope.resReAct = response.data;
																		$scope.getAllCoups();
																	},function(error) {
																		alert('operation failed '+error.data);
																	})
												}
											}
										},
										function(error) {
											alert('operation failed ',error.data);
										});
					}

					// // getCompanyDetails
					// $scope.getCompanyDetails = function() {
					// $scope.hideAllTables();
					// $scope.showTableCompany = true;
					// $http.get('http://localhost:8080/EKLWS/rest/company/getcompany').then(
					// function(response) {
					// $scope.company = response.data;
					// });
					// }

					// updateCompanyDetails
					$scope.updateCompanyDetails = function() {
						$scope.hideAllTables();
						$scope.updateCompany = true;
						$scope.cmpUpdated = "";

						$scope.newCompanyDetails = function() {
							if ($scope.company.email == null && $scope.company.password == null) {
								window.alert("Fill at least one field"+ $scope.password + ':' + $scope.email);
							} else {
								$http.put('http://localhost:8080/EKLWS/rest/company/updatecompany?mail='
														+ $scope.company.email
														+ '&pass='
														+ $scope.company.password).then(
												function(response) {
													$scope.cmpUpdated = response.data;
													// console.log($scope.cmpUpdated
													// + ' mail='
													// + $scope.email + '&pass='
													// + $scope.password);
												},
												function(error) {
													alert('operation failed '+ error.data);
												});
							}
						}
					}

					// createCoupon
					$scope.createNewCoupon = function() {
						$scope.hideAllTables();
						$scope.addCoupon = true;
						$scope.couponWasCreated = null;

						$scope.today = new Date();
						$http.get('http://localhost:8080/EKLWS/rest/company/getcoupontypes/').then(function(response) {
									$scope.couponTypes = response.data;
								}, function(error) {
									alert('operation failed ', error.data);
								});

						$scope.submitNewCoupon = function() {
							if ($scope.addTitle == null
									|| $scope.addSDate == null
									|| $scope.addEDate == null
									|| $scope.addAmount == null
									|| $scope.addCouponType == null
									|| $scope.addPrice == null) {
								alert("Fill all mandatory fields");
							} else {
								var sDateMillis = new Date($scope.addSDate).getTime();
								var eDateMillis = new Date($scope.addEDate).getTime();

								$http.post('http://localhost:8080/EKLWS/rest/company/addcoup/'
														+ $scope.addTitle + '/'
														+ sDateMillis + '/'
														+ eDateMillis + '/'
														+ $scope.addAmount + '/'
														+ $scope.addCouponType + '/'
														+ $scope.addMessage + '/' 
														+ $scope.addPrice).then(
												function(response) {
													$scope.couponWasCreated = response.data;
												},function(error) {
													alert('operation failed '+ error.data);
												});
								$scope.addTitle = null;
								$scope.addSDate = null;
								$scope.addEDate = null;
								$scope.addAmount = null;
								$scope.addCouponType = null;
								$scope.addPrice = null;
								$scope.addMessage = null;
							}
						}
					}

					// update coupon
					$scope.updateCoupon = function() {
						$scope.hideAllTables();
						$scope.showFindCoupon = true;
						$scope.today = new Date();
						$scope.tomorrow = new Date();
						$scope.tomorrow.setDate($scope.today.getDate() + 1);

						$scope.findCouponById = function() {
							$scope.couponWasUpdated = null;
							$http.get('http://localhost:8080/EKLWS/rest/company/getcoupon/'+ $scope.id).then(
											function(response) {
												$scope.showUpdateCoupon = true;
												$scope.coupon = response.data;
												//$scope.origEDate = new Date($scope.coupon.endDate);
												//$scope.eDate = $scope.origEDate;
												$scope.amount = $scope.coupon.amount;
												$scope.message = $scope.coupon.message;
												$scope.price = $scope.coupon.price;

												$scope.submitUpdatedCoupon = function() {
													if ($scope.eDate == null
															|| $scope.amount == null
															|| $scope.price == null) {
														alert("Fill all mandatory fields");
													} else {
														var eDateMillis = new Date($scope.eDate).getTime();

														$http.put('http://localhost:8080/EKLWS/rest/company/updatecoupon/'+ $scope.id
																				+ '?endDate='+ eDateMillis
																				+ '&amount='+ $scope.amount
																				+ '&message='+ $scope.message
																				+ '&price='+ $scope.price).then(
																		function(response) {
																			$scope.couponWasUpdated = response.data;
																			$scope.coupon = null;
																			$scope.eDate = null;
																			$scope.amount = null;
																			$scope.message = null;
																			$scope.price = null;
																			$scope.id = null;
																		},function(error) {
																			alert('operation failed '+ error.data);
																		})
														$scope.showUpdateCoupon = false;
													}
												}
											},
											function(error) {
												alert('operation failed '+ error.data);
											})
						}
						$scope.couponWasUpdated = null;
					}

					// find coupon by price
					$scope.findCouponByPrice = function() {
						$scope.hideAllTables();
						$scope.showCouponByPrice = true;

						$scope.couponsToFind = function() {
							if ($scope.findByPrice == null) {
								alert("Please enter price")
							} else {
								$scope.showCouponByPriceTable = true;
								$http.get('http://localhost:8080/EKLWS/rest/company/couponbyprice/'+ $scope.findByPrice).then(
										function(response) {
											$scope.resByPrice = response.data;
										},function(error) {
											alert('operation failed ',error.data);
										})
							}
						}

					}

					// find coupon by type
					$scope.findCouponByType = function() {
						$scope.hideAllTables();
						$scope.showCouponByType = true;

						$http.get('http://localhost:8080/EKLWS/rest/company/getcoupontypes/').then(function(response) {
									$scope.couponTypes = response.data;
								}, function(error) {
									alert('operation failed ', error.data);
								});

						$scope.couponsToFind = function() {
							if ($scope.findByType == null) {
								alert("Please select type")
							} else {
								$scope.showCouponByTypeTable = true;
								$http.get('http://localhost:8080/EKLWS/rest/company/couponbytype/'+ $scope.findByType).then(
										function(response) {
											$scope.resByType = response.data;
										},
										function(error) {
											alert('operation failed ',error.data);
										})
							}
						}

					}

					// find coupon by date
					$scope.findCouponByDate = function() {
						$scope.hideAllTables();
						$scope.showCouponByDate = true;

						$scope.couponsToFind = function() {
							if ($scope.findByDate == null) {
								alert("Please enter date")
							} else {
								var eDateMillis = new Date($scope.findByDate).getTime();
								$scope.showCouponByDateTable = true;
								$http.get('http://localhost:8080/EKLWS/rest/company/couponbydate/'+ eDateMillis).then(
										function(response) {
											$scope.resByDate = response.data;
										},
										function(error) {
											alert('operation failed ',error.data);
										})
							}
						}
					}

					// Log off
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
