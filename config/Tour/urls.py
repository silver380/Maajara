from django.urls import path, include
from .views import TourListAPIView

app_name = 'Tour'
urlpatterns = [
    path('', TourListAPIView.as_view(), name="get_tours"),
]
