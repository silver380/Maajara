from django.urls import path, include
from .views import TourListAPIView, CreatedTours, Register, MyConfirmedTours, MyPendingTours ,MyPendingUsers,AddTour

app_name = 'Tour'
urlpatterns = [
    path('all/', TourListAPIView.as_view(), name="all_tours"),
    #should they have a same name?
    path('register/', Register.as_view(), name="created_tours"),
    path('createdtours/', CreatedTours.as_view(), name="created_tours"),
    path('confirmedtours/', MyConfirmedTours.as_view(), name="created_tours"),
    path('pendingtours/', MyPendingTours.as_view(), name="created_tours"),
    path('pendingusers/', MyPendingUsers.as_view(), name="created_tours"),
    path('addtour/',AddTour.as_view(), name = "add_tour"),
]
