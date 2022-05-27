from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status
# import pdb
# pdb.set_trace()

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

		#TODO

