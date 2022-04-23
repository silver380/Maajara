from django.urls import path
from .views import PlaceListView

urlpatterns = [
    path('all/', PlaceListView.as_view()),
]
