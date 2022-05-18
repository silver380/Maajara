from django.urls import path
from .views import TourListAPIView, CreatedTours, Register, MyConfirmedTours, MyPendingTours, \
    MyPendingUsers, MyConfirmedUsers, AcceptUser, Add, TourSuggestion

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
    path('suggestion/<int:pk>/', TourSuggestion.as_view(), name="tour_suggestion"),
]
