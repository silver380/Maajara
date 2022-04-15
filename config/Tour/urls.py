from django.urls import path, include
from .views import TourListAPIView, CreatedTours, Register

app_name = 'Tour'
urlpatterns = [
    path('all/', TourListAPIView.as_view(), name="all_tours"),
    path('register/', Register.as_view(), name="created_tours"),
    path('createdtours/', CreatedTours.as_view(), name="created_tours")
]
