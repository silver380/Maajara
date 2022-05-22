from django.urls import path
from .views import  TourListAPIView, CreatedTours, Register, MyConfirmedTours, MyPendingTours, \
    MyPendingUsers, MyConfirmedUsers, AcceptUser, Add, AddRate, GetRate

app_name = 'Tour'
urlpatterns = [
    path('all/', TourListAPIView.as_view(), name="all_tours"),
    path('register/', Register.as_view(), name="created_tours"),
    path('createdtours/', CreatedTours.as_view(), name="created_tours"),
    path('confirmedtours/', MyConfirmedTours.as_view(), name="created_tours"),
    path('pendingtours/', MyPendingTours.as_view(), name="created_tours"),
    path('pendingusers/', MyPendingUsers.as_view(), name="created_tours"),
    path('confirmedusers/', MyConfirmedUsers.as_view(), name="created_tours"),
    path('addtour/', Add.as_view(), name="add_tour"),
    path('acceptuser/', AcceptUser.as_view(), name="accept_user"),
    path('addrate/', AddRate.as_view(), name="add_rate"),
    path('getrate/', GetRate.as_view(), name="get_rate"),
   # path('searchtour/', SearchTour.as_view(), name="search_tour"),
]
