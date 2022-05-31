from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status
from MyUser.models import MyUser

class RegisterAndAuthenticateTest(APITestCase):

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

	def test_should_not_register_invalid_email(self):
		user_info = {
		"email":"uu.com",
		"password":123 ,
		"first_name":"User",
		"last_name" :"User"}
		
		response = self.client.post(('/auth/register/'),user_info)
		self.assertEqual(response.status_code, 400)
		
	def test_should_not_register_invalid_password(self):
		user_info = {
		"email":"uu.com",
		"password":"",
		"first_name":"User",
		"last_name" :"User"}
		
		response = self.client.post(('/auth/register/'),user_info)
		self.assertEqual(response.status_code, 400)

	def test_should_register(self):
		user_info = {
		"email":"u@u.com",
		"password":"usER!@123",
		"first_name":"User",
		"last_name" :"User"}
		
		response = self.client.post(('/auth/register/'),user_info)
		self.assertEqual(response.status_code, 201)


	def test_should_get_token(self):
		user_info = {
		"email":"u@u.com",
		"password":"usER!@123",
		"first_name":"User",
		"last_name" :"User"}
		
		self.client.post(('/auth/register/'),user_info)
		response = self.client.post(('/auth/token/'), {"username":"u@u.com",
				                                       "password":'usER!@123'})

		self.assertEqual(response.status_code, 200)

	def test_should_not_get_token_invalid_username(self):
		user_info = {
		"email":"u@u.com",
		"password":"usER!@123",
		"first_name":"User",
		"last_name" :"User"}
		
		self.client.post(('/auth/register/'),user_info)
		response = self.client.post(('/auth/token/'), {"username":"u@a.com",
				                                       "password":'usER!@123'})
		self.assertEqual(response.status_code, 400)

	def test_should_not_get_token_invalid_password(self):
		user_info = {
		"email":"u@u.com",
		"password":"usER!@123",
		"first_name":"User",
		"last_name" :"User"}
		
		self.client.post(('/auth/register/'),user_info)
		response = self.client.post(('/auth/token/'), {"username":"u@u.com",
				                                       "password":'usER!@12'})
		self.assertEqual(response.status_code, 400)

	def test_should_upgrade_tl(self):

		self.authenticate()
		user_info = {
			"date_of_birth":"2000-12-30",
			"gender":"Female",
			"biography":'tvsbjfsvdfk', 
			"languages":"English", 
			"phone_number":916,
			"ssn":1234}
			
		response = self.client.patch(('/auth/upgrade/'),user_info)
		self.assertEqual(response.status_code, 200)
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

	def test_should_get_user_info(self):
		self.authenticate()
		response = self.client.get(('/auth/info/'))
		self.assertEqual(response.status_code, 200)

	def test_should_not_increase_ticket_empty_value(self):

		self.authenticate_upgrade()
		response = self.client.post(('/auth/increaseticket/'),{})
		self.assertEqual(response.status_code,401 )

	def test_should_not_increase_ticket_neg_value(self):
		self.authenticate_upgrade()
		response = self.client.post(('/auth/increaseticket/'),{"value": -1})
		self.assertEqual(response.status_code,401)

	def test_should_increase_ticket(self):
		self.authenticate_upgrade()
		response = self.client.post(('/auth/increaseticket/'),{"value":5})
		self.assertEqual(response.status_code,200 )

	def test_should_not_increase_ticket_user(self):

		self.authenticate()
		response = self.client.post(('/auth/increaseticket/'),{"value":10})
		self.assertEqual(response.status_code,403)
	def test_should_not_increase_ticket_not_auth(self):

		response = self.client.post(('/auth/increaseticket/'),{"value":10})
		self.assertEqual(response.status_code,401)