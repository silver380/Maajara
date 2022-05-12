from django.urls import path
from .views import PlaceListView

urlpatterns = [
    path('', PlaceListView.as_view()),
]
