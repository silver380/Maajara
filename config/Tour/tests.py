from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status


class TourListAPIViewTest(APITestCase):

	def authenticate(self):
		user_info = {
		"email":"u@u.com",
		"password":123 ,
		"first_name":"User",
		"last_name" :"User"}
		
		self.client.post(('/auth/register/'),user_info)
		response = self.client.post(('/auth/token/'), {"username":"u@u.com",
			                                           "password":123})

		self.client.credentials(HTTP_AUTHORIZATION='Token ' + response.data['token'])


	def test_should_not_list_tours(self):
	 	response = self.client.get('/tour/all/')
	 	self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

	def test_should_list_tours(self):
		self.authenticate()
		response = self.client.get('/tour/all/')
		self.assertEqual(response.status_code, status.HTTP_200_OK)


class AddTourTest(APITestCase):

		def authenticate(self):
			user_info = {
			"email":"u@u.com",
			"password":123 ,
			"first_name":"User",
			"last_name" :"User"}
			
			self.client.post(('/auth/register/'),user_info)
			response = self.client.post(('/auth/token/'), {"username":"u@u.com",
				                                           "password":123})

			self.client.credentials(HTTP_AUTHORIZATION='Token ' + response.data['token'])

		def authenticate_upgrade(self):
		
			self.authenticate()
			user_info = {
				"date_of_birth":"2000-12-30",
				"gender":"Female",
				"biography":'tvsbjfsvdfk', 
				"languages":"English", 
				"phone_number":916,
				"ssn":1234}
				
			response = self.client.patch(('/auth/upgrade/'),user_info)


		def test_should_not_create_tour_invalid_token(self):

			tour_info = {
				    "tour_name": "1",
				    "tour_capacity": 1,
				    "destination": 1,
				    "residence": "Hotel",
				    "start_date": "1980-10-10",
				    "end_date": "1980-10-10",
				    "has_breakfast": "True",
				    "has_lunch": "True",
				    "has_dinner": "True",
				    "has_transportation": "Car",
				    "places": [],
				    "price": 10}

			self.authenticate()

			response = self.client.post(('/tour/addtour/'), tour_info)
			self.assertEqual(response.status_code, 403)

		def test_should_create_tour(self):

			tour_info = {
				    "tour_name": "1",
				    "tour_capacity": 1,
				    "destination": 1,
				    "residence": "Hotel",
				    "start_date": "1980-10-10",
				    "end_date": "1980-10-10",
				    "has_breakfast": "True",
				    "has_lunch": "True",
				    "has_dinner": "True",
				    "has_transportation": "Car",
				    "places": [],
				    "price": 10}

			self.authenticate_upgrade()

			response = self.client.post(('/tour/addtour/'), tour_info)
			self.assertEqual(response.status_code, 200)

