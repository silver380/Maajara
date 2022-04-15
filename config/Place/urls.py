from django.urls import path, include
from .views import PlaceListView

urlpatterns = [
    path('all/', PlaceListView.as_view()),

]
