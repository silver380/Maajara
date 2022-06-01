from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status
from Tour.models import Tour

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
			user_info = {
			"email":"tl@tl.com",
			"password":123 ,
			"first_name":"tl",
			"last_name" :"tl"}
			
			self.client.post(('/auth/register/'),user_info)
			response = self.client.post(('/auth/token/'), {"username":"tl@tl.com",
				                                           "password":123})

			self.client.credentials(HTTP_AUTHORIZATION='Token ' + response.data['token'])
			user_info = {
				"date_of_birth":"2000-12-30",
				"gender":"Female",
				"biography":'tvsbjfsvdfk', 
				"languages":"English", 
				"phone_number":916,
				"ssn":1234}
				
			response = self.client.patch(('/auth/upgrade/'),user_info)

		def create_tour(self):
			self.authenticate_upgrade()
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
			

			self.client.post(('/tour/addtour/'), tour_info)
			tour_info = {
				    "tour_name": "1",
				    "tour_capacity": 1,
				    "destination": 1,
				    "residence": "Hotel",
				    "start_date": "2023-10-10",
				    "end_date": "2023-10-10",
				    "has_breakfast": "True",
				    "has_lunch": "True",
				    "has_dinner": "True",
				    "has_transportation": "Car",
				    "places": [],
				    "price": 10}
			self.client.post(('/tour/addtour/'), tour_info)
			tour_info = {
				    "tour_name": "1",
				    "tour_capacity": 1,
				    "destination": 1,
				    "residence": "Hotel",
				    "start_date": "2023-10-10",
				    "end_date": "2023-10-10",
				    "has_breakfast": "True",
				    "has_lunch": "True",
				    "has_dinner": "True",
				    "has_transportation": "Car",
				    "places": [],
				    "price": 10}
			self.client.post(('/tour/addtour/'), tour_info)

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

			response = self.client.post(('/tour/addtour/'), tour_info)
			self.assertEqual(response.status_code, 401)

		def test_should_not_create_tour_incomplete_data(self):

			tour_info = {
				    "tour_name": "1",
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



		def test_should_list_created_tours(self):

			self.authenticate_upgrade()
			response = self.client.get('/tour/createdtours/')
			self.assertEqual(response.status_code, 200)

		def test_should_get_confirmed_tours(self):

			self.authenticate()
			response = self.client.get('/tour/confirmedtours/')
			self.assertEqual(response.status_code, 200)

		def test_should_get_pending_tours(self):

			self.authenticate()
			response = self.client.get('/tour/pendingtours/')
			self.assertEqual(response.status_code, 200)


		def test_should_not_get_confirmed_tours(self):

			response = self.client.get('/tour/confirmedtours/')
			self.assertEqual(response.status_code, 401)

		def test_should_not_get_pending_tours(self):

			response = self.client.get('/tour/pendingtours/')
			self.assertEqual(response.status_code, 401)

		def test_should_not_get_pending_users_not_tl(self):

			self.authenticate()
			response = self.client.get('/tour/pendingusers/')
			self.assertEqual(response.status_code, 403)

		def test_should_not_get_pending_users_not_auth(self):

			response = self.client.get('/tour/pendingusers/')
			self.assertEqual(response.status_code, 401)

		def test_should_get_pending_users(self):

			self.create_tour()
			response = self.client.get('/tour/pendingusers/')
			self.assertEqual(response.status_code, 200)

		def test_should_not_get_confirmed_users_not_tl(self):

			self.authenticate()
			response = self.client.get('/tour/confirmedusers/')
			self.assertEqual(response.status_code, 403)

		def test_should_not_get_pending_users_not_auth(self):

			response = self.client.get('/tour/confirmedusers/')
			self.assertEqual(response.status_code, 401)

		def test_should_get_confirmedusers_users(self):

			self.create_tour()
			response = self.client.get('/tour/confirmedusers/')
			self.assertEqual(response.status_code, 200)

		def test_should_not_get_archived_tourTl_not_tl(self):

			self.authenticate()
			response = self.client.get('/tour/archivedtl/')
			self.assertEqual(response.status_code, 403)

		def test_should_not_get_archived_tourTl_not_auth(self):

			response = self.client.get('/tour/archivedtl/')
			self.assertEqual(response.status_code, 401)

		def test_should_get_archived_tourTl(self):

			self.create_tour()
			response = self.client.get('/tour/archivedtl/')
			self.assertEqual(response.status_code, 200)


		def test_should_not_get_archived_touruser_not_auth(self):

			response = self.client.get('/tour/archiveduser/')
			self.assertEqual(response.status_code, 401)

		def test_should_get_archived_tourTl(self):

			self.authenticate()
			response = self.client.get('/tour/archiveduser/')
			self.assertEqual(response.status_code, 200)


		def test_should_not_register_incomplete_data(self):
			
			self.authenticate() #authenticate user
			register_info={

			}
			response = self.client.post(('/tour/register/'), register_info)
			self.assertEqual(response.status_code, 400)

		def test_should_not_register_invalid_tour(self):
			self.authenticate() #authenticate user
			register_info={
				"tour_id" : 1
			}
			response = self.client.post(('/tour/register/'), register_info)
			self.assertEqual(response.status_code, 404)

		def test_should_not_register_invalid_date(self):
			self.create_tour()
			self.authenticate()
			register_info={
				"tour_id" : 1
			}
			response = self.client.post(('/tour/register/'), register_info)
			self.assertEqual(response.status_code, 400)

		def test_should_register(self):
			self.create_tour()
			self.authenticate()
			register_info={
				"tour_id" : 3
			}
			response = self.client.post(('/tour/register/'), register_info)
			self.assertEqual(response.status_code, 200)


		def test_should_not_register_invalid_full(self):
			self.create_tour()
			self.authenticate()
			register_info={
					"tour_id" : 1
				}
			self.client.post(('/tour/register/'), register_info)
			self.authenticate()
			response = self.client.post(('/tour/register/'), register_info)
			self.assertEqual(response.status_code, 400)



